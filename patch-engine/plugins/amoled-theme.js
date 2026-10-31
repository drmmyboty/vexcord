export const manifest = { id: "amoled-theme", version: "0.1.0", permissions: ["theme"] };
export const theme = { name: "AMOLED Black", colors: { background: "#000000", surface: "#080808", surfaceAlt: "#111111", text: "#f5f5f5", muted: "#a1a1aa", accent: "#5865f2", border: "#242424" } };
export function applyTheme(adapter) {
  if (!adapter || typeof adapter.setTheme !== "function") throw new TypeError("A compatible setTheme adapter is required.");
  adapter.setTheme(theme);
  return theme;
}
