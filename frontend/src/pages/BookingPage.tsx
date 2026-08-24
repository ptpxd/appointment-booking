import { useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import { ApiError } from "../api/httpClient";
import { publicBookingApi, type ProviderSlotsResponse, type Slot } from "../api/publicBookingApi";
import { BookingForm } from "../features/booking/BookingForm";
import { SlotPicker } from "../features/booking/SlotPicker";

const today = new Date().toISOString().slice(0, 10);
export function BookingPage() {
  const { providerId = "" } = useParams(); const [searchParams, setSearchParams] = useSearchParams(); const date = searchParams.get("date") ?? today; const [data, setData] = useState<ProviderSlotsResponse>(); const [selected, setSelected] = useState<Slot>(); const [error, setError] = useState("");
  useEffect(() => { let active = true; publicBookingApi.getSlots(providerId, date).then((response) => active && setData(response)).catch((reason) => active && setError(reason instanceof ApiError ? reason.message : "Az idopontok nem tolthetők be.")); return () => { active = false; }; }, [providerId, date]);
  return <main className="app-shell"><header><a href="/">Idopontfoglalas</a><a href="/provider/login">Szolgaltatoi belepes</a></header><h1>{data?.provider.displayName ?? "Idopontfoglalas"}</h1><label>Nap<input type="date" value={date} min={today} onChange={(event) => setSearchParams({ date: event.target.value })} /></label>{error ? <p role="alert">{error}</p> : !data ? <p role="status">Idopontok betoltese...</p> : <SlotPicker slots={data.slots} selectedId={selected?.id} onSelect={setSelected} />}{selected && <BookingForm slot={selected} date={date} onAlternative={setSelected} />}</main>;
}