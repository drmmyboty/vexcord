import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

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
  return catalog.plugins.find((plugin) => plugin.id === pluginId);
}

try {
  const state = readState();

  if (command === "list") {
    console.log("Vexcord Plugin Catalog");
    for (const plugin of catalog.plugins) {
      const enabled = state.enabled.includes(plugin.id);
      console.log(`[${enabled ? "ON" : "OFF"}] ${plugin.name} (${plugin.id}) — ${plugin.status}`);
      console.log(`      ${plugin.description}`);
    }
  } else if (command === "validate") {
    const ids = new Set();
    for (const plugin of catalog.plugins) {
      if (!/^[a-z0-9][a-z0-9._-]{1,63}$/.test(plugin.id)) throw new Error(`Invalid plugin id: ${plugin.id}`);
      if (ids.has(plugin.id)) throw new Error(`Duplicate plugin id: ${plugin.id}`);
      ids.add(plugin.id);
      if (!Array.isArray(plugin.permissions)) throw new Error(`Missing permissions array: ${plugin.id}`);
    }
    console.log(`Catalog valid: ${catalog.plugins.length} entries. Catalog entries are metadata only, not installed runtime plugins.`);
  } else if (command === "enable" || command === "disable") {
    if (!id) throw new Error(`Usage: node index.js ${command} <plugin-id>`);
    const plugin = findPlugin(id);
    if (!plugin) throw new Error(`Unknown plugin id: ${id}`);
    if (plugin.status !== "implemented") {
      throw new Error(`${plugin.name} is currently ${plugin.status}; it cannot be enabled as a working plugin yet.`);
    }
    const enabled = new Set(state.enabled);
    if (command === "enable") enabled.add(id);
    else enabled.delete(id);
    state.enabled = [...enabled].sort();
    writeState(state);
    console.log(`${plugin.name}: ${command}d in local plugin state.`);
  } else {
    throw new Error("Commands: list, validate, enable <plugin-id>, disable <plugin-id>");
  }
} catch (error) {
  console.error(`Vexcord Patch Engine: ${error.message}`);
  process.exitCode = 1;
}