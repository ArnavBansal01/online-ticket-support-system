import { useCallback, useState } from "react";
import { ticketApi } from "../api/ticketApi";

export function useTickets() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchTickets = useCallback(async (params = {}) => {
    setLoading(true);
    try {
      const { data } = await ticketApi.list(params);
      setTickets(data.items || []);
      return data;
    } finally {
      setLoading(false);
    }
  }, []);

  return { tickets, loading, fetchTickets, setTickets };
}
