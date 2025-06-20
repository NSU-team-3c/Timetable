import { Box, Grid, Typography } from '@mui/material';
import PageContainer from "../../components/container/PageContainer";
import DashboardCard from '../../components/shared/DashboardCard';
import img1 from "../../assets/images/profile/user-1.jpg"
import img2 from "../../assets/images/profile/user-2.jpg"
import img3 from "../../assets/images/profile/user-3.jpg"


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
                        <img src={img1} alt="attach" width="200px" />
                        <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Мещерякова Анастасия</Typography>
                    </>
                  </DashboardCard>
              </Box>
              <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={img2} alt="attach" width="200px" />
                        <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Петров Владимир</Typography>
                    </>
                  </DashboardCard>
                  </Box>
              <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={img3} alt="attach" width="200px"/>
                      <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Барсукова Дарья</Typography>
                      </>
                  </DashboardCard>
                </Box>
                <Box display={"flex"} m={1}>
                  <DashboardCard>
                    <>
                        <img src={img3} alt="attach" width="200px"/>
                      <Typography fontWeight={600} mt={2} fontSize={20} textAlign={"center"}>Диза Михаил</Typography>
                      </>
                  </DashboardCard>
                </Box>
          </Grid>
          </Box>
      </PageContainer>
  );
};

export default AboutUs;
