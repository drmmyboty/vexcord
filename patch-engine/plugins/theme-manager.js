export const manifest = { id: "theme-manager", version: "0.1.0", permissions: ["theme", "local-storage"] };
export const presets = {
  "amoled-black": { name: "AMOLED Black", background: "#000000", surface: "#080808", text: "#f5f5f5", accent: "#5865f2" },
  "midnight-blue": { name: "Midnight Blue", background: "#080d18", surface: "#101a2b", text: "#edf3ff", accent: "#4d8dff" },
  "soft-purple": { name: "Soft Purple", background: "#171020", surface: "#241631", text: "#f7edff", accent: "#b78cff" }
};
export function listThemes() { return Object.entries(presets).map(([id, theme]) => ({ id, ...theme })); }
export function applyTheme(id, adapter) {
  const theme = presets[id];
  if (!theme) throw new Error("Unknown theme preset: " + id);
  if (!adapter || typeof adapter.setTheme !== "function") throw new TypeError("A compatible setTheme adapter is required.");
  adapter.setTheme(theme);
  return theme;
}
