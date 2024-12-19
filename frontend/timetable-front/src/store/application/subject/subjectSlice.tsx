import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

interface Subject {
    id: number,
    name: string,
    code: string,
    description: string,
    duration: number,
    audienceType: string,
    teacherIds: number[],
    groupIds: number[]
  }

interface subjectState {
  subjects: Subject[];
  loading: boolean;
  error: string | null;
}

const initialState: subjectState = {
  subjects: [],
  loading: false,
  error: null,
};

export const fetchSubjects = createAsyncThunk('subjects/fetchSubjects', async () => {
  const response = await axios.get('/api/subjects');
  return response.data;
});

export const createSubject = createAsyncThunk('subjects/createSubject', async (subject: Subject) => {
  const response = await axios.post('/api/subjects', subject);
  return response.data;
});

export const updateSubject = createAsyncThunk('subjects/updateSubject', async (subject: Subject) => {
  const response = await axios.put(`/api/v/${subject.id}`, subject);
  return response.data;
});

export const deleteSubject = createAsyncThunk('subjects/deleteSubject', async (id: number) => {
  await axios.delete(`/api/subjects/${id}`);
  return id;
});

const subjectSlice = createSlice({
  name: 'subjects',
  initialState,
  reducers: {
    setLoading(state, action: PayloadAction<boolean>) {
      state.loading = action.payload;
    },
    setError(state, action: PayloadAction<string | null>) {
      state.error = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchSubjects.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchSubjects.fulfilled, (state, action: PayloadAction<Subject[]>) => {
        state.loading = false;
        state.subjects = action.payload;
      })
      .addCase(fetchSubjects.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch subjects';
      })
      .addCase(createSubject.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createSubject.fulfilled, (state, action: PayloadAction<Subject>) => {
        state.loading = false;
        state.subjects.push(action.payload);
      })
      .addCase(createSubject.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create subject';
      })
      .addCase(updateSubject.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateSubject.fulfilled, (state, action: PayloadAction<Subject>) => {
        state.loading = false;
        const index = state.subjects.findIndex((subject) => subject.id === action.payload.id);
        if (index !== -1) {
          state.subjects[index] = action.payload;
        }
      })
      .addCase(updateSubject.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update subject';
      })
      .addCase(deleteSubject.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteSubject.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        state.subjects = state.subjects.filter((subject) => subject.id !== action.payload);
      })
      .addCase(deleteSubject.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete subject';
      });
  },
});

export const { setLoading, setError } = subjectSlice.actions;

export default subjectSlice.reducer;
