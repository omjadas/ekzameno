import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { State } from "../state";

interface Subject{
  name: string,
}

interface SubjectState extends Subject {
  id: string,
  slug: string,
}

const initialState: State<SubjectState[]> = {
  data: [],
  status: "idle",
};

export const fetchSubjects = createAsyncThunk("subjects/fetchSubjects", async () => {
  const res = await fetch("api/users", {
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<SubjectState[]>;
});

export const addSubject = createAsyncThunk("subjects/addSubject", async (subject: Subject) => {
  const res = await fetch("/api/users", {
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
      state.data = action.payload;
    });
    builder.addCase(fetchSubjects.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addSubject.fulfilled, (state, action) => {
      state.data.push(action.payload);
    });
  },
});

export default subjectsSlice.reducer;
