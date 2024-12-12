import React from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography } from '@mui/material';
import { Register } from '../../../types/auth/auth';


const RegisterForm: React.FC = () => {

  const formik = useFormik<Register>({
    initialValues: {
      email: '',
      password: '',
      confirmPassword: '',
    },
    validationSchema: yup.object({
      email: yup.string().email('Неверный формат почты')
                    .required('Почта обязательна'),
      password: yup.string().min(6, 'Пароль должен содержать минимум 6 символов')
                    .required('Пароль обязателен'),
      confirmPassword: yup.string().oneOf([yup.ref('password')], 'Пароли не совпадают')
                    .required('Подтверждение пароля обязательно'),
    }),
    onSubmit: (values: Register) => {
      console.log('Форма отправлена', values);

      // Здесь можно отправить запрос на сервер для регистрации пользователя

    },
  });

  return (
    <Box sx={{ width: 400, margin: '0 auto', padding: 2 }}>
      <Typography variant="h5" mb={2} textAlign="center">
        Регистрация
      </Typography>
      <form onSubmit={formik.handleSubmit}>
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

        {/* Поле confirm password */}
        <TextField
            label="Подтвердите пароль"
            fullWidth
            variant="outlined"
            margin="normal"
            type="password"
            {...formik.getFieldProps('confirmPassword')}
            error={formik.touched.confirmPassword && Boolean(formik.errors.confirmPassword)}
            helperText={formik.touched.confirmPassword && formik.errors.confirmPassword}
        />

        {/* Кнопка отправки */}
        <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            disabled={formik.isSubmitting}
            sx={{ marginTop: 2 }}
        >
          Зарегистрироваться
        </Button>

      </form>
    </Box>
  );
};

export default RegisterForm;
