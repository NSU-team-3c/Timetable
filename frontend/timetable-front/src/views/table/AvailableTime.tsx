import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfessorAvailabilityForm from '../../components/forms/table/ProfessorAvailabilityForm';

const AvailableTime = () => (
    <PageContainer title="Выбор времени" description="this is professor availability page">
       
            <ProfessorAvailabilityForm />
    
    </PageContainer>
);

export default AvailableTime;
