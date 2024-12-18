import React, { useState } from 'react';
import { Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { mockProfessors } from '../../_mockApis/professorList';  
import { dispatch } from '../../store/Store';
import { createProfessor } from '../../store/application/professor/professorSlice';

export interface Professor {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

const ProfessorList: React.FC = () => {
  const [professors, setProfessors] = useState<Professor[]>(mockProfessors);
  const [open, setOpen] = useState(false);

  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
    },
    validationSchema: yup.object({
      firstName: yup.string().required('Имя обязательно'),
      lastName: yup.string().required('Фамилия обязательна'),
      email: yup.string().email('Неверный формат почты').required('Почта обязательна'),
      password: yup.string().min(6, 'Пароль должен собрать не менее 6 символов').required('Пароль обязателен'),
    }),
    onSubmit: (values) => {

      const newProfessor: Professor = {
        id: professors[professors.length - 1].id + 1, 
        ...values,
      };

      dispatch(createProfessor(newProfessor));
      setProfessors([...professors, newProfessor]);
      formik.resetForm();
      setOpen(false);
    },
  });

  const handleDelete = (id: number) => {
    console.log(id);
    setProfessors(professors.filter(professor => professor.id !== id));
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
              <TableCell><strong>Роль</strong></TableCell>
              <TableCell><strong>Действия</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {professors.map((professor) => (
              <TableRow key={professor.id}>
                <TableCell>{professor.firstName}</TableCell>
                <TableCell>{professor.lastName}</TableCell>
                <TableCell>{professor.email}</TableCell>
                <TableCell>
                  <IconButton color="secondary" onClick={() => handleDelete(professor.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
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
              {...formik.getFieldProps('firstName')}
              error={formik.touched.firstName && Boolean(formik.errors.firstName)}
              helperText={formik.touched.firstName && formik.errors.firstName}
            />
            <TextField
              label="Фамилия"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('lastName')}
              error={formik.touched.lastName && Boolean(formik.errors.lastName)}
              helperText={formik.touched.lastName && formik.errors.lastName}
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
