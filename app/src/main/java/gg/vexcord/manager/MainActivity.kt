package gg.vexcord.manager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.provider.OpenableColumns
import org.json.JSONObject

class MainActivity : Activity() {
    private val bg = Color.rgb(7, 10, 16)
    private val panel = Color.rgb(18, 24, 35)
    private val blue = Color.rgb(55, 150, 255)
    private val prefs by lazy { getSharedPreferences("vexcord_plugins", MODE_PRIVATE) }
    private var selectedApk: Uri? = null
    private lateinit var selectedLabel: TextView
    private lateinit var installButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        buildScreen()
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun buildScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(18), dp(20), dp(18), dp(12))
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val title = TextView(this).apply {
            text = "VEXCORD"
            textSize = 28f
            setTextColor(Color.WHITE)
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
        }
        content.addView(title)
        content.addView(label("INDEPENDENT ANDROID MANAGER", 11, blue, true))
        content.addView(space(14))
        val status = TextView(this).apply {
            text = "Your customization hub"
            textSize = 18f
            setTextColor(Color.WHITE)
        }
        content.addView(status)
        content.addView(label("Manage local plugin preferences. Toggles save on this device.", 13, Color.LTGRAY))
        content.addView(space(18))
        content.addView(section("VEXCORD PATCH INSTALLER"))
        content.addView(cardText("Select Discord APK", "Choose the Discord APK you want to work with. Vexcord will only install a modified package after its own patching and signing pipeline is implemented."))
        content.addView(Button(this).apply {
            text = "Choose APK"
            setOnClickListener {
                startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/vnd.android.package-archive"
                }, 410)
            }
        }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(8) })
        selectedLabel = label("No APK selected", 12, Color.LTGRAY)
        content.addView(selectedLabel)
        installButton = Button(this).apply {
            text = "Install selected APK"
            isEnabled = false
            setOnClickListener { installSelectedApk() }
        }
        content.addView(installButton, LinearLayout.LayoutParams(-1, -2))
        content.addView(cardText("Patching status", "This build can select an APK and pass it to Android's installer. Vexcord's own Discord patching and signing engine is still under development; this button does not patch the selected APK."))
        content.addView(space(18))
        content.addView(section("PLUGIN LIBRARY"))
        val catalog = try {
            JSONObject(assets.open("plugin-catalog.json").bufferedReader().use { it.readText() })
                .optJSONArray("plugins") ?: org.json.JSONArray()
        } catch (_: Exception) { org.json.JSONArray() }

        if (catalog.length() == 0) {
            content.addView(cardText("Catalog unavailable", "The plugin catalog could not be loaded."))
        } else {
            for (i in 0 until catalog.length()) {
                val item = catalog.optJSONObject(i) ?: continue
                val id = item.optString("id", "unknown")
                val name = item.optString("name", id)
                val description = item.optString("description", "Vexcord plugin")
                val state = item.optString("status", "planned")
                val implemented = state.equals("implemented", true)
                val card = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dp(14), dp(12), dp(14), dp(10))
                    setBackgroundColor(panel)
                }
                val row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }
                val labels = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
                }
                labels.addView(label(name, 15, Color.WHITE, true))
                labels.addView(label(description, 12, Color.LTGRAY))
                row.addView(labels)
                val toggle = Switch(this).apply {
                    isChecked = prefs.getBoolean(id, false)
                    isEnabled = implemented
                    buttonTintList = null
                    setOnCheckedChangeListener { _, checked ->
                        prefs.edit().putBoolean(id, checked).apply()
                    }
                }
                row.addView(toggle)
                card.addView(row)
                card.addView(label(if (implemented) "UTILITY MODULE • LOCAL PREFERENCE ONLY" else "PLANNED • NOT IMPLEMENTED", 10,
                    if (implemented) blue else Color.rgb(180, 150, 90), true))
                content.addView(card, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(8) })
            }
        }
        content.addView(space(8))
        content.addView(section("IMPORTANT"))
        content.addView(cardText("Independent Vexcord app", "This manager is built from this repository and does not use Revenge Manager. Current switches save preferences only; they do not modify Discord or inject plugins."))
        content.addView(space(12))
        content.addView(label("Vexcord 0.1.0 • Unofficial project", 11, Color.GRAY))
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    @Deprecated("Legacy activity result API retained for compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != 410 || resultCode != RESULT_OK) return
        val uri = data?.data ?: return
        val validArchive = try {
            contentResolver.openInputStream(uri)?.use { input ->
                val header = ByteArray(4)
                input.read(header) == 4 && header[0] == 0x50.toByte() && header[1] == 0x4b.toByte()
            } ?: false
        } catch (_: Exception) { false }
        if (!validArchive) {
            selectedApk = null
            selectedLabel.text = "This file does not look like an APK archive."
            installButton.isEnabled = false
            return
        }
        selectedApk = uri
        val filename = try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val column = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && column >= 0) cursor.getString(column) else null
            }
        } catch (_: Exception) { null }
        selectedLabel.text = "Selected: ${filename ?: "APK file"}"
        installButton.isEnabled = true
    }

    private fun installSelectedApk() {
        val uri = selectedApk ?: return
        try {
            startActivity(Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
                data = uri
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open Android installer: ${e.message ?: "unknown error"}", Toast.LENGTH_LONG).show()
        }
    }

    private fun label(textValue: String, size: Int, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = textValue
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
            setPadding(0, dp(3), 0, dp(3))
        }

    private fun section(textValue: String) = label(textValue, 12, blue, true)
    private fun space(height: Int) = View(this).apply { layoutParams = LinearLayout.LayoutParams(1, dp(height)) }

    private fun cardText(heading: String, body: String): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(12), dp(14), dp(12))
            setBackgroundColor(panel)
        }
        box.addView(label(heading, 15, Color.WHITE, true))
        box.addView(label(body, 12, Color.LTGRAY))
        return box
    }
}
