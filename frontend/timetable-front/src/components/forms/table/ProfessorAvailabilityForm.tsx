import React, { useState, useEffect, useCallback } from 'react';
import { Calendar, dateFnsLocalizer } from 'react-big-calendar';
import { format, parse, startOfWeek, getDay, addDays, addMinutes } from 'date-fns';
import { ru } from 'date-fns/locale'; // Локализация на русском
import 'react-big-calendar/lib/css/react-big-calendar.css';
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography, DialogContentText } from '@mui/material';
import { AppState, dispatch } from '../../../store/Store';
import { addTimeslot, deleteTimeslot, fetchTimeslots, removeTimeslot, updateTimeslot } from '../../../store/professor/avaliabilitySlice';
import { useSelector } from 'react-redux';

const localizer = dateFnsLocalizer({
  format,
  parse,
  startOfWeek,
  getDay,
  locales: { 'ru': ru }, 
});

const fixedWeekStart = new Date(2024, 9, 20, 9, 0); 

interface Availability {
  id: number;
  startTime: Date;
  endTime: Date;
}

const TeacherAvailabilityForm: React.FC = () => {
  const [events, setEvents] = useState<Availability[]>([]); 
  const {timeslots} = useSelector((state: AppState) => state.availability);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [eventToDelete, setEventToDelete] = useState<Availability | null>(null);

  useEffect(() => {
    dispatch(fetchTimeslots());
  }, [dispatch]);
  
  // Инициализация событий на выбор
  useEffect(() => {
    const newEvents: Availability[] = [];
    for (let day = 1; day < 7; day++) {
      for (let lesson = 0; lesson < 7; lesson++) {
        const id = 100 * day + lesson;
        const startTime = addMinutes(addDays(fixedWeekStart, day), lesson * 110); 
        const endTime = addMinutes(startTime, 95); 
        newEvents.push({ id, startTime, endTime });
      }
    }
    setEvents(newEvents);
  }, []);

  // Удаление события
  const handleDelete = useCallback((eventToDelete: Availability) => {
    if (eventToDelete) {
      dispatch(deleteTimeslot(eventToDelete.id));
      setOpenDeleteDialog(false);
    }
  }, [dispatch, eventToDelete]);

  const handleDeleteDialogOpen = (event: Availability) => {
    setEventToDelete(event);
    setOpenDeleteDialog(true);
  };

  const handleDeleteDialogClose = () => {
    setOpenDeleteDialog(false);
  };

  const CustomWeekHeader = ({ label }: { label: string }) => (
    <Box sx={{ textAlign: 'center', fontWeight: 'bold' }}>
      {label.split(" ")[1]} {/* Отображает только день недели */}
    </Box>
  );

  const isTimeConflict = (event1: Availability, event2: Availability) => {
    const start1 = new Date(event1.startTime); 
    const end1 = new Date(event1.endTime); 
    const start2 = new Date(event2.startTime); 
    const end2 = new Date(event2.endTime);

    return (
      start1.getTime() === start2.getTime() &&
      end1.getTime() === end2.getTime()
    );
  };

  const handlerEvent = useCallback((event: Availability) => {
    const conflictedEvent = timeslots.find(e => isTimeConflict(e, event));
    if (conflictedEvent === undefined) {
      dispatch(updateTimeslot(event));
    } else {
      handleDeleteDialogOpen(conflictedEvent);  
    }
  }, [dispatch, timeslots]);


  const eventPropGetter = useCallback((event: Availability) => {
    
    if (timeslots.some(e => isTimeConflict(e, event))) {
      return {
        style: {
          backgroundColor: '#ff7043', 
          color: '#fff',              
          borderRadius: '5px',       
        },
      };
    }
    return {}; 
  }, [timeslots]);

  return (
    <Box>
      <Typography variant="h4" gutterBottom align="center">
        Управление временем доступным для преподавания
      </Typography>

      <Calendar
        localizer={localizer}
        events={events} 
        startAccessor="startTime"
        endAccessor="endTime"
        defaultView="week"
        date={fixedWeekStart} 
        onSelectEvent={handlerEvent}
        eventPropGetter={eventPropGetter}
        views={['week']}
        step={55}
        min={new Date(2024, 9, 19, 9, 0)} 
        max={new Date(2029, 5, 19, 21, 50)}
        toolbar={false}
        components={{
          week: {
            header: CustomWeekHeader,
          },
        }}
      />

      {/* Список сохранённых интервалов */}
      <Box>
        <Typography variant="h6">Сохранённые интервалы</Typography>
        {timeslots.length === 0 ? (
          <Typography variant="body2" color="textSecondary">
            Нет сохранённых интервалов.
          </Typography>
        ) : (
          timeslots.map((event, index) => (
            <Box key={index} sx={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0' }}>
              <Typography>
                {format(event.startTime, 'eeee, HH:mm')} - {format(event.endTime, 'HH:mm')}
              </Typography>
              <Button color="error" onClick={() => handleDeleteDialogOpen(event)}>
                Удалить
              </Button>
            </Box>
          ))
        )}
      </Box>

      {/* Подтверждение удаления интервала */}
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
