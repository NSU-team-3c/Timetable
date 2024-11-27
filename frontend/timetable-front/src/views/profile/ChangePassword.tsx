import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ChangePasswordForm from '../../components/forms/profile/ChangePasswordForm'

const ChangePassword = () => (
    <PageContainer title="Смена пароля" description="this is Change password page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                paddingTop: '100px',
            }}
        >
            <ChangePasswordForm />
        </Box>
    </PageContainer>
);

export default ChangePassword;
