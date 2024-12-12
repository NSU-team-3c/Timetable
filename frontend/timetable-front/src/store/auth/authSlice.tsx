import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import axios from 'axios';
import { ForgotPassword, Login, Register } from '../../types/auth/auth';

interface AuthState {
  user: any | null; 
  loading: boolean;
  error: string | null;
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  user: null,
  loading: false,
  error: null,
  isAuthenticated: false,
};

export const login = createAsyncThunk('auth/login', async (loginData: Login) => {
  const response = await axios.post('/api/auth/login', loginData);
  return response.data; 
});

export const register = createAsyncThunk('auth/register', async (registerData: Register) => {
  const response = await axios.post('/api/auth/register', registerData);
  return response.data; 
});

export const forgotPassword = createAsyncThunk('auth/forgotPassword', async (forgotData: ForgotPassword) => {
  const response = await axios.post('/api/auth/forgot-password', forgotData);
  return response.data; 
});

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout(state) {
      state.user = null;
      state.isAuthenticated = false;
      state.error = null;
    },
    clearError(state) {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action: PayloadAction<any>) => {
        state.loading = false;
        state.user = action.payload; 
        state.isAuthenticated = true; 
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Login failed'; 
        state.isAuthenticated = false;
      })
      
      .addCase(register.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(register.fulfilled, (state, action: PayloadAction<any>) => {
        state.loading = false;
        state.user = action.payload;
        state.isAuthenticated = true;
      })
      .addCase(register.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Registration failed'; 
        state.isAuthenticated = false;
      })

      .addCase(forgotPassword.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(forgotPassword.fulfilled, (state) => {
        state.loading = false;
        state.error = null; 
      })
      .addCase(forgotPassword.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to send reset link'; 
      });
  },
});

export const { logout, clearError } = authSlice.actions;

export default authSlice.reducer;
