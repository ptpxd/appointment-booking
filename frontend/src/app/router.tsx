import { createBrowserRouter, Navigate } from "react-router-dom";
import { BookingConfirmationPage } from "../pages/BookingConfirmationPage";
import { BookingPage } from "../pages/BookingPage";
import { ProviderDashboardPage } from "../pages/ProviderDashboardPage";
import { ProviderLoginPage } from "../pages/ProviderLoginPage";
import { ProviderRegistrationPage } from "../pages/ProviderRegistrationPage";
import { RequireProviderSession } from "./RequireProviderSession";

export const router = createBrowserRouter([
  { path: "/", element: <Navigate to="/provider/login" replace /> },
  { path: "/book/:providerId", element: <BookingPage /> },
  { path: "/booking/confirm", element: <BookingConfirmationPage /> },
  { path: "/provider/register", element: <ProviderRegistrationPage /> },
  { path: "/provider/login", element: <ProviderLoginPage /> },
  { element: <RequireProviderSession />, children: [{ path: "/provider/dashboard", element: <ProviderDashboardPage /> }] },
]);