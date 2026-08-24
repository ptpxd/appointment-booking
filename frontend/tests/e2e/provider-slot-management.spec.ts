import { expect, test } from "@playwright/test";

test("provider dashboard requires an authenticated session", async ({ page }) => {
  await page.goto("/provider/dashboard");
  await expect(page).toHaveURL(/\/provider\/login/);
});