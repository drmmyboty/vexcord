import * as amoledTheme from "./amoled-theme.js";
import * as themeManager from "./theme-manager.js";
import * as quickReplies from "./quick-replies.js";
import * as messageBookmarks from "./message-bookmarks.js";
import * as customTimestamps from "./custom-timestamps.js";
import * as markdownPreview from "./markdown-preview.js";
import * as pluginBackup from "./plugin-backup.js";
import * as pluginErrorConsole from "./plugin-error-console.js";

export const plugins = new Map([
  [amoledTheme.manifest.id, amoledTheme],
  [themeManager.manifest.id, themeManager],
  [quickReplies.manifest.id, quickReplies],
  [messageBookmarks.manifest.id, messageBookmarks],
  [customTimestamps.manifest.id, customTimestamps],
  [markdownPreview.manifest.id, markdownPreview],
  [pluginBackup.manifest.id, pluginBackup],
  [pluginErrorConsole.manifest.id, pluginErrorConsole]
]);

export function listImplementedPlugins() {
  return [...plugins.values()].map(plugin => ({ ...plugin.manifest }));
}

export function getPlugin(id) {
  return plugins.get(id) ?? null;
}
