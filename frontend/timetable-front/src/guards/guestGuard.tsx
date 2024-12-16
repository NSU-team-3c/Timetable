import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthGuard = ({ children }: any) => {
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    useEffect(() => {
        if (authToken) {
            navigate('/profile', { replace: true });
        }
    }, [authToken, navigate]);

    return children;
};

export default AuthGuard;