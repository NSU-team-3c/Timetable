import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, TextField, FormControl, InputLabel, Select, MenuItem, FormHelperText, Autocomplete, Chip, List, ListItem, ListItemText } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { mockSubjects } from '../../_mockApis/subjects'; 
import { mockAvailableGroups } from '../../_mockApis/groups';

interface Subject {
  id: number;
  name: string;
  code: string;
  description: string;
  duration: number;
  audienceType: string, 
  groups: number[]
}

const SubjectListPage: React.FC = () => {
  const [subjects, setSubjects] = useState<Subject[]>(mockSubjects);
  const [open, setOpen] = useState(false);
  const [editingSubject, setEditingSubject] = useState<Subject | null>(null);
  const [availableGroups, setAvalilableGroups] = useState<number[]>([]);

  useEffect(() => {
    setTimeout(() => {
      setAvalilableGroups(mockAvailableGroups); 
    }, 1000);
  }, []);

  const formik = useFormik({
    initialValues: {
      name: editingSubject?.name || '',
      code: editingSubject?.code || '',
      description: editingSubject?.description || '',
      duration: editingSubject?.duration || 0,
      audienceType: editingSubject?.audienceType || 'common', 
      groups: editingSubject?.groups || [],
    },
    validationSchema: yup.object({
      name: yup.string().required('Название курса обязательно'),
      code: yup.string().required('Код курса обязательно'),
      description: yup.string().required('Описание курса обязательно'),
      duration: yup.number().required('Продолжительность курса обязательна').min(1, 'Продолжительность должна быть больше 0'),
      audienceType: yup.string(),
      groups: yup.array().min(1, 'Необходимо выбрать хотя бы одну группу').required('Группа обязательна'), 
    }),
    onSubmit: (values) => {
      if (editingSubject) {
        setSubjects(subjects.map(subject => subject.id === editingSubject.id ? { ...subject, ...values } : subject));
      } else {
        const newSubject: Subject = {
          id: subjects[subjects.length - 1].id + 1, 
          ...values,
        };
        setSubjects([...subjects, newSubject]);
      }
      formik.resetForm();
      setEditingSubject(null);
      setOpen(false);
    },
  });

  const handleDelete = (id: number) => {
    setSubjects(subjects.filter(subject => subject.id !== id));
  };

  const handleOpenDialog = (subject?: Subject) => {
    setEditingSubject(subject || null);
    setOpen(true);
  };

  const handleCloseDialog = () => {
    setOpen(false);
    formik.resetForm();
    setEditingSubject(null);
  };

    const list = (groups: number[]) =>  {
        return (
        <List>
            {groups.map((number) => (
              <ListItem key={number} sx={{padding: 0}}>
                <ListItemText primary={number.toString()} />
              </ListItem>
            ))}
        </List>
        )
    }

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>Список учебных курсов</Typography>
      <Button variant="contained" color="primary" onClick={() => handleOpenDialog()}>
        Добавить курс
      </Button>

      {/* Таблица с курсами */}
      <TableContainer component={Paper} sx={{ marginTop: 3 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Название курса</strong></TableCell>
              <TableCell><strong>Код курса</strong></TableCell>
              <TableCell><strong>Описание</strong></TableCell>
              <TableCell><strong>Группы</strong></TableCell>
              <TableCell><strong>Тип аудитории</strong></TableCell>
              <TableCell><strong>Продолжительность (часы)</strong></TableCell>
              <TableCell><strong>Действия</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {subjects.map((subject) => (
              <TableRow key={subject.id}>
                <TableCell>{subject.name}</TableCell>
                <TableCell>{subject.code}</TableCell>
                <TableCell>{subject.description}</TableCell>
                <TableCell>{list(subject.groups)}</TableCell>
                <TableCell>{subject.audienceType}</TableCell>
                <TableCell>{subject.duration}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog(subject)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="secondary" onClick={() => handleDelete(subject.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Диалоговое окно для добавления/редактирования курса */}
      <Dialog open={open} onClose={handleCloseDialog}>
        <DialogTitle>{editingSubject ? 'Редактировать курс' : 'Добавить курс'}</DialogTitle>
        <DialogContent>
          <form onSubmit={formik.handleSubmit}>
            <TextField
              label="Название курса"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('name')}
              error={formik.touched.name && Boolean(formik.errors.name)}
              helperText={formik.touched.name && formik.errors.name}
            />
            <TextField
              label="Код курса"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('code')}
              error={formik.touched.code && Boolean(formik.errors.code)}
              helperText={formik.touched.code && formik.errors.code}
            />
            <TextField
              label="Описание курса"
              fullWidth
              margin="normal"
              variant="outlined"
              {...formik.getFieldProps('description')}
              error={formik.touched.description && Boolean(formik.errors.description)}
              helperText={formik.touched.description && formik.errors.description}
            />

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
            
            <TextField
              label="Продолжительность (часы)"
              fullWidth
              margin="normal"
              variant="outlined"
              type="number"
              {...formik.getFieldProps('duration')}
              error={formik.touched.duration && Boolean(formik.errors.duration)}
              helperText={formik.touched.duration && formik.errors.duration}
            />
            <DialogActions>
              <Button onClick={handleCloseDialog} color="secondary">
                Отмена
              </Button>
              <Button type="submit" color="primary" variant="contained">
                {editingSubject ? 'Сохранить' : 'Добавить'}
              </Button>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default SubjectListPage;
