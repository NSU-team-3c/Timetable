import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import axiosInstance from '../../../utils/axios';

interface Professor {
  id: number;
  name: string;
  surname: string;
  email: string;
  password?: string; 
}

interface professorState {
  professors: Professor[];
  loading: boolean;
  error: string | null;
  dataUpdated: boolean;
}

const initialState: professorState = {
  professors: [],
  loading: false,
  error: null,
  dataUpdated: false,
};

export const fetchProfessors = createAsyncThunk('professors/fetchProfessors', async () => {
  const response = await axiosInstance.get('/api/v1/users/teachers');
  return response.data;
});

export const createProfessor = createAsyncThunk('professors/createProfessor', async (professor: Professor) => {
  const response = await axiosInstance.post('/api/admin/register_teacher', professor);
  return response.data;
});

export const updateProfessor = createAsyncThunk('professors/updateProfessor', async (professor: Professor) => {
  const response = await axiosInstance.put(`/api/professors/${professor.id}`, professor);
  return response.data;
});

export const deleteProfessor = createAsyncThunk('professors/deleteProfessor', async (id: number) => {
  await axiosInstance.delete(`/api/professors/${id}`);
  return id;
});

export const setProfessorUpdateFlag = createAsyncThunk('professors/setProfessorUpdateFlag', () => {
  return true; 
});

const professorSlice = createSlice({
  name: 'professors',
  initialState,
  reducers: {
    setLoading(state, action: PayloadAction<boolean>) {
      state.loading = action.payload;
    },
    setError(state, action: PayloadAction<string | null>) {
      state.error = action.payload;
    },
    resetProfessorUpdatedFlag(state) {
      state.dataUpdated = false; 
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProfessors.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProfessors.fulfilled, (state, action: PayloadAction<Professor[]>) => {
        state.loading = false;
        state.professors = action.payload;
        state.dataUpdated = false;
      })
      .addCase(fetchProfessors.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch professors';
      })
      .addCase(createProfessor.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createProfessor.fulfilled, (state, action: PayloadAction<Professor>) => {
        state.loading = false;
        state.professors.push(action.payload);
        state.dataUpdated = false;
      })
      .addCase(createProfessor.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create professor';
      })
      .addCase(updateProfessor.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateProfessor.fulfilled, (state, action: PayloadAction<Professor>) => {
        state.loading = false;
        const index = state.professors.findIndex((professor) => professor.id === action.payload.id);
        if (index !== -1) {
          state.professors[index] = action.payload;
          state.dataUpdated = false;
        }
      })
      .addCase(updateProfessor.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update professor';
      })
      .addCase(deleteProfessor.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteProfessor.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        state.professors = state.professors.filter((professor) => professor.id !== action.payload);
        state.dataUpdated = false;
      })
      .addCase(deleteProfessor.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete professor';
      })

      .addCase(setProfessorUpdateFlag.fulfilled, (state) => {
        state.dataUpdated = true; 
      });
  },
});

export const { setLoading, setError, resetProfessorUpdatedFlag} = professorSlice.actions;

export default professorSlice.reducer;
