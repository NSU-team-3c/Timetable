import React, { useEffect, useState } from 'react';
import { Box, Button, ButtonBase, Grid, Grid2, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import Spinner from '../../views/spinner/Spinner';
import { AppState, dispatch, useSelector } from '../../store/Store';
import { fetchProfile } from '../../store/profile/profileSlice';
import axiosInstance from '../../utils/axios';
import { format } from 'date-fns';


const TimetableSettings: React.FC = () => {
  const {profile} = useSelector((state: AppState) => state.profile);
  const role = profile?.role.split(', ');

  const sendRequest = async () => {
    await axiosInstance.get('/api/v1/timetables/generate');
  }

  const additionLinkView = () => {
    console.log(role);

    if (role?.includes('administrator', 0)) {
      console.log(0);
      return (
        <Box mt={2} sx={{boxShadow: 15, backgroundColor: 'white'}} p={4} mb={2}>
          <Typography mt={2} fontSize={24} mb={3} textAlign={'center'} fontWeight={600}>Генерация расписания </Typography>

        <Grid2 container mt={2}>
        
        <Grid2 alignItems={'center'}>
          <Button sx={{marginBottom: 1}} color={'inherit'} component={Link} to={"/admin/timetable/add-professor"}>
              <Typography fontSize={18} color="primary">Добавление преподавателей </Typography>
          </Button>
        
          <Button sx={{marginBottom: 1}} color={'inherit'} component={Link} to="/admin/timetable/add-subject">
            <Typography fontSize={18} color="primary">Добавление предметов </Typography>
          </Button>
        </Grid2>
        <Grid2>
          <Button sx={{marginBottom: 1}} color={'inherit'} component={Link} to="/admin/timetable/add-classroom">
            <Typography fontSize={18} color="primary">Добавление комнат</Typography>
          </Button>

          <Button sx={{marginBottom: 1}} color={'inherit'} component={Link} to="/admin/timetable/add-group">
            <Typography fontSize={18} color="primary">Добавление групп  </Typography>
          </Button>
          </Grid2>
          </Grid2>
          <Box mt={2}>
            <Button
              fullWidth
              variant="contained"
              color="primary"
              onClick={sendRequest}
            >
                <Typography fontSize={18} p={1}>Сгенерировать расписание  </Typography>
              
            </Button>
          </Box>
        </Box>
      )
    }
  }

  if (!profile) {
    return <Spinner />;
  }

  

  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2, position: 'relative' }}>
          {additionLinkView()}
    </Box>
  );
};

export default TimetableSettings;