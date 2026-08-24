import { request } from "./httpClient";
import type { Slot } from "./publicBookingApi";

export interface ProviderSlot extends Slot { status: "AVAILABLE" | "PENDING_CONFIRMATION" | "BOOKED" | "UNAVAILABLE" }
export interface PageResponse<T> { content: T[]; totalElements?: number; totalPages?: number }
export interface SlotInput { startsAt: string; endsAt: string }

export const providerSlotsApi = {
  list: (from: string, to: string) => request<PageResponse<ProviderSlot>>(`/provider/slots?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}&page=0&size=100`),
  create: (body: SlotInput) => request<ProviderSlot>("/provider/slots", { method: "POST", body: JSON.stringify(body) }),
  update: (slotId: string, body: SlotInput) => request<ProviderSlot>(`/provider/slots/${slotId}`, { method: "PATCH", body: JSON.stringify(body) }),
  remove: (slotId: string) => request<void>(`/provider/slots/${slotId}`, { method: "DELETE" }),
};