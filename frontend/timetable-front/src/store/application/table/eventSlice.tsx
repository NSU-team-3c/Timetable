import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axiosInstance from '../../../utils/axios';

interface MyEvent {
  id: string | number;
  //title: string;
  startTime: Date;
  endTime: Date;
  teacherName: string;
  subjectName: string;
  roomName: string
  //recurrenceInterval?: number;  
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
  const response = await axiosInstance.get('/api/v1/timetables');
  if (response.data.events) {
    console.log(response.data.events)
    return response.data.events
  }
  return [];
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
    setEvents(state, action: PayloadAction<MyEvent[]>) {
      state.events = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchEvents.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchEvents.fulfilled, (state, action: PayloadAction<MyEvent[]>) => {
        state.events = action.payload 
        state.loading = false;
      })
      .addCase(fetchEvents.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch events';  
      })
  },
});

export const { setLoading, setError, setEvents } = eventSlice.actions;

export default eventSlice.reducer;
