export type Status = "idle" | "loading" | "finished" | "error";

export interface State<T> {
  data: T,
  status: Status,
  error?: string,
}
