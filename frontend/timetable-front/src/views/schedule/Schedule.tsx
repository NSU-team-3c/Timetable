import { Box, Grid, Typography } from '@mui/material';
import PageContainer from "../../components/container/PageContainer";
import Table from '../../components/table/Table';
import BlankCard from '../../components/shared/BlankCard';


const Schedule = () => {
  return (
      <PageContainer title="Расписание">
          {/* breadcrumb */}
          <Box mt={2}>
          <BlankCard >
            <Box p={4}>
           <Table/>
           </Box>
           </BlankCard>
           </Box>
        
      </PageContainer>
  );
};

export default Schedule;
