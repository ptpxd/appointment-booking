import type { Slot } from "../../api/publicBookingApi";

export function formatSlot(slot: Slot) {
  return new Intl.DateTimeFormat("hu-HU", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(slot.startsAt));
}