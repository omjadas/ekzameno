export interface State<T> {
  data: T,
  status: "idle" | "loading" | "finished" | "error",
  error?: string,
}
