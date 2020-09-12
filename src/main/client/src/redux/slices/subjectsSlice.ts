import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";

interface Subject {
  name: string,
}

interface SubjectState extends Subject {
  id: string,
  slug: string,
  examIds: string[],
}

const subjectsAdapter = createEntityAdapter<SubjectState>();

const initialState = subjectsAdapter.getInitialState({
  status: "idle",
} as State);

export const fetchSubjects = createAsyncThunk("subjects/fetchSubjects", async () => {
  const res = await fetch("api/subjects", {
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<SubjectState[]>;
});

export const addSubject = createAsyncThunk("subjects/addSubject", async (subject: Subject) => {
  const res = await fetch("/api/subjects", {
    method: "post",
    body: JSON.stringify(subject),
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<SubjectState>;
});

export const subjectsSlice = createSlice({
  name: "subjects",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(fetchSubjects.pending, state => {
      state.status = "loading";
    });
    builder.addCase(fetchSubjects.fulfilled, (state, action) => {
      state.status = "finished";
      subjectsAdapter.upsertMany(state, action.payload);
    });
    builder.addCase(fetchSubjects.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addSubject.fulfilled, (state, action) => {
      subjectsAdapter.addOne(state, action.payload);
    });
  },
});

export const selectSubjectsStatus = (state: RootState): Status => state.subjects.status;
export const {
  selectAll: selectAllSubjects,
  selectById: selectSubjectById,
  selectIds: selectSubjectIds,
} = subjectsAdapter.getSelectors();

export default subjectsSlice.reducer;
