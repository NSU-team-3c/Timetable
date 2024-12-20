import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { ProfileData } from '../../types/user/user';
import axiosInstance from '../../utils/axios';

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
  const response = await axiosInstance.get('/api/v1/users');
  return response.data;
});

export const updateProfile = createAsyncThunk<ProfileData, ProfileData>('profile/updateProfile', async (profileData) => {  

  profileData.birthday = new Date(profileData.birthday).toISOString();
  const response = await axiosInstance.put('/api/v1/users', profileData);
  return response.data;
});

const profileSlice = createSlice({
  name: 'profile',
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

export const { setLoading, setError } = profileSlice.actions;

export default profileSlice.reducer;
