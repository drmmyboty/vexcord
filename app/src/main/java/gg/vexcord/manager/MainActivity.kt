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
import org.json.JSONObject

class MainActivity : Activity() {
    private val bg = Color.rgb(7, 10, 16)
    private val panel = Color.rgb(18, 24, 35)
    private val blue = Color.rgb(55, 150, 255)
    private val prefs by lazy { getSharedPreferences("vexcord_plugins", MODE_PRIVATE) }

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
