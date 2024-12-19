import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ClassroomList from '../../components/list/ClassroomList';


const AddClassroom = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                padding: '10%',
            }}
        >
            <ClassroomList />
        </Box>
    </PageContainer>
);

export default AddClassroom;
