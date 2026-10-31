export const manifest = { id: "quick-replies", version: "0.1.0", permissions: ["local-storage"] };
export function createQuickReplies(initial = []) {
  const items = new Map(initial.map(item => [item.id, { ...item }]));
  return {
    list: () => [...items.values()].map(item => ({ ...item })),
    add: (label, text) => {
      if (!label?.trim() || !text?.trim()) throw new Error("Both label and text are required.");
      const item = { id: globalThis.crypto?.randomUUID?.() ?? String(Date.now()) + Math.random().toString(16).slice(2), label: label.trim(), text: text.trim() };
      items.set(item.id, item); return { ...item };
    },
    remove: id => items.delete(id),
    get: id => items.has(id) ? { ...items.get(id) } : null,
    export: () => JSON.stringify([...items.values()], null, 2)
  };
}
