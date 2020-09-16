import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";

interface Exam {
  name: string,
  description: string,
  startTime: string,
  finishTime: string,
}

interface ExamState extends Exam {
  id: string,
  slug: string,
  questionIds: string[],
}

interface ExamsState extends State {
  slugs: Record<string, string>,
}

const examsAdapter = createEntityAdapter<ExamState>();

const initialState = examsAdapter.getInitialState({
  status: "idle",
  slugs: {},
} as ExamsState);

export const fetchExams = createAsyncThunk(
  "exams/fetchExams",
  async (subjectId: string) => {
    const res = await fetch(`/api/subjects/${subjectId}/exams`, {
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

export const updateExam = createAsyncThunk(
  "exams/updateExam",
  async ({ id, exam }: { id: string, exam: Exam }) => {
    const res = await fetch(`/api/exams/${id}`, {
      method: "put",
      body: JSON.stringify(exam),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<ExamState>;
  }
);

export const deleteExam = createAsyncThunk(
  "exams/deleteExam",
  async ({ examId }: { examId: string }) => {
    await fetch(`/api/exams/${examId}`, {
      method: "delete",
      headers: {
        "content-type": "application/json",
      },
    });
    return examId;
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
      examsAdapter.upsertMany(state, action.payload);
      action.payload.forEach(exam => {
        state.slugs[exam.slug] = exam.id;
      });
    });
    builder.addCase(fetchExams.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addExam.fulfilled, (state, action) => {
      examsAdapter.addOne(state, action.payload);
      state.slugs[action.payload.slug] = action.payload.id;
    });
    builder.addCase(deleteExam.fulfilled, (state, action) => {
      examsAdapter.removeOne(state, action.payload);
    });
    builder.addCase(updateExam.fulfilled, (state, action) => {
      examsAdapter.upsertOne(state, action.payload);
    });
  },
});

export const selectExamsStatus = (state: RootState): Status => state.exams.status;

export const {
  selectAll: selectAllExams,
  selectById: selectExamById,
  selectIds: selectExamIds,
} = examsAdapter.getSelectors<RootState>(state => state.exams);

export const selectExamBySlug = (slug: string) => {
  return (state: RootState): ExamState | undefined => {
    return selectExamById(state, state.exams.slugs[slug]);
  };
};

export default examsSlice.reducer;
