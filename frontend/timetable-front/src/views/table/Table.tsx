import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import ProfessorAvailabilityForm from '../../components/forms/table/ProfessorAvailabilityForm';
import Table from '../../components/table/Table';

const ChangePassword = () => (
    <PageContainer title="Смена пароля" description="this is Change password page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                paddingTop: '5%',
                padding: "5%",
            }}
        >
            <Table />
        </Box>
    </PageContainer>
);

export default ChangePassword;
