import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";

export type UserType = "STUDENT" | "INSTRUCTOR" | "ADMINISTRATOR";

export interface User {
  name: string,
  email: string,
  type: UserType,
}

export interface CreateUser extends User {
  password: string,
}

export interface UserState extends User {
  id: string,
  meta: {
    eTag: string,
  },
}

interface UsersState extends State {
  me?: string,
}

const usersAdapter = createEntityAdapter<UserState>();

const initialState = usersAdapter.getInitialState({
  status: "idle",
} as UsersState);

export const fetchUsers = createAsyncThunk("users/fetchUsers", async () => {
  const res = await fetch("/api/users", {
    headers: {
      "content-type": "application/json",
    },
  });

  if (!res.ok) {
    throw new Error(res.status.toString());
  }

  return res.json() as Promise<UserState[]>;
});

export const fetchInstructorsForSubject = createAsyncThunk(
  "users/fetchInstructorsForSubject",
  async (subjectId: string) => {
    const res = await fetch(`/api/subjects/${subjectId}/instructors`, {
      headers: {
        "content-type": "application/json",
      },
    });

    if (!res.ok) {
      throw new Error(res.status.toString());
    }

    return { instructors: await res.json() as UserState[], subjectId };
  }
);

export const fetchStudentsForSubject = createAsyncThunk(
  "users/fetchStudentsForSubject",
  async (subjectId: string) => {
    const res = await fetch(`/api/subjects/${subjectId}/students`, {
      headers: {
        "content-type": "application/json",
      },
    });

    if (!res.ok) {
      throw new Error(res.status.toString());
    }

    return { students: await res.json() as UserState[], subjectId };
  }
);

export const addUser = createAsyncThunk("users/addUser", async (user: CreateUser) => {
  const res = await fetch("/api/users", {
    method: "post",
    body: JSON.stringify(user),
    headers: {
      "content-type": "application/json",
    },
  });

  if (!res.ok) {
    throw new Error(res.status.toString());
  }

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

    if (!res.ok) {
      throw new Error(res.status.toString());
    }

    return res.json() as Promise<UserState>;
  }
);

export const signOut = createAsyncThunk(
  "users/signOut",
  async () => {
    const res = await fetch("/api/auth/signout", {
      method: "post",
    });

    if (!res.ok) {
      throw new Error(res.status.toString());
    }
  }
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
    builder.addCase(fetchInstructorsForSubject.fulfilled, (state, action) => {
      usersAdapter.upsertMany(state, action.payload.instructors);
    });
    builder.addCase(fetchStudentsForSubject.fulfilled, (state, action) => {
      usersAdapter.upsertMany(state, action.payload.students);
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
} = usersAdapter.getSelectors<RootState>(state => state.users);

export const selectMe = (state: RootState): UserState | undefined => {
  if (state.users.me !== undefined) {
    return selectUserById(state, state.users.me);
  }
};

export const selectStudents = (state: RootState): UserState[] => {
  return selectAllUsers(state).filter(user => user.type === "STUDENT");
};

export const selectInstructors = (state: RootState): UserState[] => {
  return selectAllUsers(state).filter(user => user.type === "INSTRUCTOR");
};

export const selectAdministrators = (state: RootState): UserState[] => {
  return selectAllUsers(state).filter(user => user.type === "ADMINISTRATOR");
};

export const selectUsersByIds = (ids: string[]) => {
  return (state: RootState): UserState[] => {
    return ids
      .map(id => selectUserById(state, id))
      .filter(user => user !== undefined) as UserState[];
  };
};

export default usersSlice.reducer;
