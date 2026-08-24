const configuredOrigin = import.meta.env.VITE_API_ORIGIN?.replace(/\/$/, "") ?? "";

export class ApiError extends Error {
  constructor(
    public readonly status: number,
    public readonly code: string,
    message: string,
    public readonly fieldErrors?: Record<string, string>,
    public readonly details?: unknown,
  ) {
    super(message);
  }
}

export async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const response = await fetch(`${configuredOrigin}/api${path}`, {
    credentials: "include",
    headers: { "Content-Type": "application/json", ...init.headers },
    ...init,
  });
  if (!response.ok) {
    const payload = (await response.json().catch(() => ({}))) as { code?: string; message?: string; fieldErrors?: Record<string, string>; alternatives?: unknown };
    throw new ApiError(response.status, payload.code ?? "REQUEST_FAILED", payload.message ?? "A keres nem sikerult.", payload.fieldErrors, payload.alternatives);
  }
  if (response.status === 204) return undefined as T;
  return (await response.json()) as T;
}