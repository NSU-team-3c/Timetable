import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfessorsList from '../../components/list/ProfessorsList';


const AddProfessor = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                padding: '10%',
            }}
        >
            <ProfessorsList />
        </Box>
    </PageContainer>
);

export default AddProfessor;
