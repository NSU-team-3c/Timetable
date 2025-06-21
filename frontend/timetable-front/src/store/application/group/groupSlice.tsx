import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import axiosInstance from '../../../utils/axios';

interface Group {
  id: number;
  number: number | string;
  course: number;
  department: string;
  capacity: number;
}

interface GroupState {
  groups: Group[];
  loading: boolean;
  error: string | null;
  dataUpdated: boolean;
}

const initialState: GroupState = {
  groups: [],
  loading: false,
  error: null,
  dataUpdated: false,
};

export const fetchGroups = createAsyncThunk('groups/fetchGroups', async () => {
  const response = await axiosInstance.get('/api/v1/groups');
  return response.data;
});

export const createGroup = createAsyncThunk('groups/createGroup', async (group: Group) => {
  const response = await axiosInstance.post('/api/v1/groups', group);
  return response.data;
});

export const updateGroup = createAsyncThunk('groups/updateGroup', async (group: Group) => {
  const response = await axiosInstance.put(`/api/v1/groups/${group.id}`, group);
  return response.data;
});

export const deleteGroup = createAsyncThunk('groups/deleteGroup', async (id: number) => {
  await axiosInstance.delete(`/api/v1/groups/${id}`);
  return id; 
});

export const setGroupUpdateFlag = createAsyncThunk('groups/setGroupUpdateFlag', () => {
  return true; 
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
      .addCase(fetchGroups.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchGroups.fulfilled, (state, action: PayloadAction<Group[]>) => {
        state.loading = false;
        state.groups = action.payload;
        state.dataUpdated = false;
      })
      .addCase(fetchGroups.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch groups';
      })

      .addCase(createGroup.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createGroup.fulfilled, (state, action: PayloadAction<Group>) => {
        state.loading = false;
        state.groups.push(action.payload); 
        state.dataUpdated = false;
      })
      .addCase(createGroup.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create group';
      })

      .addCase(updateGroup.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateGroup.fulfilled, (state, action: PayloadAction<Group>) => {
        state.loading = false;
        const index = state.groups.findIndex((group) => group.id === action.payload.id);
        if (index !== -1) {
          state.groups[index] = action.payload; 
        }
        state.dataUpdated = false;
      })
      .addCase(updateGroup.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update group';
      })

      // Удаление группы
      .addCase(deleteGroup.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteGroup.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        state.groups = state.groups.filter((group) => group.id !== action.payload); // Удаляем группу по ID
        state.dataUpdated = false;
      })
      .addCase(deleteGroup.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete group';
      })

      .addCase(setGroupUpdateFlag.fulfilled, (state) => {
        state.dataUpdated = true; 
      });
  },
});

export const { setLoading, setError, resetGroupUpdatedFlag } = groupSlice.actions;

export default groupSlice.reducer;
