import type { Slot } from "../../api/publicBookingApi";
import { formatSlot } from "./formatSlot";

interface SlotPickerProps { slots: Slot[]; selectedId?: string; onSelect: (slot: Slot) => void }

export function SlotPicker({ slots, selectedId, onSelect }: SlotPickerProps) {
  if (!slots.length) return <p role="status">Erre a napra nincs foglalhato idopont.</p>;
  return <div className="slot-grid" aria-label="Foglalhato idopontok">{slots.map((slot) => <button className={selectedId === slot.id ? "slot selected" : "slot"} type="button" aria-pressed={selectedId === slot.id} key={slot.id} onClick={() => onSelect(slot)}>{formatSlot(slot)}</button>)}</div>;
}