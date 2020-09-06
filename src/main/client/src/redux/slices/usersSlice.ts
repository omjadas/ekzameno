import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
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

const initialState: State<UserState[]> = {
  data: [],
  status: "idle",
};

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
      state.data = action.payload;
    });
    builder.addCase(fetchUsers.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addUser.fulfilled, (state, action) => {
      state.data.push(action.payload);
    });
  },
});

export const selectUsersStatus = (state: RootState): Status => state.users.status;

export default usersSlice.reducer;
