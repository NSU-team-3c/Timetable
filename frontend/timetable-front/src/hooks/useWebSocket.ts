import { useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { useDispatch } from '../store/Store';
import { fetchGroups, setGroupUpdateFlag } from '../store/application/group/groupSlice';
import { addNotification } from '../store/application/notification/notificationSlice';
import { fetchProfessors } from '../store/application/professor/professorSlice';
import { fetchAuditories } from '../store/application/room/auditorySlice';
import { fetchSubjects } from '../store/application/subject/subjectSlice';

export function useWebSocket(baseUrl: string, token: string | null) {
  const socketRef = useRef<Client | null>(null);
  const dispatch = useDispatch();
  const [messages, setMessages] = useState<string[]>([]);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    if (!token) {
      console.warn('No token provided for WebSocket connection');
      return;
    }

    // Добавляем токен в параметры URL
    const url = new URL(baseUrl);
    url.searchParams.append('token', token);

    const client = new Client({
      brokerURL: url.toString(),
      reconnectDelay: 100,
      onConnect: () => {
        console.log('STOMP connected');
        setIsConnected(true);

        client.subscribe('/notifications/newLog', (message) => {
          const body = JSON.parse(message.body);

          switch(body.object) { 
            case "group": { 
                dispatch(fetchGroups());
                break; 
            } 
            case "registration": { 
                dispatch(fetchProfessors());
                break; 
            } 
            case "room": { 
                dispatch(fetchAuditories());
                break; 
            } 
            case "subject": { 
                dispatch(fetchSubjects());
                break; 
            } 
            default: { 
                console.log(body.object)
                break; 
            } 
          } 

          dispatch(addNotification(body))
          setMessages((prev) => [...prev, JSON.stringify(body)]);
          console.log(body);
        });
      },
      onStompError: (error) => {
        console.error('STOMP error:', error);
      },
      onWebSocketClose: () => {
        console.log('WebSocket disconnected');
        setIsConnected(false);
      },
    });

    socketRef.current = client;
    client.activate();

    return () => {
      if (socketRef.current) {
        socketRef.current.deactivate();
      }
    };
  }, [baseUrl, token, dispatch]);

  const sendMessage = (message: string) => {
    if (socketRef.current && socketRef.current.connected) {
      socketRef.current.publish({ destination: 'chanтel/to/send/message', body: message });
    } else {
      console.warn('WebSocket is not open.');
    }
  };

  return { messages, sendMessage, isConnected };
}
