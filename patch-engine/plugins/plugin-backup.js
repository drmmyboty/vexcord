export const manifest = { id: "plugin-backup", version: "0.1.0", permissions: ["local-storage"] };
export function exportSettings(settings) {
  return JSON.stringify({ format: "vexcord-settings", schemaVersion: 1, exportedAt: new Date().toISOString(), settings }, null, 2);
}
export function importSettings(jsonText) {
  const data = JSON.parse(jsonText);
  if (data?.format !== "vexcord-settings" || data?.schemaVersion !== 1 || typeof data.settings !== "object" || data.settings === null) {
    throw new Error("Not a supported Vexcord settings backup.");
  }
  return data.settings;
}
