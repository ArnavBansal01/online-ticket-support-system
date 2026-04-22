export function toMinutesLabel(hours) {
  if (!hours) return "0h";
  return `${Math.round(hours)}h`;
}
