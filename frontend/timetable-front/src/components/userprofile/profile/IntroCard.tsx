import { Backdrop, Box, Button, Fade, Modal, Stack, Typography } from '@mui/material';

import { IconAccessible, IconBriefcase, IconUpload } from '@tabler/icons-react';
import { useEffect, useState } from 'react';
import ChildCard from "../../../components/shared/ChildCard";
import { useDispatch, useSelector } from "../../../store/Store";
import UpdateProfile from './UpdateProfile';
import { AppState, dispatch } from "../../../store/Store";
import { format } from 'date-fns';
import { fetchProfile } from '../../../store/profile/profileSlice';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50%',
  height: '500',
  bgcolor: 'background.paper',
  //overflowY: 'auto',
  boxShadow: 24,
  p: 4,
};

const IntroCard  = () => {
    const dispatch = useDispatch();

    const [updateProfile, setUpdateProfile] = useState(false);
    const {profile} = useSelector((state: AppState) => state.profile);
  
    useEffect(() => {
      dispatch(fetchProfile())
    }, [dispatch]);
  
    console.log(profile)

    return (
  <ChildCard>
    <Modal
                    aria-labelledby="transition-modal-title"
                    aria-describedby="transition-modal-description"
                    open={updateProfile}
                    onClose={() => setUpdateProfile(false)}
                    closeAfterTransition
                    slots={{ backdrop: Backdrop }}
                >
                    <Fade in={updateProfile}>
                        <Box sx={style}>
                          <UpdateProfile setUpdateProfile={setUpdateProfile} />
                        </Box>
                    </Fade>
              </Modal>
    <Typography fontWeight={600} variant="h4" mb={2}>
      Основная информация
    </Typography>
    <Typography color="textSecondary" variant="subtitle2" mb={2}>
      Здесь будет собрана информация, на основе которой будут состовляться рекомендации.
    </Typography>
    
    <Stack direction="row" gap={2} alignItems="center" mb={3}>
      <IconBriefcase size="21" />
      <Typography variant="h6">Дата рождения:  </Typography>
       <Typography fontWeight={600} variant="body1" gutterBottom>
                      {profile?.birthday ? format(new Date(profile.birthday), 'dd.MM.yy') : ''}
                </Typography>
    </Stack>
    <Stack direction="row" gap={2} alignItems="center" mb={3}>
      <IconAccessible size="21" />
       <Typography variant="h6">Почта:  </Typography>
      <Typography fontWeight={600} variant="body1" gutterBottom>
                    {profile ? profile.email : ''}
                </Typography>
    </Stack>
    <Stack direction="row" gap={2} alignItems="center" mb={3}>
      <IconAccessible size="21" />
       <Typography variant="h6">Номер телефона: </Typography>
     <Typography fontWeight={600} variant="body1" gutterBottom>
                    {profile ? profile.phone : ''}
                </Typography>
    </Stack>
    <Stack direction="row" gap={2} alignItems="center" mb={3}>
      <IconAccessible size="21" />
      <Typography variant="h6">Дата рождения: </Typography>
      <Typography fontWeight={600} variant="body1" gutterBottom>
                    {profile ? profile.birthday : ''}
                </Typography>
    </Stack>
    <Stack direction="row" gap={2} alignItems="center" mb={3}>
      <IconAccessible size="21" />
      <Typography variant="h6">О себе: </Typography>
      <Typography fontWeight={600} variant="body1" gutterBottom>
                    {profile ? profile.about : ''}
                </Typography>
    </Stack>
    

    <Button onClick={() => setUpdateProfile(true)}>
                <IconUpload />
    </Button>
  </ChildCard>
  );
};

export default IntroCard;
