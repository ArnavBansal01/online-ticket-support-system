export default function TicketStatusBadge({ status }) {
  const normalized = (status || "").toUpperCase();
  const colorMap = {
    OPEN: "#2563eb",
    IN_PROGRESS: "#d97706",
    RESOLVED: "#16a34a",
    CLOSED: "#6b7280",
  };

  return (
    <span
      className="badge"
      style={{
        background: `${colorMap[normalized] || "#6b7280"}22`,
        color: colorMap[normalized] || "#6b7280",
      }}
    >
      {normalized}
    </span>
  );
}
