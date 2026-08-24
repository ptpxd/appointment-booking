import type { Slot } from "../../api/publicBookingApi";
import { formatSlot } from "./formatSlot";

export function AppointmentAlternatives({ slots, onSelect }: { slots: Slot[]; onSelect: (slot: Slot) => void }) {
  if (!slots.length) return null;
  return <section className="alternatives" aria-labelledby="alternatives-title"><h2 id="alternatives-title">Javasolt idopontok</h2><p role="status">A valasztott idopont mar nem elerheto. Valasszon egy aktualis lehetoseget.</p><div className="slot-grid">{slots.slice(0, 3).map((slot) => <button type="button" className="slot" key={slot.id} onClick={() => onSelect(slot)}>{formatSlot(slot)}</button>)}</div></section>;
}