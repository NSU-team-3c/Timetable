import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import { mockEvents } from '../../_mockApis/events'; 

const localizer = momentLocalizer(moment);

interface MyEvent {
  id: number;
  title: string;
  start: Date;
  end: Date;
  professor: string;
  subject: string;
}

const Table: React.FC = () => {
  const [events, setEvents] = useState<MyEvent[]>([]);

  useEffect(() => {
    setEvents(mockEvents);
  }, []);

  const eventPropGetter = (event: MyEvent) => {
    return {
      style: {
        backgroundColor: '#f0f8ff', 
        color: 'black', 
        borderRadius: '5px', 
        padding: '10px',
        fontSize: '14px',
      },
    };
  };

  return (
    <div style={{ height: '100vh' }}>
      <h2>Расписание</h2>
      <Calendar
        localizer={localizer}
        events={events}
        startAccessor="start"
        endAccessor="end"
        style={{ height: 500 }}
        eventPropGetter={eventPropGetter} 
        defaultView='week'
        views={['week', 'day']}
        step={55}
        min={new Date(2024, 10, 19, 9, 0)} 
        max={new Date(2024, 10, 19, 20, 0)} 
      />
    </div>
  );
};

export default Table;
