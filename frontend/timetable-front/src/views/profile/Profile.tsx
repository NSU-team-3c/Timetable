import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfileForm from '../../components/forms/profile/Profile';
import Table from '../table/Table';
import Profile from '../../components/profile/Profile';


const ProfilePage = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                padding: '10%',
            }}
        >
            <Profile />
        </Box>
    </PageContainer>
);

export default ProfilePage;
