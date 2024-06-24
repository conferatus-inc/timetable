import axios from 'axios';

export const http = axios.create({
  baseURL: 'http://158.160.96.57:8090'
});

http.interceptors.request.use(
  config => {
    if (config.url.includes('/login')) {
      return config;
    }

    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);
