import React, { useEffect, useState } from 'react';
import { Backdrop, Box, Button, ButtonBase, Fade, Grid, Grid2, Modal, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import Spinner from '../../views/spinner/Spinner';
import { AppState, dispatch, useSelector } from '../../store/Store';
import { fetchProfile } from '../../store/profile/profileSlice';
import axiosInstance from '../../utils/axios';
import { format } from 'date-fns';
import Result from './Result';
import {  ResultType } from '../../types/timetable/timetable';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50%',
  height: '30%',
  bgcolor: 'background.paper',
  //overflowY: 'auto',
  boxShadow: 24,
  p: 4,
};


const TimetableSettings: React.FC = () => {
  const {profile} = useSelector((state: AppState) => state.profile);
  const role = profile?.role.split(', ');
  const [wait, setWait] = useState(false);
  const [open, setOpen] = useState(false);

  const [result, setResult] = useState<ResultType>();

  const sendRequest = async () => {

    let isGenerating = false
    let response
    try {
      response = await axiosInstance.get('/api/v1/timetables/generating_status');

      isGenerating = response.data.isGenerating
    } catch (err) {
      console.log(err)
      response = false;
    }

    console.log(isGenerating)

    if (!isGenerating) {
      setWait(true)
      setOpen(true)
      let res: ResultType = {isGeneratedSuccessfully: true, events: [], unplacedSubjects: []}
      try {
      const res2 = await axiosInstance.get('/api/v1/timetables/generate');
      res = res2.data
    } catch (err) {
      console.log(err)
      res = {isGeneratedSuccessfully: false, events: [], unplacedSubjects: []}
    }
    console.log(res)
    setResult(res)
    setWait(false)
    }
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
      <Modal
                          aria-labelledby="transition-modal-title"
                          aria-describedby="transition-modal-description"
                          open={open}
                          onClose={() => setOpen(false)}
                          closeAfterTransition
                          slots={{ backdrop: Backdrop }}
                      >
                          <Fade in={open}>
                              <Box sx={style}>
                                <Result setOpen={setOpen} wait={wait} result={result}/>
                              </Box>
                          </Fade>
                    </Modal>
          {additionLinkView()}
    </Box>
  );
};

export default TimetableSettings;