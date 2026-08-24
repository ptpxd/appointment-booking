import { request } from "./httpClient";

export interface ProviderCredentials { email: string; password: string }
export interface ProviderRegistration extends ProviderCredentials { displayName: string }

export const authApi = {
  register: (body: ProviderRegistration) => request<void>("/auth/register", { method: "POST", body: JSON.stringify(body) }),
  login: (body: ProviderCredentials) => request<void>("/auth/login", { method: "POST", body: JSON.stringify(body) }),
  logout: () => request<void>("/auth/logout", { method: "POST" }),
};