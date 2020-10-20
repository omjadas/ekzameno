import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";
import { QuestionSubmission } from "./questionSubmissionsSlice";

export interface ExamSubmission {
  examId: string,
  studentId: string,
  marks?: number,
}

export interface ExamSubmissionState extends ExamSubmission {
  id: string,
  meta: {
    eTag: string,
  },
}

const examSubmissionsAdapter = createEntityAdapter<ExamSubmissionState>();

const initialState = examSubmissionsAdapter.getInitialState({
  status: "idle",
} as State);

export const fetchExamSubmissions = createAsyncThunk(
  "exams/fetchSubmission",
  async (examId: string) => {
    const res = await fetch(`/api/exams/${examId}/submissions`);
    return res.json() as Promise<ExamSubmissionState[]>;
  }
);

export const createExamSubmission = createAsyncThunk(
  "exams/submitExam",
  async ({
    examId,
    studentId,
    answers,
    marks,
  }: {
    examId: string,
    studentId: string,
    answers: Omit<QuestionSubmission, "examSubmissionId">[],
    marks?: number,
  }) => {
    const res = await fetch(`/api/exams/${examId}/submissions/${studentId}`, {
      method: "post",
      body: JSON.stringify({ marks, answers }),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<ExamSubmissionState>;
  }
);

export const updateExamSubmission = createAsyncThunk(
  "exams/updateSubmission",
  async ({
    examId,
    studentId,
    marks,
    eTag,
  }: {
    examId: string,
    studentId: string,
    marks: number,
    eTag: string,
  }) => {
    const res = await fetch(`/api/exams/${examId}/submissions/${studentId}`, {
      method: "put",
      body: JSON.stringify({ marks }),
      headers: {
        "content-type": "application/json",
        "if-match": eTag,
      },
    });

    return res.json() as Promise<ExamSubmissionState>;
  }
);

export const examSubmissionsSlice = createSlice({
  name: "examSubmissions",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(fetchExamSubmissions.fulfilled, (state, action) => {
      state.status = "finished";
      examSubmissionsAdapter.upsertMany(state, action.payload);
    });
    builder.addCase(createExamSubmission.fulfilled, (state, action) => {
      examSubmissionsAdapter.addOne(state, action.payload);
    });
    builder.addCase(updateExamSubmission.fulfilled, (state, action) => {
      examSubmissionsAdapter.upsertOne(state, action.payload);
    });
  },
});

export const selectQuestionsStatus = (state: RootState): Status => state.options.status;

export const {
  selectAll: selectAllExamSubmissions,
  selectById: selectExamSubmissionById,
  selectIds: selectExamSubmissionIds,
} = examSubmissionsAdapter.getSelectors<RootState>(state => state.examSubmissions);

export const selectExamSubmissionsForExam = (examId?: string) => {
  return (state: RootState): ExamSubmissionState[] => {
    return selectAllExamSubmissions(state)
      .filter(examSubmission => examSubmission.examId === examId);
  };
};

export const selectExamSubmissionsByIds = (ids: string[]) => {
  return (state: RootState): ExamSubmissionState[] => {
    return ids
      .map(id => selectExamSubmissionById(state, id))
      .filter(question => question !== undefined) as ExamSubmissionState[];
  };
};

export default examSubmissionsSlice.reducer;
