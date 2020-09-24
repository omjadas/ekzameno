/* eslint-disable @typescript-eslint/no-non-null-assertion */
import { createAsyncThunk, createEntityAdapter, createSlice } from "@reduxjs/toolkit";
import { Subject } from "../../components/subject/subject";
import { State, Status } from "../state";
import { RootState } from "../store";
import { addExam, fetchExams } from "./examsSlice";
import { fetchInstructorsForSubject, fetchStudentsForSubject } from "./usersSlice";

export interface Subject {
  name: string,
  description: string,
  instructors: string[],
  students: string[],
}

export interface SubjectState extends Subject {
  id: string,
  slug: string,
  examIds: string[],
}

interface SubjectsState extends State {
  slugs: Record<string, string>,
}

const subjectsAdapter = createEntityAdapter<SubjectState>();

const initialState = subjectsAdapter.getInitialState({
  status: "idle",
  slugs: {},
} as SubjectsState);

export const fetchSubjects = createAsyncThunk("subjects/fetchSubjects", async () => {
  const res = await fetch("/api/subjects", {
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<SubjectState[]>;
});

export const fetchSubject = createAsyncThunk("subjects/fetchSubject", async (slug: string) => {
  const res = await fetch(`/api/subjects/${slug}`, {
    headers: {
      "content-type": "application/json",
    },
  });

  return res.json() as Promise<SubjectState>;
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

export const updateSubject = createAsyncThunk(
  "subjects/updateSubject",
  async ({
    id,
    subject,
    deletedInstructors,
    newInstructors,
    deletedStudents,
    newStudents,
  }: {
    id: string,
    subject: Omit<Subject, "instructors" | "students">,
    deletedInstructors: string[],
    newInstructors: string[],
    deletedStudents: string[],
    newStudents: string[],
  }) => {
    const promises = [
      ...deletedInstructors.map(i => fetch(`/api/subjects/${id}/instructors/${i}`, { method: "delete" })),
      ...deletedStudents.map(i => fetch(`/api/subjects/${id}/students/${i}`, { method: "delete" })),
      ...newInstructors.map(i => fetch(`/api/subjects/${id}/instructors/${i}`, { method: "post" })),
      ...newStudents.map(i => fetch(`/api/subjects/${id}/students/${i}`, { method: "post" })),
    ];
    await Promise.all(promises);
    const subjectUpdate = await fetch(`/api/subjects/${id}`, {
      method: "put",
      body: JSON.stringify(subject),
      headers: {
        "content-type": "application/json",
      },
    });

    return {
      subject: await subjectUpdate.json() as SubjectState,
      deletedInstructors,
      newInstructors,
      deletedStudents,
      newStudents,
    };
  }
);

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
      action.payload.forEach(subject => {
        state.slugs[subject.slug] = subject.id;
      });
    });
    builder.addCase(fetchSubjects.rejected, (state, action) => {
      state.status = "error";
      state.error = action.error.message;
    });
    builder.addCase(fetchSubject.fulfilled, (state, action) => {
      subjectsAdapter.upsertOne(state, action.payload);
      state.slugs[action.payload.slug] = action.payload.id;
    });
    builder.addCase(addSubject.fulfilled, (state, action) => {
      subjectsAdapter.addOne(state, action.payload);
      state.slugs[action.payload.slug] = action.payload.id;
    });
    builder.addCase(updateSubject.fulfilled, (state, action) => {
      subjectsAdapter.upsertOne(state, action.payload.subject);
      const subject = state.entities[action.payload.subject.id]!;
      subject.instructors = [
        ...subject.instructors.filter(i => !action.payload.deletedInstructors.includes(i)),
        ...action.payload.newInstructors,
      ];
      subject.students = [
        ...subject.students.filter(s => !action.payload.deletedStudents.includes(s)),
        ...action.payload.newStudents,
      ];
    });
    builder.addCase(fetchExams.fulfilled, (state, action) => {
      if (action.payload.length > 0) {
        const subject = state.entities[action.payload[0].subjectId];
        if (subject !== undefined) {
          subject.examIds = action.payload.map(e => e.id);
        }
      }
    });
    builder.addCase(addExam.fulfilled, (state, action) => {
      state.entities[action.payload.subjectId]?.examIds?.push(action.payload.id);
    });
    builder.addCase(fetchInstructorsForSubject.fulfilled, (state, action) => {
      const subject = state.entities[action.payload.subjectId];
      if (subject !== undefined) {
        subject.instructors = action.payload.instructors.map(i => i.id);
      }
    });
    builder.addCase(fetchStudentsForSubject.fulfilled, (state, action) => {
      const subject = state.entities[action.payload.subjectId];
      if (subject !== undefined) {
        subject.students = action.payload.students.map(s => s.id);
      }
    });
  },
});

export const selectSubjectsStatus = (state: RootState): Status => state.subjects.status;
export const {
  selectAll: selectAllSubjects,
  selectById: selectSubjectById,
  selectIds: selectSubjectIds,
} = subjectsAdapter.getSelectors<RootState>(
  state => state.subjects
);

export const selectSubjectBySlug = (slug: string) => {
  return (state: RootState): SubjectState | undefined => {
    return selectSubjectById(state, state.subjects.slugs[slug]);
  };
};

export default subjectsSlice.reducer;
