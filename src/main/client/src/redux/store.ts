import { Action, combineReducers, configureStore, ThunkAction } from "@reduxjs/toolkit";
import examsReducer from "./slices/examsSlice";
import subjectsReducer from "./slices/subjectsSlice";
import usersReducer from "./slices/usersSlice";
import { persistReducer, persistStore } from "redux-persist";
import storage from 'redux-persist/lib/storage';

const reducers = combineReducers({
  users: usersReducer,
  exams: examsReducer,
  subjects: subjectsReducer,
});

const persistConfig = {
  key: 'root',
  storage,
};

const persistedReducer = persistReducer(persistConfig, reducers);

export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);

export type RootState = ReturnType<typeof store.getState>;

/* eslint-disable @typescript-eslint/indent */
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
/* eslint-enable @typescript-eslint/indent */
