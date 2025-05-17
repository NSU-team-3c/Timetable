import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../../components/container/PageContainer';
import SubjectList from '../../../components/list/SubjectList';


const AddSubject = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={2}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
            }}
        >
            <SubjectList />
        </Box>
    </PageContainer>
);

export default AddSubject;
