import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import Table from '../../components/table/Table';

const ChangePassword = () => (
    <PageContainer title="Расписание" description="this is timetable view page">
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
