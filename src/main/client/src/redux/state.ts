export type Status = "idle" | "loading" | "finished" | "error";

export interface State {
  status: Status,
  error?: string,
}
