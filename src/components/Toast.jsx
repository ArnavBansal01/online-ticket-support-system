export default function Toast({ toast, onClose }) {
  if (!toast) return null;

  return (
    <div
      style={{
        position: "fixed",
        top: 20,
        right: 20,
        background: toast.type === "error" ? "#dc2626" : "#16a34a",
        color: "white",
        padding: "12px 16px",
        borderRadius: 8,
        zIndex: 9999,
        boxShadow: "0 10px 25px rgba(0,0,0,0.15)",
      }}
      onClick={onClose}
    >
      {toast.message}
    </div>
  );
}
