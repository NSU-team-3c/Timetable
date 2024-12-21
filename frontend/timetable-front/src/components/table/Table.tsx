import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import { mockEvents } from '../../_mockApis/events'; 
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography } from '@mui/material';
import { useSelector } from 'react-redux';
import { AppState, dispatch } from '../../store/Store';
import { fetchEvents, setEvents } from '../../store/application/table/eventSlice';

const localizer = momentLocalizer(moment);

interface MyEvent {
  id:  string | number;
  startTime: Date;
  endTime: Date;
  teacherName: string;
  subjectName: string;
  roomName: string;
}

const Table: React.FC = () => {
  const {events, loading} = useSelector((state: AppState) => state.events)
  //const [events, setEvents] = useState<MyEvent[]>([]);
  const [open, setOpen] = useState(false); 
  const [currentEvent, setCurrentEvent] = useState<MyEvent | null>(null);
  
  const generateRecurringEvents = (event: MyEvent) => {
    const recurringEvents: MyEvent[] = [];
    const startDate = moment(new Date(event.startTime)); 
    const endDate = moment(new Date(event.endTime));     
  
    const repeatCount = 12; 
  
    for (let i = 0; i < repeatCount; i++) {
      const newStartDate = startDate.clone().add(i, 'weeks');
      const newEndDate = endDate.clone().add(i, 'weeks');
      
      recurringEvents.push({
        ...event,
        id: `${event.id}-${i}`,
        startTime: newStartDate.toDate(),
        endTime: newEndDate.toDate(),
      });
    }
  
    return recurringEvents;
  };
  

  useEffect(() => {
    console.log(events)
    dispatch(fetchEvents());
    console.log(events)
  }, [dispatch]);

  useEffect(() => {    
    if (!loading && events.length > 0) {
      console.log(events)
      const allEvents: MyEvent[] = [];

      events.forEach((event) => {
        const recurringEvents = generateRecurringEvents(event);
        allEvents.push(...recurringEvents); 
      });

      dispatch(setEvents(allEvents));
    }
  }, [dispatch]);

  const eventPropGetter = (event: MyEvent) => {
    return {
      style: {
        backgroundColor: '#f0f8ff', 
        color: 'black',  
        fontSize: '100%',
      },
    };
  };

  const handlerEvent = (event: MyEvent) => {
    setCurrentEvent(event);
    setOpen(true);
  } 

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Box>
      <h2>Расписание</h2>
      <Calendar
        localizer={localizer}
        events={events}
        startAccessor="startTime"
        endAccessor="endTime"
        style={{ height: 700, fontSize: 20}}
        eventPropGetter={eventPropGetter} 
        onSelectEvent={handlerEvent}
        defaultView="day"
        views={['week', 'day']}
        step={55}
        min={new Date(2024, 9, 19, 9, 0)} 
        max={new Date(2029, 5, 19, 20, 0)}
      />


      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Событие</DialogTitle>
        <DialogContent>
          <Typography variant="body1" gutterBottom>
            Предмет: {currentEvent ? currentEvent.subjectName : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Преподаватель: {currentEvent ? currentEvent.teacherName : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Преподаватель: {currentEvent ? currentEvent.roomName : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Начало: {currentEvent ? moment(currentEvent.startTime).format('HH:mm') : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Конец: {currentEvent ? moment(currentEvent.endTime).format('HH:mm') : ''}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Отмена
          </Button>
        </DialogActions>
      </Dialog> 
    </Box>
  );
};

export default Table;
