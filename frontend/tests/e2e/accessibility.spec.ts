import AxeBuilder from "@axe-core/playwright";
import { expect, test } from "@playwright/test";

test("provider login has no critical accessibility violations", async ({ page }) => {
  await page.goto("/provider/login");
  const report = await new AxeBuilder({ page }).analyze();
  expect(report.violations.filter((violation) => violation.impact === "critical")).toEqual([]);
});