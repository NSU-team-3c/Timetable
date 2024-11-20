import React from 'react';
import {Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ForgotPasswordForm from '../../components/forms/auth/ForgotPasswordForm';

const Login = () => (
    <PageContainer title="Вход" description="this is Login page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                paddingTop: '100px',
            }}
        >
            <ForgotPasswordForm />
        </Box>
    </PageContainer>
);

export default Login;
