import { useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useDispatch } from '../store/Store';
import { setGroupUpdateFlag } from '../store/application/group/groupSlice';

export function useWebSocket(url: string, token: string | null) {
  const socketRef = useRef<Client | null>(null);
  const dispatch = useDispatch();
  const [messages, setMessages] = useState<string[]>([]);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {

    const client = new Client({
      brokerURL: url,
      connectHeaders : {
        Authorization: `Bearer ${token}`,
      },
      webSocketFactory: () => SockJS(url),
      onConnect: () => {
        console.log('STOMP connected');
        setIsConnected(true);
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

    client.subscribe('chanтel/to/recive/message', (message) => {
      JSON.parse(message.body);
          
      dispatch(setGroupUpdateFlag());
    });

    client.activate();


    return () => {
      if (socketRef.current) {
        socketRef.current.deactivate();
      }
    };
  }, [url, token]);

  const sendMessage = (message: string) => {
    if (socketRef.current && socketRef.current.connected) {
      socketRef.current.publish({ destination: 'chanтel/to/send/message', body: message });
    } else {
      console.warn('WebSocket is not open.');
    }
  };

  return { messages, sendMessage, isConnected };
}