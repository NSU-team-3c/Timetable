import { Box, Drawer, useTheme } from '@mui/material';
import Sidebar from './Sidebar';


const Navigation = () => {
    const theme = useTheme();

    return (
        <Box>
            <Drawer
                anchor="left"
                open
                variant="permanent"
                PaperProps={{
                sx: {
                    transition: theme.transitions.create('width', {
                    duration: theme.transitions.duration.shortest,
                    }),
                    width: '100%',
                    boxSizing: 'border-box',
                },
                }}
            >
                {/* ------------------------------------------- */}
                {/* Sidebar Box */}
                {/* ------------------------------------------- */}
                
                <Sidebar />
            </Drawer>
        </Box>
    );

};

export default Navigation;
