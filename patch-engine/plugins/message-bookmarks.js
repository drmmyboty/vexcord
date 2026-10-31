export const manifest = { id: "message-bookmarks", version: "0.1.0", permissions: ["local-storage"] };
export function createBookmarks(initial = []) {
  const items = new Map(initial.map(item => [item.id, { ...item }]));
  return {
    list: () => [...items.values()].map(item => ({ ...item })),
    add: ({ messageId, channelId, content, note = "" }) => {
      if (!messageId || !channelId) throw new Error("messageId and channelId are required.");
      const id = channelId + ":" + messageId;
      const item = { id, messageId, channelId, content: String(content ?? ""), note: String(note), savedAt: new Date().toISOString() };
      items.set(id, item); return { ...item };
    },
    remove: id => items.delete(id),
    export: () => JSON.stringify([...items.values()], null, 2)
  };
}
