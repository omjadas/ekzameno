import { Action, configureStore, ThunkAction } from "@reduxjs/toolkit";
import examsReducer from "./slices/examsSlice";
import subjectsReducer from "./slices/subjectsSlice";
import usersReducer from "./slices/usersSlice";

export const store = configureStore({
  reducer: {
    users: usersReducer,
    exams: examsReducer,
    subjects: subjectsReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;

/* eslint-disable @typescript-eslint/indent */
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
/* eslint-enable @typescript-eslint/indent */
