import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";
import { addOption, fetchOptions } from "./optionsSlice";

export type QuestionType = "MULTIPLE_CHOICE" | "SHORT_ANSWER";

export const questionLabels: Record<QuestionType, string> = {
  MULTIPLE_CHOICE: "Multiple Choice",
  SHORT_ANSWER: "Short Answer",
};

export interface Question {
  question: string,
  type: QuestionType,
  marks: number,
}

export interface CreateQuestion extends Question {
  options: {
    answer: string,
    correct: boolean,
  }[],
}

export interface QuestionState extends Question {
  id: string,
  examId: string,
  optionIds: string[],
  meta: {
    eTag: string,
  },
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
    if (!res.ok) {
      throw new Error(res.status.toString());
    }
    return res.json() as Promise<QuestionState[]>;
  }
);

export const addQuestion = createAsyncThunk(
  "questions/addQuestion",
  async ({ examId, question }: { examId: string, question: CreateQuestion }) => {
    const res = await fetch(`/api/exams/${examId}/questions`, {
      method: "post",
      body: JSON.stringify(question),
      headers: {
        "content-type": "application/json",
      },
    });
    if (!res.ok) {
      throw new Error(res.status.toString());
    }
    return res.json() as Promise<QuestionState>;
  }
);

export const updateQuestion = createAsyncThunk(
  "questions/updateQuestion",
  async ({
    id,
    question,
    eTag,
  }: {
    id: string,
    question: Question,
    eTag: string,
  }) => {
    const res = await fetch(`/api/questions/${id}`, {
      method: "put",
      body: JSON.stringify(question),
      headers: {
        "content-type": "application/json",
        "if-match": eTag,
      },
    });
    if (!res.ok) {
      throw new Error(res.status.toString());
    }
    return res.json() as Promise<QuestionState>;
  }
);

export const deleteQuestion = createAsyncThunk(
  "questions/deleteQuestion",
  async ({ questionId }: { questionId: string }) => {
    await fetch(`/api/questions/${questionId}`, {
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
    builder.addCase(fetchOptions.fulfilled, (state, action) => {
      if (action.payload.length > 0) {
        const question = state.entities[action.payload[0].questionId];

        if (question !== undefined) {
          question.optionIds = action.payload.map(o => o.id);
        }
      }
    });
    builder.addCase(addOption.fulfilled, (state, action) => {
      const question = state.entities[action.payload.questionId];

      if (question !== undefined) {
        question.optionIds === undefined && (question.optionIds = []);
        question.optionIds.push(action.payload.id);
      }
    });
  },
});

export const selectQuestionsStatus = (state: RootState): Status => state.questions.status;

export const {
  selectAll: selectAllQuestions,
  selectById: selectQuestionById,
  selectIds: selectQuestionIds,
} = questionsAdapter.getSelectors<RootState>(state => state.questions);

export const selectQuestionsForExam = (examId: string) => {
  return (state: RootState): QuestionState[] => {
    return selectAllQuestions(state)
      .filter(question => question.examId === examId);
  };
};

export const selectQuestionsByIds = (ids: string[]) => {
  return (state: RootState): QuestionState[] => {
    return ids
      .map(id => selectQuestionById(state, id))
      .filter(question => question !== undefined) as QuestionState[];
  };
};

export default questionsSlice.reducer;
