import React, { useEffect, useState } from 'react';
import { Box, Button, ButtonBase, Grid, Grid2, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import { useWebSocket } from '../../hooks/useWebSocket';


const TimetableNotifications: React.FC = () => {
  const { messages, sendMessage, isConnected } = useWebSocket('http://localhost:8080/websockets', sessionStorage.getItem('authToken'));
  const [input, setInput] = useState('');


  return (
    <Box sx={{margin: '0 auto', padding: 2, position: 'relative' }}>
         <Typography color={"secondary"} mt={2} fontSize={24} mb={3} textAlign={'center'} fontWeight={600}>Последние обновления</Typography>
         <Typography fontSize={18} >Статус: {isConnected ? 'Online' : 'Offline'}</Typography>
         <Box mt={2}>
            {messages.map((msg, idx) => (
                <Typography fontSize={18}key={idx}>{msg}</Typography>
            ))}
         </Box>
    </Box>
  );
};

export default TimetableNotifications;