import { AppBar, Box, Stack, Toolbar, Typography, styled } from '@mui/material';

import Search from '../Search';
import Navigation from './GuestNavigation';

const Header = () => {
    const AppBarStyled = styled(AppBar)(({ theme }) => ({
        boxShadow: 'none',
        background: theme.palette.primary.light,
        justifyContent: 'center',
        backdropFilter: 'blur(4px)',
    }));

    const ToolbarStyled = styled(Toolbar)(({ theme }) => ({
        width: '100%',
        color: theme.palette.text.secondary,
    }));

    return (
        <Box
            sx={{
                borderBottom: 1,
                maxHeight: 50,
                borderBottomColor: 'primary',
                borderRadius: 0,
            }}
        >
            <AppBarStyled position="sticky" color="default">
                <ToolbarStyled>
                   <Box  px={3}>
              <Typography color='primary' fontWeight={600} fontSize={20}>University</Typography>
               <Typography fontSize={20}>Timetable</Typography>
            </Box>

                    <>
                        <Navigation />
                    </>


                    <Box flexGrow={1} />
                    <Stack spacing={1} direction="row" alignItems="center">
                        <Search />
                    </Stack>
                </ToolbarStyled>
            </AppBarStyled>
        </Box>
    );
};

export default Header;
