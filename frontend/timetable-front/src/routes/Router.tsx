// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React, { lazy } from 'react';
import { Navigate } from 'react-router-dom';
import Loadable from '../layouts/full/shared/loadable/Loadable';

import GuestGuard from '../guards/guestGuard'
import AuthGuard from '../guards/authGuard';

/* ***Layouts**** */
const BlankLayout = Loadable(lazy(() => import('../layouts/blank/BlankLayout')));
const GuestLayout = Loadable(lazy(() => import('../layouts/guest/GuestLayout')));
const FullLayout  = Loadable(lazy(() => import('../layouts/full/FullLayout')));

const Error       = Loadable(lazy(() => import('../views/auth/Error')));

/* ***Auth*** */
const Login             = Loadable(lazy(() => import('../views/auth/Login')));
const Register          = Loadable(lazy(() => import('../views/auth/Register')));
const ForgotPassword    = Loadable(lazy(() => import('../views/auth/ForgotPassword')));

/* ***Profile*** */
const Profile           = Loadable(lazy(() => import('../views/profile/Profile')));
const ProfileEdit       = Loadable(lazy(() => import('../views/profile/ProfileEdit')));
const ChangePassword    = Loadable(lazy(() => import('../views/profile/ChangePassword')));
const ProfessorProfile  = Loadable(lazy(() => import('../views/profile/Professor')));


/* ***Admin*** */
const AddProfessor  = Loadable(lazy(() => import('../views/add/AddProfessor')));
const AddSubject    = Loadable(lazy(() => import('../views/add/AddSubject')));
const AddClassroom  = Loadable(lazy(() => import('../views/add/AddClassroom')));
const AddGroup      = Loadable(lazy(() => import('../views/add/AddGroup')));

const Timetable         = Loadable(lazy(() => import('../views/table/Table')));
const CreateCourse      = Loadable(lazy(() => import('../views/courses/Course')));
const AvaliabilityTime  = Loadable(lazy(() => import('../views/table/AvailableTime')));

const Router = [
  {
    path: '/',
    element: (
      <GuestGuard>
        <GuestLayout />
      </GuestGuard>
    ),
    children: [
      { path: '/',                      element: <Navigate to="/auth/login" /> },
      { path: '/auth/login',            element: <Login /> },
      { path: '/auth/register',         element: <Register /> },
      { path: '/auth/forgot-password',  element: <ForgotPassword /> },
    ],
  },
  {
    path: '/profile',
    element: (
      <AuthGuard>
        <FullLayout/>
      </AuthGuard>
    ),
    children: [
      { path: '/profile',                         element: <Profile /> },
      { path: '/profile/profile-edit',            element: <ProfileEdit /> },
      { path: '/profile/timetable',               element: <Timetable /> },
      { path: '/profile/change-password',         element: <ChangePassword /> },
      { path: '/profile/professor',               element: <ProfessorProfile /> },
      { path: '/profile/professor/course',        element: <CreateCourse /> },
      { path: '/profile/professor/availability',  element: <AvaliabilityTime /> }
    ],
  },

  {
    path: '/admin',
    element: (
      <AuthGuard>
        <FullLayout/>
      </AuthGuard>
    ),
    children: [
      { path: '/admin/add-professor',  element: <AddProfessor /> },
      { path: '/admin/add-subject',     element: <AddSubject /> },
      { path: '/admin/add-classroom',   element: <AddClassroom /> },
      { path: '/admin/add-group',       element: <AddGroup /> },
    ],
  },
  {
    path: '/',
    element: <BlankLayout />,
    children: [
        { path: '404',  element: <Error /> },
        { path: '*',    element: <Navigate to="/404" /> },
    ],
  },
];

export default Router;

