import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { AppState, dispatch, useSelector } from '../../store/Store';
import { createProfessor, fetchProfessors, deleteProfessor } from '../../store/application/professor/professorSlice';

export interface Professor {
  id: number;
  name: string;
  surname: string;
  email: string;
}

const ProfessorList: React.FC = () => {
  const [open, setOpen] = useState(false);
  const { professors } = useSelector((state: AppState) => state.professors);  

  useEffect(() => {
    dispatch(fetchProfessors()); 
  }, [dispatch]);

  const formik = useFormik({
    initialValues: {
      name: '',
      surname: '',
      email: '',
      password: '',
    },
    validationSchema: yup.object({
      name: yup.string().required('Имя обязательно'),
      surname: yup.string().required('Фамилия обязательна'),
      email: yup.string().email('Неверный формат почты').required('Почта обязательна'),
      password: yup.string().min(6, 'Пароль должен быть не менее 6 символов').required('Пароль обязателен'),
    }),
    onSubmit: (values) => {
      const newProfessor: Professor = {
        id: new Date().getTime(), 
        ...values,
      };
      dispatch(createProfessor(newProfessor));
      formik.resetForm();
      setOpen(false);
    },
  });

  const handleDelete = (id: number) => {
    dispatch(deleteProfessor(id)); 
  };

  const handleOpenDialog = () => {
    setOpen(true);
  };

  const handleCloseDialog = () => {
    setOpen(false);
    formik.resetForm();
  };

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>Список преподавателей</Typography>
      <Button variant="contained" color="primary" onClick={handleOpenDialog}>
        Добавить преподавателя
      </Button>

      {/* Таблица с преподавателями */}
      <TableContainer component={Paper} sx={{ marginTop: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Имя</strong></TableCell>
              <TableCell><strong>Фамилия</strong></TableCell>
              <TableCell><strong>Email</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {professors.map((professor) => (
              <TableRow key={professor.id}>
                <TableCell>{professor.name}</TableCell>
                <TableCell>{professor.surname}</TableCell>
                <TableCell>{professor.email}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Диалоговое окно для добавления преподавателя */}
      <Dialog open={open} onClose={handleCloseDialog}>
        <DialogTitle>Добавить преподавателя</DialogTitle>
        <DialogContent>
          <form onSubmit={formik.handleSubmit}>
            <TextField
              label="Имя"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('name')}
              error={formik.touched.name && Boolean(formik.errors.name)}
              helperText={formik.touched.name && formik.errors.name}
            />
            <TextField
              label="Фамилия"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('surname')}
              error={formik.touched.surname && Boolean(formik.errors.surname)}
              helperText={formik.touched.surname && formik.errors.surname}
            />
            <TextField
              label="Email"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('email')}
              error={formik.touched.email && Boolean(formik.errors.email)}
              helperText={formik.touched.email && formik.errors.email}
            />
            <TextField
              label="Password"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('password')}
              error={formik.touched.password && Boolean(formik.errors.password)}
              helperText={formik.touched.password && formik.errors.password}
            />
            <DialogActions>
              <Button onClick={handleCloseDialog} color="secondary">
                Отмена
              </Button>
              <Button type="submit" color="primary" variant="contained">
                Добавить
              </Button>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default ProfessorList;
