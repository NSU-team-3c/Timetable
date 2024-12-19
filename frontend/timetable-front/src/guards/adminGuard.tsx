import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AdminGuard = ({ children }: any) => {
    const navigate  = useNavigate();
    const authToken = sessionStorage.getItem('authToken');
    const role      = sessionStorage.getItem('role')

    useEffect(() => {
        if (!authToken) {
            navigate('/auth/login', { replace: true });
        } else if (role !== 'adminUser') {
            navigate('/profile', { replace: true });
        }
    }, [authToken, navigate]);

    return children;
};

export default AdminGuard;