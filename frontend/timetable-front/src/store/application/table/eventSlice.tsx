import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

interface MyEvent {
  id: string | number;
  title: string;
  start: Date;
  end: Date;
  professor: string;
  subject: string;
  recurrenceInterval?: number;  
}

interface EventState {
  events: MyEvent[];
  loading: boolean;
  error: string | null;
}

const initialState: EventState = {
  events: [],
  loading: false,
  error: null,
};

export const fetchEvents = createAsyncThunk('events/fetchEvents', async () => {
  const response = await axios.get('/api/events');
  return response.data;
});

const eventSlice = createSlice({
  name: 'events',
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
      .addCase(fetchEvents.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchEvents.fulfilled, (state, action: PayloadAction<MyEvent[]>) => {
        state.loading = false;
        state.events = action.payload;  
      })
      .addCase(fetchEvents.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch events';  
      })
  },
});

export const { setLoading, setError } = eventSlice.actions;

export default eventSlice.reducer;
