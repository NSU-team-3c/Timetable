// src/components/LoginForm.tsx
import React from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography} from '@mui/material';

interface ForgotPasswordValues {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

const ChangePasswordForm: React.FC = () => {

  const formik = useFormik<ForgotPasswordValues>({
    initialValues: {
        oldPassword:  '',
        newPassword:  '',
        confirmPassword: '',
    },
    validationSchema: yup.object({
        oldPassword: yup.string().min(6, 'Пароль должен содержать минимум 6 символов')
                    .required('Старый пароль обязателен'),
        newPassword: yup.string().min(6, 'Пароль должен содержать минимум 6 символов')
                    .required('Новый пароль обязателен'),
        confirmPassword: yup.string().oneOf([yup.ref('newPassword')], 'Пароли не совпадают')
                    .required('Подтверждение пароля обязательно'),

    }),
    onSubmit: (values: any) => {
      console.log('Форма отправлена', values);

      // Здесь можно отправить запрос на сервер для изменения пароля

    },
  });

  return (
    <Box sx={{ width: 400, margin: '0 auto', padding: 2 }}>
      <Typography variant="h5" mb={2} textAlign="center">
        Восстановление пароля
      </Typography>
      <form onSubmit={formik.handleSubmit}>
        {/* Поле oldPassword */}
        <TextField
            label="Старый пароль"
            fullWidth
            variant="outlined"
            margin="normal"
            type="oldPassword"
            {...formik.getFieldProps('oldPassword')}
            error={formik.touched.oldPassword && Boolean(formik.errors.oldPassword)}
            helperText={formik.touched.oldPassword && formik.errors.oldPassword}
        />

        <TextField
            label="Новый пароль"
            fullWidth
            variant="outlined"
            margin="normal"
            type="newPassword"
            {...formik.getFieldProps('newPassword')}
            error={formik.touched.newPassword && Boolean(formik.errors.newPassword)}
            helperText={formik.touched.newPassword && formik.errors.newPassword}
        />

        <TextField
            label="Подтвердите новый пароль"
            fullWidth
            variant="outlined"
            margin="normal"
            type="confirmPassword"
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
           Изменить пароль
        </Button>



      </form>
    </Box>
  );
};

export default ChangePasswordForm;
