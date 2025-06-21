import { Box, Button } from '@mui/material';
import { Link } from 'react-router-dom';

const ButtonSx = {
    marginLeft: '20px',
    color: '#434343',
};

const GuestLinks = () => {
    return (
        <>
            <Box textAlign={'center'}>
           
            <Button sx={ButtonSx} color="primary" variant="text" to="/auth/login" 
                component={Link}
            >
                ЛИЧНЫЙ КАБИНЕТ
            </Button>
            <Button sx={ButtonSx} color="primary" variant="text" to="/auth/about" 
                component={Link}
            >
                О НАС
            </Button>
            <Button sx={ButtonSx} color="primary" variant="text" to="/auth/faq" 
                component={Link}
                >
                ПОМОЩЬ
            </Button>
            </Box>
        </>
    );
};

export default GuestLinks;
