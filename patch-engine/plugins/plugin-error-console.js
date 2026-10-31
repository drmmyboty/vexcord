export const manifest = { id: "plugin-error-console", version: "0.1.0", permissions: ["local-storage"] };
export function createErrorConsole(limit = 100) {
  const entries = [];
  return {
    log(error, pluginId = "unknown") {
      entries.push({ pluginId, message: String(error?.message ?? error), time: new Date().toISOString() });
      while (entries.length > limit) entries.shift();
    },
    list: () => entries.map(entry => ({ ...entry })),
    clear: () => { entries.length = 0; },
    export: () => JSON.stringify(entries, null, 2)
  };
}
