import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfessorsList from '../../components/list/ProfessorsList';


const AddProfessor = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={2}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
            }}
        >
            <ProfessorsList />
        </Box>
    </PageContainer>
);

export default AddProfessor;
