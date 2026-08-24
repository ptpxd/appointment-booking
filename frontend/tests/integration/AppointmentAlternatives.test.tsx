import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { AppointmentAlternatives } from "../../src/features/booking/AppointmentAlternatives";

describe("AppointmentAlternatives", () => {
  it("lets a keyboard-accessible button choose an alternative", () => {
    const select = vi.fn();
    render(<AppointmentAlternatives onSelect={select} slots={[{ id: "one", startsAt: "2026-09-01T08:00:00Z", endsAt: "2026-09-01T08:30:00Z" }]} />);
    fireEvent.click(screen.getByRole("button"));
    expect(select).toHaveBeenCalledWith(expect.objectContaining({ id: "one" }));
  });
});