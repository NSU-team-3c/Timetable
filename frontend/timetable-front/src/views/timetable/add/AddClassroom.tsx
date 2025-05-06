import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../../components/container/PageContainer';
import ClassroomList from '../../../components/list/ClassroomList';



const AddClassroom = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={2}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
            }}
        >
            <ClassroomList />
        </Box>
    </PageContainer>
);

export default AddClassroom;
