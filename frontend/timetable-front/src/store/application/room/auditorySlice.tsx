import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import axiosInstance from '../../../utils/axios';

interface Auditory {
  id: number;
  name: string;
  type: string;
  capacity: number;
}

interface AuditoryState {
  auditories: Auditory[];
  loading: boolean;
  error: string | null;
}

const initialState: AuditoryState = {
  auditories: [],
  loading: false,
  error: null,
};

export const fetchAuditories = createAsyncThunk('rooms/fetchAuditories', async () => {
  const response = await axiosInstance.get('/api/v1/rooms');
  return response.data;
});

export const createAuditory = createAsyncThunk('rooms/createAuditory', async (auditory: Auditory) => {
  const response = await axiosInstance.post('/api/v1/rooms', auditory);
  return response.data;
});

export const updateAuditory = createAsyncThunk('rooms/updateAuditory', async (auditory: Auditory) => {
  const response = await axiosInstance.put(`/api/v1/rooms/${auditory.id}`, auditory);
  return response.data;
});

export const deleteAuditory = createAsyncThunk('rooms/deleteAuditory', async (id: number) => {
  await axiosInstance.delete(`/api/v1/rooms/${id}`);
  return id;
});

const auditorySlice = createSlice({
  name: 'auditories',
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
      .addCase(fetchAuditories.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAuditories.fulfilled, (state, action: PayloadAction<Auditory[]>) => {
        state.loading = false;
        state.auditories = action.payload;
      })
      .addCase(fetchAuditories.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch auditories';
      })
      .addCase(createAuditory.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createAuditory.fulfilled, (state, action: PayloadAction<Auditory>) => {
        state.loading = false;
        state.auditories.push(action.payload);
      })
      .addCase(createAuditory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create auditory';
      })
      .addCase(updateAuditory.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateAuditory.fulfilled, (state, action: PayloadAction<Auditory>) => {
        state.loading = false;
        const index = state.auditories.findIndex((auditory) => auditory.id === action.payload.id);
        if (index !== -1) {
          state.auditories[index] = action.payload;
        }
      })
      .addCase(updateAuditory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update auditory';
      })
      .addCase(deleteAuditory.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteAuditory.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        state.auditories = state.auditories.filter((auditory) => auditory.id !== action.payload); // Удаляем аудиторию по ID
      })
      .addCase(deleteAuditory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete auditory';
      });
  },
});

export const { setLoading, setError } = auditorySlice.actions;

export default auditorySlice.reducer;
