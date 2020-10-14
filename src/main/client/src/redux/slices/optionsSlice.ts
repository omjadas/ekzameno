import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { State, Status } from "../state";
import { RootState } from "../store";

export interface Option {
  answer: string,
  correct: boolean,
}

export interface OptionState extends Option {
  id: string,
  questionId: string,
  meta: {
    eTag: string,
  },
}

const optionsAdapter = createEntityAdapter<OptionState>();

const initialState = optionsAdapter.getInitialState({
  status: "idle",
} as State);

export const fetchOptions = createAsyncThunk(
  "options/fetchOptions",
  async (questionId: string) => {
    const res = await fetch(`/api/questions/${questionId}/options`, {
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<OptionState[]>;
  }
);

export const addOption = createAsyncThunk(
  "options/addOption",
  async ({ questionId, option }: { questionId: string, option: Option }) => {
    const res = await fetch(`/api/questions/${questionId}/options`, {
      method: "post",
      body: JSON.stringify(option),
      headers: {
        "content-type": "application/json",
      },
    });

    return res.json() as Promise<OptionState>;
  }
);

export const updateOption = createAsyncThunk(
  "options/updateOption",
  async ({
    id,
    option,
    eTag,
  }: {
    id: string,
    option: Option,
    eTag: string,
  }) => {
    const res = await fetch(`/api/options/${id}`, {
      method: "put",
      body: JSON.stringify(option),
      headers: {
        "content-type": "application/json",
        "if-match": eTag,
      },
    });

    return res.json() as Promise<OptionState>;
  }
);

export const deleteOption = createAsyncThunk(
  "options/deleteOption",
  async (optionId: string) => {
    await fetch(`/api/options/${optionId}`, {
      method: "delete",
      headers: {
        "content-type": "application/json",
      },
    });
    return optionId;
  }
);

export const optionsSlice = createSlice({
  name: "options",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(fetchOptions.pending, state => {
      state.status = "loading";
    });
    builder.addCase(fetchOptions.fulfilled, (state, action) => {
      state.status = "finished";
      optionsAdapter.upsertMany(state, action.payload);
    });
    builder.addCase(fetchOptions.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(addOption.fulfilled, (state, action) => {
      optionsAdapter.addOne(state, action.payload);
    });
    builder.addCase(deleteOption.fulfilled, (state, action) => {
      optionsAdapter.removeOne(state, action.payload);
    });
    builder.addCase(updateOption.fulfilled, (state, action) => {
      optionsAdapter.upsertOne(state, action.payload);
    });
  },
});

export const selectQuestionsStatus = (state: RootState): Status => state.options.status;

export const {
  selectAll: selectAllOptions,
  selectById: selectOptionById,
  selectIds: selectOptionIds,
} = optionsAdapter.getSelectors<RootState>(state => state.options);

export const selectOptionsByIds = (ids: string[]) => {
  return (state: RootState): OptionState[] => {
    return ids
      .map(id => selectOptionById(state, id))
      .filter(question => question !== undefined) as OptionState[];
  };
};

export default optionsSlice.reducer;
