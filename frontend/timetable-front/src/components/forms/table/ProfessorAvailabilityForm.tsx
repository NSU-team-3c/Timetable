import React, { useState, useCallback } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography, DialogContentText } from '@mui/material';
import 'react-big-calendar/lib/css/react-big-calendar.css';

// Локализатор для moment
const localizer = momentLocalizer(moment);

interface Availability {
  start: Date;
  end: Date;
}

const TeacherAvailabilityForm: React.FC = () => {
  const [events, setEvents] = useState<Availability[]>([]); 
  const [open, setOpen] = useState(false); 
  const [currentEvent, setCurrentEvent] = useState<Availability | null>(null); 
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [eventToDelete, setEventToDelete] = useState<Availability | null>(null);

  const handleSelectSlot = useCallback((slotInfo: any) => {
    if (slotInfo.start.getHours() !== 0) { 
      setCurrentEvent({
        start: slotInfo.start,
        end: slotInfo.end,
      });
      setOpen(true);
    }
  }, []);

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = () => {
    if (currentEvent) {
      // Check if the new event overlaps with any existing events
      const overlap = events.some(
        (event) =>
          (event.start < currentEvent.end && event.end > currentEvent.start)
      );
      if (!overlap) {
        setEvents([...events, currentEvent]);
        setOpen(false);
        setCurrentEvent(null);
      } else {
        alert('This time slot overlaps with an existing availability!');
      }
    }
  };

  const handleDelete = (eventToDelete: Availability) => {
    setEvents(events.filter(event => event !== eventToDelete));
    setOpenDeleteDialog(false);
  };

  const handleDeleteDialogOpen = (event: Availability) => {
    setEventToDelete(event);
    setOpenDeleteDialog(true);
  };

  const handleDeleteDialogClose = () => {
    setOpenDeleteDialog(false);
  };

  const CustomWeekHeader = ({ label }: { label: string }) => (
    <Box sx={{ textAlign: 'center', fontWeight: 'bold' }}>
      {label.split(" ")[1]}
    </Box>
  );

  return (
    <Box>
      <Typography variant="h4" gutterBottom align="center">
        Управление временем доступным для преподавания
      </Typography>

      <Calendar
        localizer={localizer}
        events={events}
        startAccessor="start"
        endAccessor="end"
        selectable
        onSelectSlot={handleSelectSlot}
        defaultView="week"
        views={['week']}
        step={45}
        min={new Date(2024, 9, 19, 9, 0)} 
        max={new Date(2029, 5, 19, 20, 0)}
        toolbar={false}
        components={{
          week: {
            header: CustomWeekHeader,
          },
        }}
      />

      {/* Модальное окно для добавления интервала */}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Добавить свободное время</DialogTitle>
        <DialogContent>
          <Typography variant="body1" gutterBottom>
            Начало: {currentEvent ? moment(currentEvent.start).format('HH:mm') : '—'}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Конец: {currentEvent ? moment(currentEvent.end).format('HH:mm') : '—'}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Отмена
          </Button>
          <Button
            onClick={handleSave}
            color="primary"
            disabled={!currentEvent || events.some(
              (event) =>
                (event.start < currentEvent.end && event.end > currentEvent.start)
            )}
          >
            Сохранить
          </Button>
        </DialogActions>
      </Dialog>

      {/* Вывод списка сохранённых интервалов */}
      <Box>
        <Typography variant="h6">Сохранённые интервалы</Typography>
        {events.length === 0 ? (
          <Typography variant="body2" color="textSecondary">
            Нет сохранённых интервалов.
          </Typography>
        ) : (
          events.map((event, index) => (
            <Box key={index} sx={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0' }}>
              <Typography>
                {moment(event.start).format('dddd, HH:mm')} - {moment(event.end).format('HH:mm')}
              </Typography>
              <Button color="error" onClick={() => handleDeleteDialogOpen(event)}>
                Удалить
              </Button>
            </Box>
          ))
        )}
      </Box>

      {/* Confirm Delete Dialog */}
      <Dialog
        open={openDeleteDialog}
        onClose={handleDeleteDialogClose}
      >
        <DialogTitle>Удалить интервал?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Вы уверены, что хотите удалить этот интервал?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteDialogClose} color="primary">
            Отмена
          </Button>
          <Button
            onClick={() => eventToDelete && handleDelete(eventToDelete)}
            color="secondary"
          >
            Удалить
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default TeacherAvailabilityForm;
