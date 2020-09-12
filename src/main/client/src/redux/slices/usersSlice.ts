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
}

interface UsersState extends State {
  me?: string,
}

const usersAdapter = createEntityAdapter<UserState>();

const initialState = usersAdapter.getInitialState({
  status: "idle",
} as UsersState);

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

export const signIn = createAsyncThunk(
  "users/signIn",
  async ({ email, password }: {email: string, password: string}) => {
    const res = await fetch("/api/auth/signin", {
      method: "post",
      body: JSON.stringify({
        email,
        password,
      }),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<UserState>;
  }
);

export const signOut = createAsyncThunk(
  "users/signOut",
  () => fetch("/api/auth/signout", {
    method: "post",
  })
);

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
    builder.addCase(signIn.fulfilled, (state, action) => {
      state.me = action.payload.id;
      usersAdapter.addOne(state, action.payload);
    });
    builder.addCase(signOut.fulfilled, state => {
      state.me = undefined;
    });
  },
});

export const selectUsersStatus = (state: RootState): Status => state.users.status;
export const {
  selectAll: selectAllUsers,
  selectById: selectUserById,
  selectIds: selectUserIds,
} = usersAdapter.getSelectors();

export const selectMe = (state: RootState): UserState | undefined => {
  if (state.users.me !== undefined) {
    return selectUserById(state.users, state.users.me);
  }
};

export default usersSlice.reducer;
