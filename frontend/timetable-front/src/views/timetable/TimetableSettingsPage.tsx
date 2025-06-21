import React from 'react';
import { Box } from '@mui/material';
import TimetableSettings from '../../components/timetable/Timetable';
import PageContainer from '../../components/container/PageContainer';
import TimetableNotifications from '../../components/timetable/TimetableNotification';
import BlankCard from '../../components/shared/BlankCard';


const TimetableSettingsPage = () => (
    <PageContainer title="Настройки расписания" description="this is timetable page">
       
        <Box
            mt={2}
        >
            <TimetableSettings />
        </Box>
    </PageContainer>
);

export default TimetableSettingsPage;