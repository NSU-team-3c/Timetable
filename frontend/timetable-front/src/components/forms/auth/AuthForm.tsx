import React from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography, FormControlLabel, Checkbox, Stack } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { Login } from '../../../types/auth/auth';
import { dispatch } from '../../../store/Store';
import { login } from '../../../store/auth/authSlice';

const LoginForm: React.FC = () => {
  const navigate = useNavigate();
  const formik = useFormik<Login>({
    initialValues: {
      email: '',
      password: '',
    },
    validationSchema: yup.object({
      email: yup.string().email('Неверный формат почты').required('Почта обязательна'),
      password: yup.string().min(6, 'Пароль должен содержать минимум 6 символов').required('Пароль обязателен'),
    }),
    onSubmit: async (values) => {
      //console.log('Форма отправлена', values);

      const resultAction = await dispatch(login(values)); 

      if (login.fulfilled.match(resultAction)) {
        const result = resultAction.payload;  
        if (result.access_token) {
          sessionStorage.setItem('authToken', result.access_token);
          
          navigate('/profile');
        }

      } else {
        formik.setErrors({ email: 'Неверная почта или пароль' });
      }
        
    },
  });

  return (
    <Box sx={{ width: '400px', margin: '0 auto', padding: 2 }}>
      <Typography variant="h5" mb={2} textAlign="center">
        Вход
      </Typography>
      <form onSubmit={formik.handleSubmit}>
        <Box>
            {/* Поле email */}
            <TextField
                label="Эл. почта"
                fullWidth
                variant="outlined"
                margin="normal"
                type="email"
                {...formik.getFieldProps('email')}
                error={formik.touched.email && Boolean(formik.errors.email)}
                helperText={formik.touched.email && formik.errors.email}
            />

            {/* Поле password */}
            <TextField
                label="Пароль"
                fullWidth
                variant="outlined"
                margin="normal"
                type="password"
                {...formik.getFieldProps('password')}
                error={formik.touched.password && Boolean(formik.errors.password)}
                helperText={formik.touched.password && formik.errors.password}
            />

            
            {/* Ссылка на восстановление пароля */}
            <Link to="/auth/forgot-password" style={{ color: '#1976d2'}}>
                Забыли пароль?
            </Link>

            
            {/* Кнопка отправки */}
            <Box mt={2}>
            <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                disabled={formik.isSubmitting}
            >
                Войти
            </Button>
            </Box>

            {/* Ссылка на регистрацию */}
            <Stack direction="row" justifyContent="center" spacing={1} mt={2}>
            <Typography color="textSecondary">
                Нет аккаунта?
            </Typography>
            <Link to="/auth/register" style={{ textDecoration: 'none' }}>
                <Typography color="primary">
                Зарегистрироваться
                </Typography>
            </Link>
            </Stack>
        </Box>
      </form>
    </Box>
  );
};

export default LoginForm;
