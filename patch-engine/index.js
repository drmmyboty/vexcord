import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { listImplementedPlugins } from "./plugins/index.js";

const root = path.dirname(fileURLToPath(import.meta.url));
const catalogPath = path.join(root, "catalog.json");
const statePath = path.join(root, "plugin-state.json");
const catalog = JSON.parse(fs.readFileSync(catalogPath, "utf8"));
const command = process.argv[2] ?? "list";
const id = process.argv[3];

function readState() {
  if (!fs.existsSync(statePath)) return { schemaVersion: 1, enabled: [] };
  const value = JSON.parse(fs.readFileSync(statePath, "utf8"));
  if (!Array.isArray(value.enabled)) throw new Error("Invalid plugin-state.json: enabled must be an array.");
  return value;
}
function writeState(state) {
  fs.writeFileSync(statePath, JSON.stringify(state, null, 2) + "\n", "utf8");
}
function findPlugin(pluginId) {
  return catalog.plugins.find(plugin => plugin.id === pluginId);
}
const runtimeIds = new Set(listImplementedPlugins().map(plugin => plugin.id));

try {
  const state = readState();
  if (command === "list") {
    console.log("Vexcord Plugin Catalog");
    for (const plugin of catalog.plugins) {
      const enabled = state.enabled.includes(plugin.id);
      const runtime = runtimeIds.has(plugin.id) ? "module available" : "not implemented";
      console.log(`[${enabled ? "ON" : "OFF"}] ${plugin.name} (${plugin.id}) — ${plugin.status}; ${runtime}`);
      console.log(`      ${plugin.description}`);
    }
    console.log(`\n${runtimeIds.size} standalone utility modules are available. Discord Android integration is not yet connected.`);
  } else if (command === "validate") {
    const ids = new Set();
    for (const plugin of catalog.plugins) {
      if (!/^[a-z0-9][a-z0-9._-]{1,63}$/.test(plugin.id)) throw new Error(`Invalid plugin id: ${plugin.id}`);
      if (ids.has(plugin.id)) throw new Error(`Duplicate plugin id: ${plugin.id}`);
      ids.add(plugin.id);
      if (!Array.isArray(plugin.permissions)) throw new Error(`Missing permissions array: ${plugin.id}`);
      if (plugin.status === "implemented" && !runtimeIds.has(plugin.id)) throw new Error(`Catalog says implemented but module is missing: ${plugin.id}`);
    }
    for (const runtimeId of runtimeIds) {
      const item = findPlugin(runtimeId);
      if (!item || item.status !== "implemented") throw new Error(`Runtime module missing implemented catalog entry: ${runtimeId}`);
    }
    console.log(`Catalog valid: ${catalog.plugins.length} entries; ${runtimeIds.size} utility modules registered.`);
  } else if (command === "enable" || command === "disable") {
    if (!id) throw new Error(`Usage: node index.js ${command} <plugin-id>`);
    const plugin = findPlugin(id);
    if (!plugin) throw new Error(`Unknown plugin id: ${id}`);
    if (!runtimeIds.has(id)) throw new Error(`${plugin.name} is catalog-only and has no runtime module yet.`);
    const enabled = new Set(state.enabled);
    if (command === "enable") enabled.add(id);
    else enabled.delete(id);
    state.enabled = [...enabled].sort();
    writeState(state);
    console.log(`${plugin.name}: ${command}d in local plugin state. This does not inject it into Discord.`);
  } else {
    throw new Error("Commands: list, validate, enable <plugin-id>, disable <plugin-id>");
  }
} catch (error) {
  console.error(`Vexcord Patch Engine: ${error.message}`);
  process.exitCode = 1;
}
