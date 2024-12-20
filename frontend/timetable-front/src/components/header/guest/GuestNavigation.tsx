import { Button } from '@mui/material';
import { Link } from 'react-router-dom';

const ButtonSx = {
    marginLeft: '20px',
    color: '#434343',
};

const GuestLinks = () => {
    return (
        <>
            <Button sx={ButtonSx} color="primary" variant="text" to="/auth/login" 
                component={Link}
            >
                ЛИЧНЫЙ КАБИНЕТ
            </Button>
            <Button sx={ButtonSx} color="primary" variant="text" to="/auth/courses"
                component={Link}
            >
                КУРСЫ
            </Button>
            <Button sx={ButtonSx} color="primary" variant="text" to="/auth/disciplines"
                component={Link}
            >
                ДИСЦИПЛИНЫ
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
        </>
    );
};

export default GuestLinks;
