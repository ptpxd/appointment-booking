import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { SlotManagementPanel } from "../../src/features/provider-dashboard/SlotManagementPanel";

describe("SlotManagementPanel", () => {
  it("shows an accessible empty state", () => {
    render(<SlotManagementPanel slots={[]} onCreate={vi.fn()} onUpdate={vi.fn()} onDelete={vi.fn()} />);
    expect(screen.getByRole("status")).toHaveTextContent("Meg nincs rogzitett idosav.");
  });
});