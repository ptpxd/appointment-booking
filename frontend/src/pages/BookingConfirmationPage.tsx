import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { ApiError } from "../api/httpClient";
import { publicBookingApi, type ConfirmedBooking } from "../api/publicBookingApi";
import { formatSlot } from "../features/booking/formatSlot";

export function BookingConfirmationPage() {
  const [params] = useSearchParams(); const token = params.get("token"); const [result, setResult] = useState<ConfirmedBooking>(); const [requestError, setRequestError] = useState(""); const error = token ? requestError : "Hianyzik a megerositesi token.";
  useEffect(() => { if (token) publicBookingApi.confirm(token).then(setResult).catch((reason) => setRequestError(reason instanceof ApiError ? reason.message : "A megerosites nem sikerult.")); }, [token]);
  return <main className="app-shell"><h1>Foglalas megerositese</h1>{!result && !error && <p role="status">Megerostes folyamatban...</p>}{error && <p role="alert">{error}</p>}{result && <section className="form-panel"><h2>Sikeres foglalas</h2><p>{result.providerName}</p><p>{formatSlot(result.slot)}</p><p>Azonosito: {result.reservationId}</p></section>}</main>;
}