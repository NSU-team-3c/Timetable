import React, { useEffect } from 'react';
import { Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, TextField, FormControl, InputLabel, Select, MenuItem, FormHelperText, CircularProgress } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { useSelector } from 'react-redux';
import { fetchAuditories, createAuditory, updateAuditory, deleteAuditory } from '../../store/application/room/auditorySlice'; 
import { AppState, dispatch } from '../../store/Store';  

interface Auditory {
  id: number;
  number: string;
  type: string;
  capacity: number;
}

const ClassroomList: React.FC = () => {
  const { auditories, loading, error } = useSelector((state: AppState) => state.rooms);  
  const [open, setOpen] = React.useState(false);
  const [editingAuditory, setEditingAuditory] = React.useState<Auditory | null>(null);

  useEffect(() => {
    dispatch(fetchAuditories()); 
  }, [dispatch]);

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
        dispatch(updateAuditory({ ...editingAuditory, ...values }));  
      } else {
        dispatch(createAuditory({ id: Date.now(), ...values }));  
      }
      formik.resetForm();
      setEditingAuditory(null);
      setOpen(false);
    },
    enableReinitialize: true,  
  });

  const handleDelete = (id: number) => {
    dispatch(deleteAuditory(id));  
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
              <Select label="Тип аудитории" {...formik.getFieldProps('type')}>
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

      {/* Индикатор загрузки */}
      {loading && (
        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: 3 }}>
          <CircularProgress />
        </Box>
      )}

      {/* Ошибка при загрузке */}
      {error && (
        <Typography variant="body1" color="error" sx={{ marginTop: 2 }}>
          Ошибка: {error}
        </Typography>
      )}
    </Box>
  );
};

export default ClassroomList;
