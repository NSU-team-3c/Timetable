import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfessorAvailabilityForm from '../../components/forms/table/ProfessorAvailabilityForm';

const ChangePassword = () => (
    <PageContainer title="Выбор времени" description="this is professor availability page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                padding: '5%',
            }}
        >
            <ProfessorAvailabilityForm />
        </Box>
    </PageContainer>
);

export default ChangePassword;
