// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import RegisterForm from '../../components/forms/auth/RegisterForm';

const Register = () => (
    <PageContainer title="Регистрация" description="this is Register page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                paddingTop: '10%',
            }}
        >
            <RegisterForm />
        </Box>
    </PageContainer>
);

export default Register;
