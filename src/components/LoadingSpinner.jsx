export default function LoadingSpinner() {
  return (
    <div
      style={{ display: "flex", justifyContent: "center", padding: "40px 0" }}
    >
      <div
        style={{
          width: 30,
          height: 30,
          border: "4px solid #e5e7eb",
          borderTopColor: "var(--primary)",
          borderRadius: "50%",
          animation: "spin 1s linear infinite",
        }}
      />
    </div>
  );
}
