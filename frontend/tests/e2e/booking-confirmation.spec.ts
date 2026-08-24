import { expect, test } from "@playwright/test";

test("shows a useful error when the confirmation token is absent", async ({ page }) => {
  await page.goto("/booking/confirm");
  await expect(page.getByRole("alert")).toContainText("Hianyzik");
});