import React, { useEffect, useState } from 'react';
import { Box, Button, ButtonBase, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import Spinner from '../../views/spinner/Spinner';
import { AppState, dispatch, useSelector } from '../../store/Store';
import { fetchProfile } from '../../store/profile/profileSlice';
import axiosInstance from '../../utils/axios';
import { format } from 'date-fns';


const Profile: React.FC = () => {
  const [photoView, setPhotoView] = useState<string | null>(null);
  const {profile} = useSelector((state: AppState) => state.profile);
  
  useEffect(() => {
    dispatch(fetchProfile())

    if (profile?.photoUrl) {
      setPhotoView(URL.createObjectURL(profile.photoUrl));
    }

  }, [dispatch]);

  const sendRequest = async () => {
    await axiosInstance.get('/api/v1/timetables/generate');
  }

  const groupView = () => {
    return (
        <Typography fontWeight={600} variant="body1" gutterBottom>
            Группа: {profile?.group}
        </Typography>
    );
  }

  if (!profile) {
    return <Spinner />;
  }

  

  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2, position: 'relative' }}>

        <Box sx={{ position: 'absolute', top: 0, right: 0, zIndex: 1, }}>
            {photoView ? (
                <img
                src={photoView}
                alt="View"
                style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                    borderRadius: '5%',
                }}
            /> ) : ''}
        </Box>

        <Box display="flex" flexDirection="column" gap={2} padding={"10%"}>
            <Typography fontWeight={600} variant="body1" gutterBottom>
                Фамилия: {profile ? profile.surname : ''}
            </Typography>

            <Typography fontWeight={600} variant="body1" gutterBottom>
                Имя: {profile ? profile.name : ''}
            </Typography>

            <Typography fontWeight={600} variant="body1" gutterBottom>
                Отчество: {profile ? profile.patronymic : ''}
            </Typography>

            { profile ? (!(profile.role.split(', ').includes('student') || 
              (profile.role.split(', ').includes('administrator'))) ? groupView() : '' ) : ''
            }

            <Typography fontWeight={600} variant="body1" gutterBottom>
                  Дата рождения: {profile?.birthday ? format(new Date(profile.birthday), 'dd.MM.yy') : ''}
            </Typography>

            <Typography fontWeight={600} variant="body1" gutterBottom>
                email: {profile ? profile.email : ''}
            </Typography>

            <Typography fontWeight={600} variant="body1" gutterBottom>
                Номер телефона: {profile ? profile.phone : ''}
            </Typography>

            <Typography fontWeight={600} variant="body1" gutterBottom>
                О себе: {profile ? profile.about : ''}
            </Typography>


          <Link to="/profile/change-password" style={{ textAlign: 'left', display: 'block' }}>
            <Typography color="secondary"> Изменить пароль {'>'}</Typography>
          </Link>

          <Link to="/profile/profile-edit" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
            <Typography color="primary">Редактировать профиль {'>'}</Typography>
          </Link>

        </Box>
    </Box>
  );
};

export default Profile;