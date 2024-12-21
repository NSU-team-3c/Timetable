import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import Profile from '../../components/profile/Profile';


const ProfilePage = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={2}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
            }}
        >
            <Profile />
        </Box>
    </PageContainer>
);

export default ProfilePage;
