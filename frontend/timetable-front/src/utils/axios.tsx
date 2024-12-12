import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const axiosInstance = axios.create({
  baseURL: 'https://api.example.com', 
  headers: {
    'Content-Type': 'application/json', 
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    } else {
      delete config.headers['Authorization'];
    }
    
    return config; 
  },
  (error) => {
    return Promise.reject(error); 
  }
);

// Интерсептор ответа для обработки ошибок
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response) {
      const status = error.response.status;
      switch (status) {
        case 401:
          localStorage.removeItem('authToken');
          console.log('Unauthorized, please login again.');
          break;
        case 403:
          console.log('Forbidden, you do not have permission to access this resource.');
          break;
        case 500:
          console.log('Internal Server Error. Please try again later.');
          break;
        default:
          console.error('An error occurred:', error);
      }
    } else {
      console.error('Error with no response:', error);
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance;
