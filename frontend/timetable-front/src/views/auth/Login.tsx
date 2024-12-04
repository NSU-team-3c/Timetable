// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import LoginForm from '../../components/forms/auth/AuthForm';

const Login = () => (
    <PageContainer title="Вход" description="this is Login page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                paddingTop: '10%',
            }}
        >
            <LoginForm />
        </Box>
    </PageContainer>
);

export default Login;
