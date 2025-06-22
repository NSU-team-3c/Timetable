import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Notification } from '../../../types/notifications/notifications';

interface NotificationsState {
  notifications: Notification[];
  lastChecked: number,
  loading: boolean;
  error: string | null;
  dataUpdated: boolean;
}

const initialState: NotificationsState = {
  notifications: [],
  lastChecked: 0,
  loading: false,
  error: null,
  dataUpdated: false,
};


export const addNotification = createAsyncThunk('notification/addNotification', (notification: Notification) => {
  return notification;
});

export const clearNotifications = createAsyncThunk('notification/clearNotification', (notification: Notification) => {
  return true;
});

export const updateChecked = createAsyncThunk('notification/updateCheckedNotification', (val: number) => {
  return val;
});

const groupSlice = createSlice({
  name: 'groups',
  initialState,
  reducers: {
    setLoading(state, action: PayloadAction<boolean>) {
      state.loading = action.payload;
    },
    setError(state, action: PayloadAction<string | null>) {
      state.error = action.payload;
    },
    resetGroupUpdatedFlag(state) {
      state.dataUpdated = false; 
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(addNotification.fulfilled, (state, action: PayloadAction<Notification>) => {
        state.loading = false;
        state.notifications.push(action.payload);
        state.dataUpdated = false;
      })
      .addCase(updateChecked.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        state.lastChecked = action.payload
        state.dataUpdated = false;
      })
      .addCase(clearNotifications.fulfilled, (state, action: PayloadAction<boolean>) => {
        state.loading = false;
        state.notifications = [];
        state.lastChecked = 0;
        state.dataUpdated = false;
      })
  },
});

export const { setLoading, setError, resetGroupUpdatedFlag } = groupSlice.actions;

export default groupSlice.reducer;
