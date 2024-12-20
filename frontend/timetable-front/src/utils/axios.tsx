import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', 
  headers: {
    'accept': 'application/json',
    'Content-Type': 'application/json', 
  },
});

axiosInstance.interceptors.request.use(
  (config) => {

    const token = sessionStorage.getItem('authToken');
    
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    } else {
      config.headers['Authorization'] = `Bearer `;
    }
    
    return config; 
  },
  (error) => {
    console.log(error);
    return Promise.reject(error); 
  }
);

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response) {
      const status = error.response.status;
      switch (status) {
        case 401:
          sessionStorage.removeItem('authToken');
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
