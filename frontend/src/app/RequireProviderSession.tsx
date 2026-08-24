import { Navigate, Outlet, useLocation } from "react-router-dom";

export function RequireProviderSession() {
  const location = useLocation();
  return sessionStorage.getItem("providerSession") === "active" ? <Outlet /> : <Navigate to="/provider/login" replace state={{ from: location.pathname }} />;
}