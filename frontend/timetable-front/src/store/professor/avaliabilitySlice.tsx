// store/timeslotsSlice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axiosInstance from '../../utils/axios';
import { dispatch } from '../Store';

interface TimeSlot {
  id: number;
  startTime: Date;
  endTime: Date;
}

interface TimeslotsState {
  timeslots: TimeSlot[];
  loading: boolean;
  error: string | null;
}

const initialState: TimeslotsState = {
  timeslots: [],
  loading: false,
  error: null,
};

// Получение всех timeslots с сервера
export const fetchTimeslots = createAsyncThunk('timeslots/fetchTimeslots', async () => {
  const response = await axiosInstance.get('/api/v1/timeslots');
  return response.data.map((timeslot: { id: any; startTime: string; endTime: string; }) => {
    return {
      id: timeslot.id,
      startTime: new Date(timeslot.startTime), 
      endTime: new Date(timeslot.endTime),
    };
  });
});

// Обновление события (timeslot) по его ID
export const updateTimeslot = createAsyncThunk(
  'timeslots/updateTimeslot',
  async (timeslot: TimeSlot, { rejectWithValue }) => {
    try {
      const response = await axiosInstance.post(`/api/v1/timeslots`, [{
        startTime: timeslot.startTime.toISOString(),
        endTime: timeslot.endTime.toISOString(),
      }] );
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data || 'Ошибка при обновлении');
    }
  }
);

// Удаление события (timeslot) по его ID
export const deleteTimeslot = createAsyncThunk(
  'timeslots/deleteTimeslot',
  async (id: number, { rejectWithValue }) => {
    try {
      await axiosInstance.delete(`/api/v1/timeslots`, {data: [id]});
      return id; 
    } catch (error: any) {
      return rejectWithValue(error.response?.data || 'Ошибка при удалении');
    }
  }
);

const timeslotsSlice = createSlice({
  name: 'timeslots',
  initialState,
  reducers: {
    addTimeslot: (state, action: PayloadAction<TimeSlot>) => {
      state.timeslots.push(action.payload); 
    },
    removeTimeslot: (state, action: PayloadAction<number>) => {
      state.timeslots = state.timeslots.filter((timeslot) => timeslot.id !== action.payload);
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchTimeslots.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchTimeslots.fulfilled, (state, action) => {
        state.loading = false;
        state.timeslots = action.payload; // Сохраняем полученные события
      })
      .addCase(fetchTimeslots.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Ошибка при получении данных';
      })
      .addCase(updateTimeslot.pending, (state) => {
        state.loading = true;
      })
      .addCase(updateTimeslot.fulfilled, (state, action) => {
        state.loading = false;
        const updatedTimeslot = action.payload;
        state.timeslots = updatedTimeslot;
      })
      .addCase(updateTimeslot.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(deleteTimeslot.pending, (state) => {
        state.loading = true;
      })
      .addCase(deleteTimeslot.fulfilled, (state, action) => {
        state.loading = false;
        const idToDelete = action.payload;
        state.timeslots = state.timeslots.filter((timeslot) => timeslot.id !== idToDelete); // Удаляем событие из состояния
      })
      .addCase(deleteTimeslot.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { addTimeslot, removeTimeslot } = timeslotsSlice.actions;

export default timeslotsSlice.reducer;
