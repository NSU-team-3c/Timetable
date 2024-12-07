import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { ProfessorFormValues } from '../../types/user/user';
import axios from 'axios';


interface ProfileState {
  professorProfile: ProfessorFormValues | null;
  loading: boolean;
  error: string | null;
}

const initialState: ProfileState = {
  professorProfile: null,
  loading: false,
  error: null,
};


export const fetchProfessorProfile = createAsyncThunk<ProfessorFormValues>(
  'profile/fetchProfessorProfile', 
  async () => {
    const response = await axios.get('/api/v1/profile/professor');
    return response.data;
  }
);

export const updateProfessorProfile = createAsyncThunk<ProfessorFormValues, ProfessorFormValues>(
  'profile/updateProfessorProfile', 
  async (profileData) => {
    const response = await axios.put('/api/v1/profile/professor', profileData);
    return response.data;
  }
);

const profileSlice = createSlice({
  name: 'profile',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchProfessorProfile.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProfessorProfile.fulfilled, (state, action: PayloadAction<ProfessorFormValues>) => {
        state.loading = false;
        state.professorProfile = action.payload;
      })
      .addCase(fetchProfessorProfile.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch professor profile';
      })
      .addCase(updateProfessorProfile.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateProfessorProfile.fulfilled, (state, action: PayloadAction<ProfessorFormValues>) => {
        state.loading = false;
        state.professorProfile = action.payload;
      })
      .addCase(updateProfessorProfile.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update professor profile';
      });
  },
});

export default profileSlice.reducer;
