import React, { useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography, FormControl, FormHelperText } from '@mui/material';
import CameraAltIcon from '@mui/icons-material/CameraAlt';
import { Link } from 'react-router-dom'; 

interface ProfileFormValues {
  surname: string;
  name: string;
  patronymic: string;
  birthday: string;
  email: string;
  phone: string;
  about: string;
  photo: File | null;
}

const ProfileForm: React.FC = () => {
  const [photoPreview, setPhotoPreview] = useState<string | null>(null);

  const formik = useFormik<ProfileFormValues>({
    initialValues: {
      surname: '',
      name: '',
      patronymic: '',
      birthday: '',
      email: '',
      phone: '',
      about: '',
      photo: null, // Инициализация photo как null
    },

    validationSchema: yup.object({
      name: yup.string().required('Имя обязательно'),
      surname: yup.string().required('Фамилия обязательна'),
      patronymic: yup.string(),
      birthday: yup.date().required('Дата рождения обязательна'),
      email: yup.string().email('Неверный формат почты').required('Почта обязательна'),
      phone: yup.string(),
      about: yup.string(),
      photo: yup
        .mixed()
        .nullable()
        .notRequired()
        .test('fileSize', 'Размер файла слишком большой', (value) => {
          if (!value) return true; // Если фото не выбрано, пропускаем проверку
          if (value instanceof File) {
            return value.size <= 5000000; // Максимальный размер файла 5MB
          }
          return false;
        })
        .test('fileType', 'Неверный формат файла', (value) => {
          if (!value) return true; // Если фото не выбрано, пропускаем проверку
          if (value instanceof File) {
            return ['image/jpeg', 'image/png', 'image/gif'].includes(value.type);
          }
          return false;
        }),
    }),

    onSubmit: async (values) => {
      console.log('Форма отправлена', values);
    },
  });

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files ? event.target.files[0] : null;
    if (file) {
      // Создание предпросмотра изображения
      setPhotoPreview(URL.createObjectURL(file));
      formik.setFieldValue('photo', file); // Устанавливаем файл в состояние Formik
    }
  };

  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2, position: 'relative' }}>
      <form onSubmit={formik.handleSubmit}>
        {/* Поле для загрузки фотографии в правом верхнем углу */}
        <Box
          sx={{
            position: 'absolute',
            top: 0,
            right: 0,
            zIndex: 1, // чтобы фото было поверх других элементов
          }}
        >
          <FormControl fullWidth margin="normal">
            <input
              type="file"
              accept="image/*"
              id="photo-upload"
              style={{ display: 'none' }} // Скрываем стандартный инпут
              onChange={handleFileChange}
            />
            <label htmlFor="photo-upload">
              <Box
                sx={{
                  width: 150,
                  height: 165,
                  borderRadius: '5%', // Округлая форма
                  border: '2px solid #ddd',
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  backgroundColor: '#f0f0f0',
                  cursor: 'pointer',
                  position: 'relative',
                }}
              >
                {photoPreview ? (
                  <img
                    src={photoPreview}
                    alt="Preview"
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover',
                      borderRadius: '50%',
                    }}
                  />
                ) : (
                  <CameraAltIcon sx={{ fontSize: 30, color: '#555' }} />
                )}
              </Box>
            </label>
            {formik.touched.photo && formik.errors.photo && (
              <FormHelperText error>{formik.errors.photo}</FormHelperText>
            )}
          </FormControl>
        </Box>

        {/* Основная форма */}
        <Box display="flex" flexDirection="column" gap={2}>
          <TextField
            label="Фамилия"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('surname')}
            error={formik.touched.surname && Boolean(formik.errors.surname)}
            helperText={formik.touched.surname && formik.errors.surname}
          />

          <TextField
            label="Имя"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('name')}
            error={formik.touched.name && Boolean(formik.errors.name)}
            helperText={formik.touched.name && formik.errors.name}
          />

          <TextField
            label="Отчество"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('patronymic')}
            error={formik.touched.patronymic && Boolean(formik.errors.patronymic)}
            helperText={formik.touched.patronymic && formik.errors.patronymic}
          />

          <TextField
            label="Дата рождения"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('birthday')}
            error={formik.touched.birthday && Boolean(formik.errors.birthday)}
            helperText={formik.touched.birthday && formik.errors.birthday}
            placeholder="ДД.ММ.ГГГГ"
          />

          <TextField
            label="Электронная почта"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('email')}
            error={formik.touched.email && Boolean(formik.errors.email)}
            helperText={formik.touched.email && formik.errors.email}
          />

          <TextField
            label="Номер телефона"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('phone')}
            error={formik.touched.phone && Boolean(formik.errors.phone)}
            helperText={formik.touched.phone && formik.errors.phone}
          />

          <Link to="/profile/change-password" style={{ color: '#1976d2', textAlign: 'left', display: 'block' }}>
            Изменить пароль {'>'}
          </Link>

          <TextField
            label="О себе"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('about')}
            error={formik.touched.about && Boolean(formik.errors.about)}
            helperText={formik.touched.about && formik.errors.about}
          />
        </Box>
        <Box mt={2}>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            disabled={formik.isSubmitting}
          >
            Сохранить
          </Button>

          <Link to="/profile/professor" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
            <Typography color="primary">Я преподаватель {'>'}</Typography>
          </Link>
        </Box>
      </form>
    </Box>
  );
};

export default ProfileForm;
