import React from 'react';
import { Box } from '@mui/material';
import TimetableSettings from '../../components/timetable/Timetable';
import PageContainer from '../../components/container/PageContainer';
import TimetableNotifications from '../../components/timetable/TimetableNotification';


const TimetableSettingsPage = () => (
    <PageContainer title="Настройки расписания" description="this is timetable page">
        <Box
            p={2}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
            }}
        >
            <TimetableSettings />
            <TimetableNotifications />
        </Box>
    </PageContainer>
);

export default TimetableSettingsPage;