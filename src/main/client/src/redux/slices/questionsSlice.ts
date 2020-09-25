import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";

export interface Question {
  name: string,
  type: "MULTI_CHOICE" | "SHORT_ANSWER",
}

export interface QuestionState extends Question {
  id: string,
  examId: string,
}

const questionsAdapter = createEntityAdapter<QuestionState>();

const initialState = questionsAdapter.getInitialState({
  status: "idle",
} as State);

export const fetchQuestions = createAsyncThunk(
  "questions/fetchQuestions",
  async (examId: string) => {
    const res = await fetch(`/api/exams/${examId}/questions`, {
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<QuestionState[]>;
  }
);

export const addQuestion = createAsyncThunk(
  "questions/addQuestion",
  async ({ examId, question }: { examId: string, question: Question }) => {
    const res = await fetch(`/api/exams/${examId}/questions`, {
      method: "post",
      body: JSON.stringify(question),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<QuestionState>;
  }
);

export const updateQuestion = createAsyncThunk(
  "questions/updateQuestion",
  async ({ id, question }: { id: string, question: Question }) => {
    const res = await fetch(`/api/questions/${id}`, {
      method: "put",
      body: JSON.stringify(question),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<QuestionState>;
  }
);

export const deleteQuestion = createAsyncThunk(
  "questions/deleteQuestion",
  async ({ questionId }: { questionId: string }) => {
    await fetch(`/api/exams/${questionId}`, {
      method: "delete",
      headers: {
        "content-type": "application/json",
      },
    });
    return questionId;
  }
);

export const questionsSlice = createSlice({
  name: "questions",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(fetchQuestions.pending, state => {
      state.status = "loading";
    });
    builder.addCase(fetchQuestions.fulfilled, (state, action) => {
      state.status = "finished";
      questionsAdapter.upsertMany(state, action.payload);
    });
    builder.addCase(fetchQuestions.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addQuestion.fulfilled, (state, action) => {
      questionsAdapter.addOne(state, action.payload);
    });
    builder.addCase(deleteQuestion.fulfilled, (state, action) => {
      questionsAdapter.removeOne(state, action.payload);
    });
    builder.addCase(updateQuestion.fulfilled, (state, action) => {
      questionsAdapter.upsertOne(state, action.payload);
    });
  },
});

export const selectQuestionsStatus = (state: RootState): Status => state.questions.status;

export const {
  selectAll: selectAllQuestions,
  selectById: selectQuestionById,
  selectIds: selectQuestionIds,
} = questionsAdapter.getSelectors<RootState>(state => state.questions);

export const selectQuestionsByIds = (ids: string[]) => {
  return (state: RootState): QuestionState[] => {
    return ids
      .map(id => selectQuestionById(state, id))
      .filter(question => question !== undefined) as QuestionState[];
  };
};

export default questionsSlice.reducer;
