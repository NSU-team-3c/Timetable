import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfessorForm from '../../components/forms/profile/Professor';

const Profile = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                padding: '10%',
            }}
        >
            <ProfessorForm />
        </Box>
    </PageContainer>
);

export default Profile;
