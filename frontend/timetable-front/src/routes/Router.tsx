// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React, { lazy } from 'react';
import { Navigate } from 'react-router-dom';
import Loadable from '../layouts/full/shared/loadable/Loadable';

/* ***Layouts**** */
const BlankLayout = Loadable(lazy(() => import('../layouts/blank/BlankLayout')));

/* ***Auth*** */
const Login           = Loadable(lazy(() => import('../views/auth/Login')));
const Register        = Loadable(lazy(() => import('../views/auth/Register')));
const ForgotPassword  = Loadable(lazy(() => import('../views/auth/ForgotPassword')));

const Timetable = Loadable(lazy(() => import('../views/table/Table'))) 

/* ***Profile*** */
const Profile           = Loadable(lazy(() => import('../views/profile/Profile')))
const ChangePassword    = Loadable(lazy(() => import('../views/profile/ChangePassword')))
//const ProfessorProfile  = Loadable(lazy(() => import()))

const Error = Loadable(lazy(() => import('../views/auth/Error')));


const Router = [
  {
    path: '/',
    element: <BlankLayout />,
    children: [
      { path: '/', element: <Navigate to="/auth/login" /> },
      { path: '/auth/404', element: <Error /> },
      { path: '/auth/login', element: <Login /> },
      { path: '/auth/register', element: <Register /> },
      { path: '/auth/forgot-password', element: <ForgotPassword /> },
      { path: '*', element: <Navigate to="/auth/404" /> },
    ],
  },
  {
    path: '/profile',
    element: <BlankLayout/>,
    children: [
      { path: '/profile', element: <Profile /> },
      { path: '/profile/timetable', element: <Timetable/> },
      { path: '/profile/change-password', element: <ChangePassword/> },
      //{ path: '/profile/professor', element: <ProfessorProfile/>}
    ],
  },
  
];

export default Router;
