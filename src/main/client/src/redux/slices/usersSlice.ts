import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";

interface User {
  name: string,
  email: string,
  type: "student" | "instructor" | "administrator",
}

interface UserState extends User {
  id: string,
  slug: string,
}

const usersAdapter = createEntityAdapter();

const initialState = usersAdapter.getInitialState({
  status: "idle",
} as State);

export const fetchUsers = createAsyncThunk("users/fetchUsers", async () => {
  const res = await fetch("api/users", {
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<UserState[]>;
});

export const addUser = createAsyncThunk("users/addUser", async (user: User) => {
  const res = await fetch("/api/users", {
    method: "post",
    body: JSON.stringify(user),
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<UserState>;
});

export const usersSlice = createSlice({
  name: "users",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(fetchUsers.pending, state => {
      state.status = "loading";
    });
    builder.addCase(fetchUsers.fulfilled, (state, action) => {
      state.status = "finished";
      usersAdapter.upsertMany(state, action.payload);
    });
    builder.addCase(fetchUsers.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addUser.fulfilled, (state, action) => {
      usersAdapter.addOne(state, action.payload);
    });
  },
});

export const selectUsersStatus = (state: RootState): Status => state.users.status;
export const {
  selectAll: selectAllUsers,
  selectById: selectUserById,
  selectIds: selectUserIds,
} = usersAdapter.getSelectors();

export default usersSlice.reducer;
