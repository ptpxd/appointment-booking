import { expect, test } from "@playwright/test";

test("booking page exposes a loading state while slots are requested", async ({ page }) => {
  await page.route("**/api/public/providers/example/slots?date=2026-09-01", async (route) => {
    await new Promise((resolve) => setTimeout(resolve, 500));
    await route.fulfill({
      contentType: "application/json",
      body: JSON.stringify({ provider: { id: "example", displayName: "Teszt szolgaltato" }, slots: [] }),
    });
  });

  await page.goto("/book/example?date=2026-09-01");
  await expect(page.getByRole("status")).toContainText("betoltese");
});