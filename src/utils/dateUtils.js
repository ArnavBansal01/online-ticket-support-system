export function formatDateTime(value) {
  if (!value) return "-";
  return new Date(value).toLocaleString();
}

export function formatDate(value) {
  if (!value) return "-";
  return new Date(value).toLocaleDateString();
}
