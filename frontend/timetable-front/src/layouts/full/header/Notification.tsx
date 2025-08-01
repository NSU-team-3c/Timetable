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
import * as dropdownData from './data';
import img1 from "../../../assets/images/profile/1.png";

import { IconBellRinging } from '@tabler/icons-react';
import { Link } from 'react-router-dom';
import Scrollbar from '../../../components/custom-scroll/Scrollbar';
import TimetableNotifications from '../../../components/timetable/TimetableNotification';
import { AppState, useSelector } from '../../../store/Store';
import { Notification } from '../../../types/notifications/notifications';

const Notifications = () => {
  const [anchorEl2, setAnchorEl2] = useState(null);
  const notifications : Notification[] | undefined = useSelector((state: AppState) => state.notifications.notifications);

  const handleClick2 = (event: any) => {
    setAnchorEl2(event.currentTarget);
  };

  const handleClose2 = () => {
    setAnchorEl2(null);
  };

  return (
    <Box>
      <IconButton
        size="large"
        aria-label="show 11 new notifications"
        color="inherit"
        aria-controls="msgs-menu"
        aria-haspopup="true"
        sx={{
          color: anchorEl2 ? 'primary.main' : 'text.secondary',
        }}
        onClick={handleClick2}
      >
        <Badge variant="dot" color="primary">
          <IconBellRinging size="21" stroke="1.5" />
        </Badge>
      </IconButton>
      {/* ------------------------------------------- */}
      {/* Message Dropdown */}
      {/* ------------------------------------------- */}
      <Menu
        id="msgs-menu"
        anchorEl={anchorEl2}
        keepMounted
        open={Boolean(anchorEl2)}
        onClose={handleClose2}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        sx={{
          '& .MuiMenu-paper': {
            width: '360px',
          },
        }}
      >
        <Stack direction="row" py={2} px={4} justifyContent="space-between" alignItems="center">
          <Typography variant="h6">Обновления</Typography>
          <Chip label={notifications.length + " новых"} color="primary" size="small" />
        </Stack>
        <Scrollbar sx={{ height: '385px' }}>
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
                      sx={{
                        width: '240px',
                      }}
                    >
                      {notification ? notification.updatedBy : ''}
                    </Typography>
                    <Typography
                      color="textSecondary"
                      variant="subtitle2"
                      sx={{
                        width: '240px',
                      }}
                      noWrap
                    >
                       {notification ? notification.message : ''}
                    </Typography>
                  </Box>
                </Stack>
              </MenuItem>
            </Box>
          ))}
        </Scrollbar>
        <Box p={3} pb={1}>
          <Button to="/profile/notifications" variant="outlined" component={Link} color="primary" fullWidth>
            Посмотреть все обновления
          </Button>
        </Box>
      </Menu>
    </Box>
  );
};

export default Notifications;
