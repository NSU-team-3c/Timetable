import React, { useState } from 'react';
import { Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, TextField, FormControl, InputLabel, Select, MenuItem, FormHelperText } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { mockAuditories } from '../../_mockApis/auditorues'; 

interface Auditory {
  id: number;
  number: string;
  type: string;
  capacity: number;
}

const ClassroomList: React.FC = () => {
  const [auditories, setAuditories] = useState<Auditory[]>(mockAuditories);
  const [open, setOpen] = useState(false);
  const [editingAuditory, setEditingAuditory] = useState<Auditory | null>(null);
  
  const formik = useFormik({
    initialValues: {
      number: editingAuditory?.number || '',
      type: editingAuditory?.type || '',
      capacity: editingAuditory?.capacity || 0,
    },
    validationSchema: yup.object({
      number: yup.string().required('Номер аудитории обязателен'),
      type: yup.string().required('Здание обязательно'),
      capacity: yup.number().required('Вместимость обязательна').min(1, 'Вместимость должна быть больше 0'),
    }),
    onSubmit: (values) => {
      if (editingAuditory) {
        setAuditories(auditories.map(auditory => auditory.id === editingAuditory.id ? { ...auditory, ...values } : auditory));
      } else {
        const newAuditory: Auditory = {
          id: auditories[auditories.length - 1].id + 1, 
          ...values,
        };
        setAuditories([...auditories, newAuditory]);
      }
      formik.resetForm();
      setEditingAuditory(null);
      setOpen(false);
    },
  });

  const handleDelete = (id: number) => {
    setAuditories(auditories.filter(auditory => auditory.id !== id));
  };

  const handleOpenDialog = (auditory?: Auditory) => {
    setEditingAuditory(auditory || null);
    setOpen(true);
  };

  const handleCloseDialog = () => {
    setOpen(false);
    formik.resetForm();
    setEditingAuditory(null);
  };

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>Список аудиторий</Typography>
      <Button variant="contained" color="primary" onClick={() => handleOpenDialog()}>
        Добавить аудиторию
      </Button>

      {/* Таблица с аудиториями */}
      <TableContainer component={Paper} sx={{ marginTop: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Номер аудитории</strong></TableCell>
              <TableCell><strong>Тип</strong></TableCell>
              <TableCell><strong>Вместимость</strong></TableCell>
              <TableCell><strong>Действия</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {auditories.map((auditory) => (
              <TableRow key={auditory.id}>
                <TableCell>{auditory.number}</TableCell>
                <TableCell>{auditory.type}</TableCell>
                <TableCell>{auditory.capacity}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog(auditory)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="secondary" onClick={() => handleDelete(auditory.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Диалоговое окно для добавления/редактирования аудитории */}
      <Dialog open={open} onClose={handleCloseDialog}>
        <DialogTitle>{editingAuditory ? 'Редактировать аудиторию' : 'Добавить аудиторию'}</DialogTitle>
        <DialogContent>
          <form onSubmit={formik.handleSubmit}>
            <TextField
              label="Номер аудитории"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('number')}
              error={formik.touched.number && Boolean(formik.errors.number)}
              helperText={formik.touched.number && formik.errors.number}
            />
            <FormControl fullWidth margin="normal" error={formik.touched.type && Boolean(formik.errors.type)}>
                <InputLabel>Тип аудитории</InputLabel>
                <Select
                    label="Тип аудитории"
                    {...formik.getFieldProps('type')}
                >
                    <MenuItem value="computer">Компьютерный класс</MenuItem>
                    <MenuItem value="lecture">Лекционная аудитория</MenuItem>
                    <MenuItem value="common">Обычный класс</MenuItem>
                </Select>
                {formik.touched.type && formik.errors.type && (
                    <FormHelperText>{formik.errors.type}</FormHelperText>
                )}
            </FormControl>
            <TextField
              label="Вместимость"
              fullWidth
              margin="normal"
              variant="outlined"
              type="number"
              {...formik.getFieldProps('capacity')}
              error={formik.touched.capacity && Boolean(formik.errors.capacity)}
              helperText={formik.touched.capacity && formik.errors.capacity}
            />
            <DialogActions>
              <Button onClick={handleCloseDialog} color="secondary">
                Отмена
              </Button>
              <Button type="submit" color="primary" variant="contained">
                {editingAuditory ? 'Сохранить' : 'Добавить'}
              </Button>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default ClassroomList;
