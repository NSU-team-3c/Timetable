import React, { useEffect, useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography } from '@mui/material';
import { ProfessorFormValues } from '../../../types/user/user';
import { Link } from 'react-router-dom';


const ProfessorForm: React.FC = () => {
  const [profileData, setProfileData] = useState<ProfessorFormValues | null>(null);

  useEffect(() => {
    // setTimeout(() => {
    //   setProfileData(mockProfileData);

    //   if (mockProfileData.photo) {
    //     setPhotoPreview(URL.createObjectURL(mockProfileData.photo));
    //   }
    // }, 1000);
  }, []);

  const formik = useFormik<ProfessorFormValues>({
    enableReinitialize: true,
    initialValues: profileData || {
      organisation: '',
      education: '',
      courses: '',
      specialization: '',
    },

    validationSchema: yup.object({
      organisation: yup.string(),
      education: yup.string(),
      courses: yup.string(),
      specialization: yup.string(),
    }),

    onSubmit: async (values) => {
      console.log('Форма отправлена', values);
    },
  });

  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2, position: 'relative' }}>
      <form onSubmit={formik.handleSubmit}>
        {/* Основная форма */}
        <Box display="flex" flexDirection="column" gap={2}>
          <TextField
            label="Организация"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('organisation')}
            error={formik.touched.organisation && Boolean(formik.errors.organisation)}
            helperText={formik.touched.organisation && formik.errors.organisation}
          />

          <TextField
            label="Образование"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('education')}
            error={formik.touched.education && Boolean(formik.errors.education)}
            helperText={formik.touched.education && formik.errors.education}
          />

          <TextField
            label="Курсы"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('courses')}
            error={formik.touched.courses && Boolean(formik.errors.courses)}
            helperText={formik.touched.courses && formik.errors.courses}
          />


          <TextField
            label="Специализация"
            fullWidth
            variant="outlined"
            margin="normal"
            {...formik.getFieldProps('specialization')}
            error={formik.touched.specialization && Boolean(formik.errors.specialization)}
            helperText={formik.touched.specialization && formik.errors.specialization}
          />

          <Link to="/profile/professor/course" style={{ textDecoration: 'none', display: 'block', marginTop: 8 }}>
            <Typography color="primary">Создать курс {'>'}</Typography>
          </Link>

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

        </Box>
      </form>
    </Box>
  );
};

export default ProfessorForm;