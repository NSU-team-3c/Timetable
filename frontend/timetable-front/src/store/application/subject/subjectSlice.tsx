import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import axiosInstance from '../../../utils/axios';

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
  dataUpdated: boolean;
}

const initialState: subjectState = {
  subjects: [],
  loading: false,
  error: null,
  dataUpdated: false,
};

export const fetchSubjects = createAsyncThunk('subjects/fetchSubjects', async () => {
  const response = await axiosInstance.get('/api/v1/subjects');
  return response.data;
});

export const createSubject = createAsyncThunk('subjects/createSubject', async (subject: Subject) => {
  const response = await axiosInstance.post('/api/v1/subjects', subject);
  return response.data;
});

export const updateSubject = createAsyncThunk('subjects/updateSubject', async (subject: Subject) => {
  const response = await axiosInstance.put(`/api/v1/${subject.id}`, subject);
  return response.data;
});

export const deleteSubject = createAsyncThunk('subjects/deleteSubject', async (id: number) => {
  await axiosInstance.delete(`/api/v1/subjects/${id}`);
  return id;
});

export const setSubjectUpdateFlag = createAsyncThunk('subjects/setSubjectUpdateFlag', () => {
  return true; 
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
    resetSubjectUpdatedFlag(state) {
      state.dataUpdated = false; 
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
        state.dataUpdated = false;
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
        state.dataUpdated = false;
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
          state.dataUpdated = false;
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
        state.dataUpdated = false;
      })
      .addCase(deleteSubject.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete subject';
      })

      .addCase(setSubjectUpdateFlag.fulfilled, (state) => {
        state.dataUpdated = true; 
      });
  },
});

export const { setLoading, setError, resetSubjectUpdatedFlag } = subjectSlice.actions;

export default subjectSlice.reducer;
