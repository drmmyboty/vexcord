export const manifest = { id: "custom-timestamps", version: "0.1.0", permissions: [] };
export function formatTimestamp(input, mode = "24h", locale = undefined) {
  const date = input instanceof Date ? input : new Date(input);
  if (Number.isNaN(date.getTime())) throw new TypeError("Invalid timestamp.");
  return new Intl.DateTimeFormat(locale, { hour: "2-digit", minute: "2-digit", hour12: mode === "12h" }).format(date);
}
