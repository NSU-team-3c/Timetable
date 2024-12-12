import React, { useState } from 'react';
import { Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { mockGroups } from '../../_mockApis/groupList'; 

interface Group {
  id: number;
  number: number | string;
  course: number;
  department: string;
  capacity: number
}

const GroupList: React.FC = () => {
  const [groups, setGroups] = useState<Group[]>(mockGroups);
  const [open, setOpen] = useState(false);
  const [editingGroup, setEditingGroup] = useState<Group | null>(null);

  const formik = useFormik({
    initialValues: {
      number: editingGroup?.number || '',
      course: editingGroup?.course || 1,
      department: editingGroup?.department || '',
      capacity: editingGroup?.capacity || 0,
    },
    validationSchema: yup.object({
      number: yup.string().required('Название группы обязательно'),
      course: yup.number().required('Курс обязателен').min(1, 'Курс должен быть больше или равен 1').max(4, 'Курс не может быть больше 4'),
      department: yup.string().required('Отделение обязательно'),
    }),
    onSubmit: (values) => {
      if (editingGroup) {
        setGroups(groups.map(group => group.id === editingGroup.id ? { ...group, ...values } : group));
      } else {
        const newGroup: Group = {
          id: groups[groups.length - 1].id + 1, 
          ...values,
        };
        setGroups([...groups, newGroup]);
      }
      formik.resetForm();
      setEditingGroup(null);
      setOpen(false);
    },
  });

  const handleDelete = (id: number) => {
    setGroups(groups.filter(group => group.id !== id));
  };

  const handleOpenDialog = (group?: Group) => {
    setEditingGroup(group || null);
    setOpen(true);
  };

  const handleCloseDialog = () => {
    setOpen(false);
    formik.resetForm();
    setEditingGroup(null);
  };

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>Список студенческих групп</Typography>
      <Button variant="contained" color="primary" onClick={() => handleOpenDialog()}>
        Добавить группу
      </Button>

      {/* Таблица с группами */}
      <TableContainer component={Paper} sx={{ marginTop: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Название группы</strong></TableCell>
              <TableCell><strong>Курс</strong></TableCell>
              <TableCell><strong>Факультет</strong></TableCell>
              <TableCell><strong>Количество учащихся</strong></TableCell>
              <TableCell><strong>Действия</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {groups.map((group) => (
              <TableRow key={group.id}>
                <TableCell>{group.number}</TableCell>
                <TableCell>{group.course}</TableCell>
                <TableCell>{group.department}</TableCell>
                <TableCell>{group.capacity}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog(group)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="secondary" onClick={() => handleDelete(group.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Диалоговое окно для добавления/редактирования группы */}
      <Dialog open={open} onClose={handleCloseDialog}>
        <DialogTitle>{editingGroup ? 'Редактировать группу' : 'Добавить группу'}</DialogTitle>
        <DialogContent>
          <form onSubmit={formik.handleSubmit}>
            <TextField
              label="Номер группы"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('number')}
              error={formik.touched.number && Boolean(formik.errors.number)}
              helperText={formik.touched.number && formik.errors.number}
            />
            <TextField
              label="Курс"
              fullWidth
              margin="normal"
              variant="outlined"
              type="number"
              {...formik.getFieldProps('course')}
              error={formik.touched.course && Boolean(formik.errors.course)}
              helperText={formik.touched.course && formik.errors.course}
            />
            <TextField
              label="Факультет"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('department')}
              error={formik.touched.department && Boolean(formik.errors.department)}
              helperText={formik.touched.department && formik.errors.department}
            />
            <TextField
              label="Количество учащихся"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('capacity')}
              error={formik.touched.capacity && Boolean(formik.errors.capacity)}
              helperText={formik.touched.capacity && formik.errors.capacity}
            />
            <DialogActions>
              <Button onClick={handleCloseDialog} color="secondary">
                Отмена
              </Button>
              <Button type="submit" color="primary" variant="contained">
                {editingGroup ? 'Сохранить' : 'Добавить'}
              </Button>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default GroupList;
