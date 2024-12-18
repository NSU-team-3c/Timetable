import { configureStore } from '@reduxjs/toolkit';
import CustomizerReducer from './customizer/CustomizerSlice';
import { combineReducers } from 'redux';
import  ProfileReducer  from './profile/profileSlice';
import ProfessorsReducer from './application/professor/professorSlice'
import AuditoryReducer from './application/room/auditorySlice';
import AuthReducer from './auth/authSlice';
import AvailabilityReducer from './professor/avaliabilitySlice';
import GroupReducer from './application/group/groupSlice';
import EventReducer from './application/table/eventSlice';
import SubjectReducer from './application/subject/subjectSlice'

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
    professors: ProfessorsReducer,
    rooms: AuditoryReducer,
    availability: AvailabilityReducer,
    groups: GroupReducer,
    events: EventReducer,
    subjects: SubjectReducer,
  },
});

const rootReducer = combineReducers({
  customizer: CustomizerReducer,
  auth: AuthReducer,
  profile: ProfileReducer,
  professors: ProfessorsReducer,
  rooms: AuditoryReducer,
  availability: AvailabilityReducer,
  groups: GroupReducer,
  events: EventReducer,
  subjects: SubjectReducer,
});

export type AppState = ReturnType<typeof rootReducer>;
export type AppDispatch = typeof store.dispatch;
export const { dispatch } = store;
export const useDispatch = () => useAppDispatch<AppDispatch>();
export const useSelector: TypedUseSelectorHook<AppState> = useAppSelector;

export default store;
