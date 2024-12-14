import { configureStore } from '@reduxjs/toolkit';
import CustomizerReducer from './customizer/CustomizerSlice';
import { combineReducers } from 'redux';
import  ProfileReducer  from './profile/profileSlice';
import ProfessorReducer from './profile/professorSlice'
import AuditoryReducer from './application/room/auditorySlice'
import AuthReducer from './auth/authSlice';
import {
  useDispatch as useAppDispatch,
  useSelector as useAppSelector,
  TypedUseSelectorHook,
} from 'react-redux';

export const store = configureStore({
  reducer: {
    customizer: CustomizerReducer,
    auth: AuthReducer,
    profile: ProfileReducer,
    professor: ProfessorReducer,
    rooms: AuditoryReducer,
  },
});

const rootReducer = combineReducers({
  customizer: CustomizerReducer,
  auth: AuthReducer,
  profile: ProfileReducer,
  professor: ProfessorReducer,
  rooms: AuditoryReducer,
});

export type AppState = ReturnType<typeof rootReducer>;
export type AppDispatch = typeof store.dispatch;
export const { dispatch } = store;
export const useDispatch = () => useAppDispatch<AppDispatch>();
export const useSelector: TypedUseSelectorHook<AppState> = useAppSelector;

export default store;
