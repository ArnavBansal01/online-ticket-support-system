import { useMemo, useState } from "react";
import { ToastContext } from "./toastContext";

export function ToastProvider({ children }) {
  const [toast, setToast] = useState(null);

  const showToast = (message, type = "success") => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3000);
  };

  const value = useMemo(
    () => ({ toast, showToast, clearToast: () => setToast(null) }),
    [toast],
  );

  return (
    <ToastContext.Provider value={value}>{children}</ToastContext.Provider>
  );
}
