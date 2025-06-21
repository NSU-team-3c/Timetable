// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { Box } from "@mui/material";
import PageContainer from "../../components/container/PageContainer";
import TimetableNotifications from '../../components/timetable/TimetableNotification';

const TimetableUpdatesPage = () => {
    return (
        <PageContainer title="Обновления в данных" description="this is Faq page">
            <Box
                        mt={2}
                        sx={{backgroundColor: 'white'}}
                    >
            <TimetableNotifications />
            </Box>
        </PageContainer>
    );
};

export default TimetableUpdatesPage;

