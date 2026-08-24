import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { BookingForm } from "../../src/features/booking/BookingForm";
import { ApiError } from "../../src/api/httpClient";
import { publicBookingApi } from "../../src/api/publicBookingApi";

vi.mock("../../src/api/publicBookingApi", async (importOriginal) => {
  const actual = await importOriginal<typeof import("../../src/api/publicBookingApi")>();
  return { ...actual, publicBookingApi: { ...actual.publicBookingApi, createBooking: vi.fn() } };
});

describe("BookingForm", () => {
  it("announces an unavailable slot and offers alternatives", async () => {
    vi.mocked(publicBookingApi.createBooking).mockRejectedValue(new ApiError(409, "SLOT_UNAVAILABLE", "Betelt", undefined, [{ id: "other", startsAt: "2026-09-01T09:00:00Z", endsAt: "2026-09-01T09:30:00Z" }]));
    render(<BookingForm date="2026-09-01" slot={{ id: "slot", startsAt: "2026-09-01T08:00:00Z", endsAt: "2026-09-01T08:30:00Z" }} onAlternative={() => undefined} />);
    fireEvent.change(screen.getByLabelText("Email cim"), { target: { value: "guest@example.com" } });
    fireEvent.click(screen.getByRole("button", { name: "Foglalas inditasa" }));
    expect(await screen.findByText("Javasolt idopontok")).toBeInTheDocument();
    expect(screen.getByText("Ez az idopont kozben betelt.")).toHaveAttribute("aria-live", "polite");
  });
});