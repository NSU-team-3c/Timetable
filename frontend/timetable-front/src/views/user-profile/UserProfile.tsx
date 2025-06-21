// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React from 'react';
import { Box, ButtonBase, Grid } from '@mui/material';

import axios from 'axios';
import { useNavigate } from 'react-router';
import ProfileBanner from '../../components/userprofile/profile/ProfileBanner';
import IntroCard from '../../components/userprofile/profile/IntroCard';
import PageContainer from '../../components/container/PageContainer';
import PhotosCard from '../../components/userprofile/profile/PhotosCard';

const UserProfile = () => {
  const navigate = useNavigate();


  return (
    <PageContainer title="User Profile" description="this is User Profile page">
      <Box>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <ProfileBanner />
        </Grid>

        {/* intro and Photos Card */}
        <Grid item xs={12} container spacing={3}>
            <Grid item sm={6}>
              <IntroCard />
            </Grid>
            <Grid item sm={6}>
              <PhotosCard />
            </Grid>
          
        </Grid>
      </Grid>


      </Box>
    </PageContainer>
  );
};

export default UserProfile;
