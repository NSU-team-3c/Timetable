import React, { useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import { ChangePasswordValues } from '../../../types/user/user' 

const ChangePasswordForm: React.FC = () => {
  const [isDialogOpen, setDialogOpen] = useState(false);
  const [isCodeSent, setCodeSent] = useState(false);

  const formik = useFormik<ChangePasswordValues>({
    initialValues: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
      confirmationCode: '', 
    },
    validationSchema: yup.object({
      oldPassword: yup.string().min(6, 'Пароль должен содержать минимум 6 символов').required('Старый пароль обязателен'),
      newPassword: yup.string().min(6, 'Пароль должен содержать минимум 6 символов').required('Новый пароль обязателен'),
      confirmPassword: yup.string().oneOf([yup.ref('newPassword')], 'Пароли не совпадают').required('Подтверждение пароля обязательно'),
      confirmationCode: isCodeSent ? yup.string().required('Введите код подтверждения') : yup.string(), 
    }),
    onSubmit: async (values: ChangePasswordValues) => {
      setDialogOpen(true); 

      setCodeSent(true); 
    },
  });

  const handleCloseDialog = () => {
    setDialogOpen(false);
  };

  const handleSendConfirmationCode = () => {
    console.log('Код подтверждения отправлен');

    setDialogOpen(false);
    // Отправка кода подтверждения на email
  };

  return (
    <Box sx={{ width: 400, margin: '0 auto', padding: 2 }}>
      <Typography variant="h5" mb={2} textAlign="center">
        Изменить пароль
      </Typography>
      <form onSubmit={formik.handleSubmit}>
        {/* Поле старого пароля */}
        <TextField
          label="Старый пароль"
          fullWidth
          variant="outlined"
          margin="normal"
          type="password"
          {...formik.getFieldProps('oldPassword')}
          error={formik.touched.oldPassword && Boolean(formik.errors.oldPassword)}
          helperText={formik.touched.oldPassword && formik.errors.oldPassword}
        />

        {/* Поле нового пароля */}
        <TextField
          label="Новый пароль"
          fullWidth
          variant="outlined"
          margin="normal"
          type="password"
          {...formik.getFieldProps('newPassword')}
          error={formik.touched.newPassword && Boolean(formik.errors.newPassword)}
          helperText={formik.touched.newPassword && formik.errors.newPassword}
        />

        {/* Подтверждение нового пароля */}
        <TextField
          label="Подтвердите новый пароль"
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
          {isCodeSent ? 'Подтвердить изменение пароля' : 'Отправить код подтверждения'}
        </Button>
      </form>

      {/* Модальное окно для ввода кода подтверждения */}
      <Dialog open={isDialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>Введите код подтверждения</DialogTitle>
        <DialogContent>
          <TextField
            label="Код подтверждения"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('confirmationCode')}
            error={formik.touched.confirmationCode && Boolean(formik.errors.confirmationCode)}
            helperText={formik.touched.confirmationCode && formik.errors.confirmationCode}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="secondary">
            Закрыть
          </Button>
          <Button
            onClick={handleSendConfirmationCode}
            color="primary"
          >
            Подтвердить код
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ChangePasswordForm;