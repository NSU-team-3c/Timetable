import React from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { ForgotPassword } from '../../../types/auth/auth';

const ForgotPasswordForm: React.FC = () => {

  const formik = useFormik<ForgotPassword>({
    initialValues: {
      email: '',
    },
    validationSchema: yup.object({
      email: yup.string().email('Неверный формат почты')
                    .required('Почта обязательна'),
    }),
    onSubmit: (values: ForgotPassword) => {
      console.log('Форма отправлена', values);

      // Здесь можно отправить запрос на сервер для восстановления пароля

    },
  });

  return (
    <Box sx={{ width: 400, margin: '0 auto', padding: 2 }}>
      <Typography variant="h5" mb={2} textAlign="center">
        Восстановление пароля
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

        {/* Кнопка отправки */}
        <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            disabled={formik.isSubmitting}
            sx={{ marginTop: 2 }}
        >
          Отправить пароль
        </Button>

        <Button color="primary" fullWidth component={Link} to="/auth/login">
            Я вспомнил пароль!
        </Button>

      </form>
    </Box>
  );
};

export default ForgotPasswordForm;
