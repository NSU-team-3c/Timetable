import Router from './routes/Router'
import { useRoutes } from 'react-router-dom';
import { ThemeSettings } from './theme/Theme';
import ScrollToTop from './components/ScrollToTop';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { Toaster } from 'react-hot-toast';

function App() {
  // Получаем маршруты из Router.ts
  const routing = useRoutes(Router);
  
  // Получаем текущую тему через ThemeSettings
  const theme = ThemeSettings();
  
  return (
    <ThemeProvider theme={theme}>
      <Toaster />
        <CssBaseline />
        <ScrollToTop>{routing}</ScrollToTop>
    </ThemeProvider>
  );
}

export default App;
