import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { State } from "../state";

interface Exam {
  name: string,
}

interface ExamState extends Exam {
  id: string,
  slug: string,
}

const initialState: State<ExamState[]> = {
  data: [],
  status: "idle",
};

export const fetchExams = createAsyncThunk(
  "exams/fetchExams",
  async (subjectId: string) => {
    const res = await fetch(`api/subjects/${subjectId}/exams`, {
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<ExamState[]>;
  }
);

export const addExam = createAsyncThunk(
  "exams/addExam",
  async ({ subjectId, exam }: { subjectId: string, exam: Exam }) => {
    const res = await fetch(`/api/subjects/${subjectId}/exams`, {
      method: "post",
      body: JSON.stringify(exam),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<ExamState>;
  }
);

export const examsSlice = createSlice({
  name: "exams",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(fetchExams.pending, state => {
      state.status = "loading";
    });
    builder.addCase(fetchExams.fulfilled, (state, action) => {
      state.status = "finished";
      state.data = action.payload;
    });
    builder.addCase(fetchExams.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addExam.fulfilled, (state, action) => {
      state.data.push(action.payload);
    });
  },
});

export default examsSlice.reducer;
