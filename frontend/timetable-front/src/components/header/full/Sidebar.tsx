import React, { useState } from 'react';
import { Box, Drawer, List, ListItem, IconButton, useTheme, Typography } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { Link } from 'react-router-dom';

const Sidebar = () => {
    const theme = useTheme();
    const [open, setOpen] = useState(false);  

    const drawerStyles = {
        transition: theme.transitions.create('width', {
            duration: theme.transitions.duration.shortest,
        }),
        width: open ? '17%' : 0,  
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
                            <Typography variant="body1" sx={{ fontSize: '1.2rem', mb: 2 }}>
                                Личный кабинет
                            </Typography>
                        </ListItem>
                        <ListItem component={Link} to="/profile/timetable">
                            <Typography variant="body1" sx={{ fontSize: '1.2rem', mb: 2 }}>
                                Мое расписание
                            </Typography>
                        </ListItem>
                        <ListItem component={Link} to="/faculties">
                            <Typography variant="body1" sx={{ fontSize: '1.2rem', mb: 2 }}>
                                По факультетам
                            </Typography>
                        </ListItem>
                        <ListItem component={Link} to="/auth/faq">
                            <Typography variant="body1" sx={{ fontSize: '1.2rem', mb: 2 }}>
                                Поддержка
                            </Typography>
                        </ListItem>
                    </List>
                </Box>
            </Drawer>
        </Box>
    );
};

export default Sidebar;
