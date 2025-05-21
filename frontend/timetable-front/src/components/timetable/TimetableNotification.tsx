import React, { useEffect, useState } from 'react';
import { Box, Button, ButtonBase, Grid, Grid2, Typography} from '@mui/material';
import { Link } from 'react-router-dom'; 
import { useWebSocket } from '../../hooks/useWebSocket';


const TimetableNotifications: React.FC = () => {
  const { messages, sendMessage, isConnected } = useWebSocket('ws://localhost:8080/ws', sessionStorage.getItem('authToken'));
  const [input, setInput] = useState('');

  const handleSend = () => {
    sendMessage(input);
    setInput('');
  };


  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2, position: 'relative' }}>
         <Typography color={"secondary"} mt={2} fontSize={24} mb={3} textAlign={'center'} fontWeight={600}>Последние обновления</Typography>
         <Typography fontSize={18} >Статус: {isConnected ? 'Online' : 'Offline'}</Typography>
         <Box mt={2}>
            {messages.map((msg, idx) => (
                <Typography fontSize={18}key={idx}>{msg}</Typography>
            ))}
         </Box>
         <Box mt={2}>
         <input
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                />
            <Button onClick={handleSend}>Отправить</Button>
         </Box>
    </Box>
  );
};

export default TimetableNotifications;