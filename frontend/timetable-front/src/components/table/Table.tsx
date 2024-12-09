import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import { mockEvents } from '../../_mockApis/events'; 
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography } from '@mui/material';

const localizer = momentLocalizer(moment);

interface MyEvent {
  id:  string | number;
  title: string;
  start: Date;
  end: Date;
  professor: string;
  subject: string;
  recurrenceInterval?: number;  
}

const Table: React.FC = () => {
  const [events, setEvents] = useState<MyEvent[]>([]);
  const [open, setOpen] = useState(false); 
  const [currentEvent, setCurrentEvent] = useState<MyEvent | null>(null);
  
  const generateRecurringEvents = (event: MyEvent) => {
    const recurringEvents: MyEvent[] = [];
    const startDate = moment(event.start); 
    const endDate = moment(event.end);     


    if (event.recurrenceInterval === 0) {
      recurringEvents.push({
        ...event,
        id: `${event.id}-0`,
        start: startDate.toDate(),
        end: endDate.toDate(),
      });
      return recurringEvents; 
    }
  
    const repeatCount = 12; 
  
    for (let i = 0; i < repeatCount; i++) {
      let newStartDate = startDate.clone().add(i * (event.recurrenceInterval || 1), 'weeks');
      let newEndDate = endDate.clone().add(i * (event.recurrenceInterval || 1), 'weeks');
      
      recurringEvents.push({
        ...event,
        id: `${event.id}-${i}`,
        start: newStartDate.toDate(),
        end: newEndDate.toDate(),
      });
    }
  
    return recurringEvents;
  };
  

  useEffect(() => {
    const allEvents: MyEvent[] = [];

    mockEvents.forEach((event) => {
      const recurringEvents = generateRecurringEvents(event);
      allEvents.push(...recurringEvents); 
    });

    setEvents(allEvents); 
  }, []);

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
        startAccessor="start"
        endAccessor="end"
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
            Предмет: {currentEvent ? currentEvent.subject : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Преподаватель: {currentEvent ? currentEvent.professor : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Начало: {currentEvent ? moment(currentEvent.start).format('HH:mm') : ''}
          </Typography>
          <Typography variant="body1" gutterBottom>
            Конец: {currentEvent ? moment(currentEvent.end).format('HH:mm') : ''}
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
