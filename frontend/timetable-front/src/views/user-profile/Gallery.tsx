// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { Grid } from '@mui/material';
import PageContainer from "../../components/container/PageContainer";
import ChildCard from "../../components/shared/ChildCard";
import ProfileBanner from '../../components/userprofile/profile/ProfileBanner';
import GalleryCard from '../../components/userprofile/gallery/GalleryCard';


const Gallery = () => {
  return (
    <PageContainer title="User Profile" description="this is User Profile page">
      <Grid container spacing={3}>
        <Grid item sm={12}>
          <ProfileBanner />
        </Grid>
        <Grid item sm={12}>
          <ChildCard>
            <GalleryCard />
          </ChildCard>
        </Grid>
      </Grid>
    </PageContainer>
  );
};

export default Gallery;
