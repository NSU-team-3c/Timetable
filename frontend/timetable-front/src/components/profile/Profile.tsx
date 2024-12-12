import React, { useEffect, useState } from 'react';
import { Box, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import { mockProfileData } from '../../_mockApis/profile';
import { ProfileData } from '../../types/user/user';
import Spinner from '../../views/spinner/Spinner';


const Profile: React.FC = () => {
  const [photoView, setPhotoView] = useState<string | null>(null);
  const [profileData, setProfileData] = useState<ProfileData | null>(null);


  useEffect(() => {
    setTimeout(() => {
      setProfileData(mockProfileData);

      if (mockProfileData.photo) {
        setPhotoView(URL.createObjectURL(mockProfileData.photo));
      }
    }, 1000);
  }, []);

  const groupView = () => {
    return (
        <Typography variant="body1" gutterBottom>
            Группа: {profileData?.group}
        </Typography>
    );
  }

  const additionLinkView = () => {
    return (
      <Box>
        <Link to="/admin/add-professor" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
          <Typography color="primary">Добавление преподавателей {'>'} </Typography>
        </Link>

        <Link to="/admin/add-subject" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
          <Typography color="primary">Добавление предметов {'>'} </Typography>
        </Link>

        <Link to="/admin/add-classroom" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
          <Typography color="primary">Добавление комнат {'>'} </Typography>
        </Link>
        
        <Link to="/admin/add-group" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
          <Typography color="primary">Добавление групп {'>'} </Typography>
        </Link>
      </Box>      
    )
  }

  if (!profileData) {
    return <Spinner />;
  }

  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2, position: 'relative' }}>

        <Typography variant="body1" gutterBottom>
            {profileData ? (profileData.role ==='professor' ? "Статус преподавателя подтвержден" : '' ) : ''}
        </Typography>  


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
            <Typography variant="body1" gutterBottom>
                Фамилия: {profileData ? profileData.surname : ''}
            </Typography>

            <Typography variant="body1" gutterBottom>
                Имя: {profileData ? profileData.name : ''}
            </Typography>

            <Typography variant="body1" gutterBottom>
                Отчество: {profileData ? profileData.patronymic : ''}
            </Typography>

            {profileData ? (profileData.role ==='student' ? groupView() : '' ) : ''}

            <Typography variant="body1" gutterBottom>
                Дата рождения: {profileData ? profileData.birthday : ''}
            </Typography>

            <Typography variant="body1" gutterBottom>
                email: {profileData ? profileData.email : ''}
            </Typography>

            <Typography variant="body1" gutterBottom>
                Номер телефона: {profileData ? profileData.phone : ''}
            </Typography>

            <Typography variant="body1" gutterBottom>
                О себе: {profileData ? profileData.about : ''}
            </Typography>


          <Link to="/profile/change-password" style={{ textAlign: 'left', display: 'block' }}>
            <Typography color="secondary"> Изменить пароль {'>'}</Typography>
          </Link>

          <Link to="/profile/profile-edit" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
            <Typography color="primary">Редактировать профиль {'>'}</Typography>
          </Link>

          {profileData ? (profileData.role === 'adminUser' ? additionLinkView() : '' ) : ''}

        </Box>
    </Box>
  );
};

export default Profile;