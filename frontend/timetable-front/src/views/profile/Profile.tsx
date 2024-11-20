import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfileForm from '../../components/forms/profile/Profile';

const Profile = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                paddingTop: '100px',
            }}
        >
            <ProfileForm />
        </Box>
    </PageContainer>
);

export default Profile;
