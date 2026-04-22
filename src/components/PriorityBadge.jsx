export default function PriorityBadge({ priority }) {
  const normalized = (priority || "").toUpperCase();
  const colorMap = {
    LOW: "#6b7280",
    MEDIUM: "#2563eb",
    HIGH: "#d97706",
    URGENT: "#dc2626",
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
