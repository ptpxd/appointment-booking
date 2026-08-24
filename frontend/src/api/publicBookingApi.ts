import { request } from "./httpClient";

export type TimeOfDay = "MORNING" | "AFTERNOON" | "EVENING";
export interface Slot { id: string; startsAt: string; endsAt: string }
export interface ProviderSlotsResponse { provider: { id: string; displayName: string }; slots: Slot[] }
export interface BookingRequest { slotId: string; guestEmail: string; preference?: { preferredDate?: string; preferredTimeOfDay?: TimeOfDay } }
export interface PendingBooking { reservationId: string; expiresAt: string; message: string }
export interface ConfirmedBooking { reservationId: string; status: "CONFIRMED"; providerName: string; slot: Slot }

export const publicBookingApi = {
  getSlots: (providerId: string, date: string) => request<ProviderSlotsResponse>(`/public/providers/${providerId}/slots?date=${encodeURIComponent(date)}`),
  createBooking: (body: BookingRequest) => request<PendingBooking>("/public/bookings", { method: "POST", body: JSON.stringify(body) }),
  confirm: (token: string) => request<ConfirmedBooking>("/public/bookings/confirm", { method: "POST", body: JSON.stringify({ token }) }),
};