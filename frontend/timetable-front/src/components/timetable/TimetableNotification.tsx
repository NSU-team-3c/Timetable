import React, { useEffect, useState } from 'react';
import { Box, Button, ButtonBase, Grid, Grid2, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import { useWebSocket } from '../../hooks/useWebSocket';


const TimetableNotifications: React.FC = () => {
  useWebSocket('http://localhost:8080/websockets', sessionStorage.getItem('authToken'));
  return (
    <></>
  );
};

export default TimetableNotifications;