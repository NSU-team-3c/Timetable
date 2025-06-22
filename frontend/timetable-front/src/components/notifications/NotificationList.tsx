// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React, { useState } from 'react';
import {
  IconButton,
  Box,
  Badge,
  Menu,
  MenuItem,
  Avatar,
  Typography,
  Button,
  Chip,
  Stack
} from '@mui/material';
import img1 from "../../assets/images/profile/1.png";

import { IconBellRinging } from '@tabler/icons-react';
import { Link } from 'react-router-dom';
import { AppState, useSelector } from '../../store/Store';
import { Notification } from '../../types/notifications/notifications';
import Scrollbar from '../custom-scroll/Scrollbar';

const NotificationsList = () => {
  const [anchorEl2, setAnchorEl2] = useState(null);
  const notifications : Notification[] | undefined = useSelector((state: AppState) => state.notifications.notifications);

  const handleClick2 = (event: any) => {
    setAnchorEl2(event.currentTarget);
  };

  const handleClose2 = () => {
    setAnchorEl2(null);
  };

  return (
    <Box p={4}>
        
        <Box mb={2} >
            <Typography variant="h4" gutterBottom>Обновления</Typography>
        </Box>

    <Box>
      <Scrollbar sx={{ }}>
          {notifications.map((notification : Notification, index : number) => (
            <Box key={index}>
              <MenuItem sx={{ py: 2, px: 4 }}>
                <Stack direction="row" spacing={2}>
                  <Avatar
                    src={img1}
                    alt={img1}
                    sx={{
                      width: 48,
                      height: 48,
                    }}
                  />
                  <Box>
                    <Typography
                      variant="subtitle2"
                      color="textPrimary"
                      fontWeight={600}
                      noWrap
                      
                    >
                      {notification ? notification.updatedBy : ''}
                    </Typography>
                    <Typography
                      color="textSecondary"
                      variant="subtitle2"
                     
                      noWrap
                    >
                       {notification ? notification.message : ''}
                    </Typography>
                    <Typography
                      color="textSecondary"
                      variant="subtitle2"
                      
                      noWrap
                    >
                       {notification.subMessage ? notification.subMessage : ''}
                    </Typography>
                     <Typography
                      color="textSecondary"
                      variant="subtitle2"
                      
                      noWrap
                    >
                       {notification ? notification.updatedAt : ''}
                    </Typography>
                  </Box>
                </Stack>
              </MenuItem>
            </Box>
          ))}
        </Scrollbar>
        </Box>
    </Box>
  );
};

export default NotificationsList;
