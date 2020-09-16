import { Action, AnyAction, combineReducers, configureStore, ThunkAction } from "@reduxjs/toolkit";
import { useDispatch } from "react-redux";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage";
import examsReducer from "./slices/examsSlice";
import subjectsReducer from "./slices/subjectsSlice";
import usersReducer from "./slices/usersSlice";

const reducers = combineReducers({
  users: usersReducer,
  exams: examsReducer,
  subjects: subjectsReducer,
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

// eslint-disable-next-line @typescript-eslint/explicit-function-return-type
export const useAppDispatch = () => useDispatch<typeof store.dispatch>();

/* eslint-disable @typescript-eslint/indent */
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
/* eslint-enable @typescript-eslint/indent */
