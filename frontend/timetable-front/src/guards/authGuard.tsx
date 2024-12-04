import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthGuard = ({ children }: any) => {
    const navigate = useNavigate();
    const authToken = localStorage.getItem('authToken');

    useEffect(() => {
        if (!authToken) {
            navigate('/auth/login', { replace: true });
        }
    }, [authToken, navigate]);

    return children;
};

export default AuthGuard;