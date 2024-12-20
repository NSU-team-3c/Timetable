import { AppBar, Box, Stack, Toolbar, Typography, styled } from '@mui/material';

import Search from '../Search';
import Navigation from './FullNavigation';
import { useLocation } from 'react-router-dom';

const Header = () => {
    const location = useLocation();
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

    const getHeaderText = () => {
        switch (location.pathname) {
            case '/about':
                return 'О нас';
            case '/services':
                return 'Услуги';
            case '/contact':
                return 'Контакты';
            default:
                return 'Страница';
        }
    };



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
                    <>
                        <Navigation />
                    </>

                    <Typography
                        sx={{
                            width: '100%',
                            paddingLeft: '15%',
                            fontSize: 20,
                            color: 'primary.main',
                        }}
                    >
                        {getHeaderText()}
                    </Typography>



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
