import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

interface Professor {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string; 
}

interface professorState {
  professors: Professor[];
  loading: boolean;
  error: string | null;
}

const initialState: professorState = {
  professors: [],
  loading: false,
  error: null,
};

export const fetchProfessors = createAsyncThunk('professors/fetchProfessors', async () => {
  const response = await axios.get('/api/professors');
  return response.data.map((professor: Professor) => {
    const { password, ...professorWithoutPassword } = professor;
    return professorWithoutPassword; 
  });
});

export const createProfessor = createAsyncThunk('professors/createProfessor', async (professor: Professor) => {
  const response = await axios.post('/api/professors', professor);
  return response.data;
});

export const updateProfessor = createAsyncThunk('professors/updateProfessor', async (professor: Professor) => {
  const response = await axios.put(`/api/professors/${professor.id}`, professor);
  return response.data;
});

export const deleteProfessor = createAsyncThunk('professors/deleteProfessor', async (id: number) => {
  await axios.delete(`/api/professors/${id}`);
  return id;
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
      })
      .addCase(deleteProfessor.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete professor';
      });
  },
});

export const { setLoading, setError } = professorSlice.actions;

export default professorSlice.reducer;
