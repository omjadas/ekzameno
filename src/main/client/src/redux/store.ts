import { Action, AnyAction, combineReducers, configureStore, ThunkAction } from "@reduxjs/toolkit";
import { useDispatch } from "react-redux";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage";
import examsReducer from "./slices/examsSlice";
import examSubmissionsReducer from "./slices/examSubmissionsSlice";
import optionsReducer from "./slices/optionsSlice";
import questionsReducer from "./slices/questionsSlice";
import subjectsReducer from "./slices/subjectsSlice";
import usersReducer from "./slices/usersSlice";
import questionSubmissionsReducer from "./slices/questionSubmissionsSlice";

const reducers = combineReducers({
  users: usersReducer,
  exams: examsReducer,
  subjects: subjectsReducer,
  questions: questionsReducer,
  options: optionsReducer,
  examSubmissions: examSubmissionsReducer,
  questionSubmissions: questionSubmissionsReducer,
});

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type
const rootReducer = (state: any, action: AnyAction) => {
  if (action.type === "RESET") {
    state = undefined;
  }

  return reducers(state, action);
};

const persistConfig = {
  key: "root",
  storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);

export type RootState = ReturnType<typeof store.getState>;

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type, @typescript-eslint/explicit-module-boundary-types
export const useAppDispatch = () => useDispatch<typeof store.dispatch>();

/* eslint-disable @typescript-eslint/indent */
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
/* eslint-enable @typescript-eslint/indent */
