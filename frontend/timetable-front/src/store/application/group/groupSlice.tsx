import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';

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
}

const initialState: GroupState = {
  groups: [],
  loading: false,
  error: null,
};

export const fetchGroups = createAsyncThunk('groups/fetchGroups', async () => {
  const response = await axios.get('/api/groups');
  return response.data;
});

export const createGroup = createAsyncThunk('groups/createGroup', async (group: Group) => {
  const response = await axios.post('/api/groups', group);
  return response.data;
});

export const updateGroup = createAsyncThunk('groups/updateGroup', async (group: Group) => {
  const response = await axios.put(`/api/groups/${group.id}`, group);
  return response.data;
});

export const deleteGroup = createAsyncThunk('groups/deleteGroup', async (id: number) => {
  await axios.delete(`/api/groups/${id}`);
  return id; 
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
      })
      .addCase(deleteGroup.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to delete group';
      });
  },
});

export const { setLoading, setError } = groupSlice.actions;

export default groupSlice.reducer;
