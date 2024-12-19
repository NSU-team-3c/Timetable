import React, { useEffect, useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, Typography, FormControl, InputLabel, Select, MenuItem, FormHelperText, Chip, Autocomplete } from '@mui/material';
import { Link } from 'react-router-dom';
import { mockCourseData } from '../../../_mockApis/course'; 
import { CourseFormValues } from '../../../types/courses/course'; 
import { mockAvailableGroups } from '../../../_mockApis/groups';

const CreateCourseForm: React.FC = () => {
  const [courseData, setCourseData] = useState<CourseFormValues | null>(null);
  const [availableGroups, setAvalilableGroups] = useState<number[]>([]);

  useEffect(() => {
    setTimeout(() => {
      setCourseData(mockCourseData); 
    }, 1000);
  }, []);

  useEffect(() => {
    setTimeout(() => {
      setAvalilableGroups(mockAvailableGroups); 
    }, 1000);
  }, []);


  const formik = useFormik<CourseFormValues>({
    enableReinitialize: true,
    initialValues: courseData || {
      name: '',
      code: '',
      description: '',
      professor: '',
      duration: 0,
      audienceType: '', 
      groups: [], 
    },

    validationSchema: yup.object({
      name: yup.string().required('Название курса обязательно'),
      code: yup.string().required('Код курса обязателен'),
      description: yup.string().min(10, 'Описание должно содержать минимум 10 символов').required('Описание обязательно'),
      professor: yup.string().required('Имя преподавателя обязательно'),
      duration: yup.number().min(1, 'Продолжительность курса должна быть хотя бы 1 неделя').required('Продолжительность курса обязательна'),
      audienceType: yup.string(), 
      groups: yup.array().min(1, 'Необходимо выбрать хотя бы одну группу').required('Группа обязательна'), 
    }),

    onSubmit: async (values) => {
      console.log('Форма отправлена', values);
      // Отправка данных на сервер
    },
  });

  return (
    <Box sx={{ width: 800, margin: '0 auto', padding: 2 }}>
      <form onSubmit={formik.handleSubmit}>
        {/* Название курса */}
        <TextField
          label="Название курса"
          fullWidth
          variant="outlined"
          margin="normal"
          {...formik.getFieldProps('name')}
          error={formik.touched.name && Boolean(formik.errors.name)}
          helperText={formik.touched.name && formik.errors.name}
        />

        {/* Код курса */}
        <TextField
          label="Код курса"
          fullWidth
          variant="outlined"
          margin="normal"
          {...formik.getFieldProps('code')}
          error={formik.touched.code && Boolean(formik.errors.code)}
          helperText={formik.touched.code && formik.errors.code}
        />

        {/* Описание курса */}
        <TextField
          label="Описание курса"
          fullWidth
          variant="outlined"
          margin="normal"
          multiline
          rows={4}
          {...formik.getFieldProps('description')}
          error={formik.touched.description && Boolean(formik.errors.description)}
          helperText={formik.touched.description && formik.errors.description}
        />

        {/* Преподаватель */}
        <TextField
          label="Преподаватель"
          fullWidth
          variant="outlined"
          margin="normal"
          {...formik.getFieldProps('professor')}
          error={formik.touched.professor && Boolean(formik.errors.professor)}
          helperText={formik.touched.professor && formik.errors.professor}
        />

        {/* Продолжительность курса */}
        <TextField
          label="Продолжительность (в неделях)"
          fullWidth
          variant="outlined"
          margin="normal"
          type="number"
          {...formik.getFieldProps('duration')}
          error={formik.touched.duration && Boolean(formik.errors.duration)}
          helperText={formik.touched.duration && formik.errors.duration}
        />

        {/* Тип аудитории */}
        <FormControl fullWidth margin="normal" error={formik.touched.audienceType && Boolean(formik.errors.audienceType)}>
          <InputLabel>Тип аудитории</InputLabel>
          <Select
            label="Тип аудитории"
            {...formik.getFieldProps('audienceType')}
          >
            <MenuItem value="online">Онлайн</MenuItem>
            <MenuItem value="computer">Компьютерный</MenuItem>
            <MenuItem value="lecture">Лекционный</MenuItem>
            <MenuItem value="common">Обычный</MenuItem>
          </Select>
          {formik.touched.audienceType && formik.errors.audienceType && (
            <FormHelperText>{formik.errors.audienceType}</FormHelperText>
          )}
        </FormControl>

        {/* Группы */}
        <Autocomplete
          multiple
          id="groups"
          options={availableGroups}
          value={formik.values.groups}
          onChange={(_, newValue) => formik.setFieldValue('groups', newValue)}
          renderInput={(params) => (
            <TextField
              {...params}
              label="Группы"
              variant="outlined"
              margin="normal"
              error={formik.touched.groups && Boolean(formik.errors.groups)}
              helperText={formik.touched.groups && formik.errors.groups}
            />
          )}
          renderTags={(value, getTagProps) =>
            value.map((option, index) => (
              <Chip label={option} {...getTagProps({ index })} key={index} sx={{ margin: 0.5 }} />
            ))
          }
        />

        {/* Кнопка отправки формы */}
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
            <Typography color="primary">Вернуться на страницу преподавателя {'>'}</Typography>
          </Link>
        </Box>
      </form>
    </Box>
  );
};

export default CreateCourseForm;
