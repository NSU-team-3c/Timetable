// src/redux/slices/profileSlice.ts
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import { ProfileData } from '../../types/user/user';

interface ProfileState {
  profile: ProfileData | null;
  loading: boolean;
  error: string | null;
}

const initialState: ProfileState = {
  profile: null,
  loading: false,
  error: null,
};


export const fetchProfile = createAsyncThunk<ProfileData>('profile/fetchProfile', async () => {
  const response = await axios.get('/api/v1/profile');
  return response.data;
});

export const updateProfile = createAsyncThunk<ProfileData, ProfileData>('profile/updateProfile', async (profileData) => {
  const formData = new FormData();
  Object.keys(profileData).forEach((key) => {
    const typedKey = key as keyof ProfileData;
    if (profileData[typedKey] !== null) {
      formData.append(key, profileData[typedKey] as string | File);
    }
  });
  
  const response = await axios.put('/api/v1/profile', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return response.data;
});



const profileSlice = createSlice({
  name: 'profile',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchProfile.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProfile.fulfilled, (state, action: PayloadAction<ProfileData>) => {
        state.loading = false;
        state.profile = action.payload;
      })
      .addCase(fetchProfile.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to load profile';
      })
      .addCase(updateProfile.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateProfile.fulfilled, (state, action: PayloadAction<ProfileData>) => {
        state.loading = false;
        state.profile = action.payload;
      })
      .addCase(updateProfile.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update profile';
      });
  },
});

export default profileSlice.reducer;
