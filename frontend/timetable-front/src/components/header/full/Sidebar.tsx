import React, { useState } from 'react';
import { Box, Drawer, List, ListItem, IconButton, useTheme, Typography, Button } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { Link } from 'react-router-dom';

const Sidebar = () => {
    const theme = useTheme();
    const [open, setOpen] = useState(false);  

    const drawerStyles = {
        transition: theme.transitions.create('width', {
            duration: theme.transitions.duration.shortest,
        }),
        width: open ? '20%' : 0,  
        boxSizing: 'border-box',
    };

    const toggleDrawer = () => {
        setOpen(!open);
    };

    return (
        <Box sx={{ display: 'flex' }}>
            {/* Кнопка бургер-меню */}
            <IconButton onClick={toggleDrawer} sx={{ position: 'absolute', top: 10, left: 10, zIndex: 10 }}>
                <MenuIcon />
            </IconButton>

            {/* Drawer (меню) */}
            <Drawer
                anchor="left"
                open={open}
                variant="temporary"  
                onClose={toggleDrawer}
                PaperProps={{
                    sx: drawerStyles,
                }}
            >
                <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <List>
                <ListItem component={Link} to="/profile">
                        <Typography
                            sx={{
                                marginTop: 1,
                                marginBottom: 3,
                                width: '20%',
                                fontSize: 24,
                                color: 'secondary.main',
                            }}
                        >
                            TimeTable
                         </Typography>
                         </ListItem>
                        <ListItem component={Link} to="/profile">
                            <Button color='primary' sx={{ mb: 1, mt: 2}}>
                            <Typography fontSize={20}>
                                Личный кабинет
                            </Typography>
                            </Button>
                        </ListItem>
                        <ListItem component={Link} to="/profile/timetable">
                            <Button color='primary' sx={{ mb: 1 }}>
                            <Typography fontSize={20}>
                                Мое расписание
                            </Typography>
                            </Button>
                        </ListItem>
                        <ListItem component={Link} to="/faculties">
                        <Button color='primary' sx={{ mb: 1 }}>
                            <Typography  fontSize={20}>
                                По факультетам
                            </Typography>
                            </Button>
                        </ListItem>
                        <ListItem component={Link} to="/auth/faq">
                        <Button color='primary' sx={{ mb: 1 }}>
                            <Typography fontSize={20}>
                                Поддержка
                            </Typography>
                            </Button>
                        </ListItem>
                    </List>
                </Box>
            </Drawer>
        </Box>
    );
};

export default Sidebar;
