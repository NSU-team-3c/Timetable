import axios from 'axios';

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
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
    localStorage.removeItem('authToken');
      console.log('Unauthorized, please login again.');
    } else {
        console.error('API Error: ', error);
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance;
