import { Box, Grid, Typography } from '@mui/material';
import PageContainer from "../../components/container/PageContainer";
import DashboardCard from '../../components/shared/DashboardCard';
import nastya from "../../assets/images/developers/Nastya.jpg"
import daria from "../../assets/images/developers/Daria.jpg"
import mikhail from "../../assets/images/developers/Mihail.jpg"
import vova from  "../../assets/images/developers/Vova.jpg"


const AboutUs = () => {
  return (
      <PageContainer title="О нас">
          {/* breadcrumb */}
          <Box mt={2} />
          {/* end breadcrumb */}
          <Box >
          <Grid container justifyContent={"center"}>
              <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={nastya} alt="attach" width="200px" />
                        <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Мещерякова Анастасия</Typography>
                        <Typography fontWeight={400} mt={2} fontSize={18} textAlign={"center"}>Backend developer</Typography>
                    </>
                  </DashboardCard>
              </Box>
              <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={vova} alt="attach" width="200px" />
                        <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Петров Владимир</Typography>
                        <Typography fontWeight={400} mt={2} fontSize={18} textAlign={"center"}>Teamlead</Typography>
                        <Typography fontWeight={400} mt={2} fontSize={18} textAlign={"center"}>Frontend / Timetable generation</Typography>
                    </>
                  </DashboardCard>
                  </Box>
              <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={daria} alt="attach" width="200px"/>
                      <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Барсукова Дарья</Typography>
                      <Typography fontWeight={400} mt={2} fontSize={18} textAlign={"center"}>Backend developer</Typography>
                      </>
                  </DashboardCard>
                </Box>
                <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={mikhail} alt="attach" width="200px"/>
                      <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Диза Михаил</Typography>
                       <Typography fontWeight={400} mt={2} fontSize={18} textAlign={"center"}>Frontend / Timetable generation</Typography>
                      </>
                  </DashboardCard>
                </Box>
          </Grid>
          </Box>
      </PageContainer>
  );
};

export default AboutUs;
