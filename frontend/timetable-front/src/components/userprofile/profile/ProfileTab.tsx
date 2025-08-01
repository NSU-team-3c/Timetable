// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { Box, Tab, Tabs } from '@mui/material';
import { IconPhoto, IconUserCircle } from '@tabler/icons-react';
import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const ProfileTab = () => {
  const location = useLocation();
  const [value, setValue] = React.useState(location.pathname);
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    setValue(newValue);
  };

  interface profileType {
    label: string;
    icon: JSX.Element;
    to: string;
  }

  const ProfileTabs: profileType[] = [
    {
      label: 'Профиль',
      icon: <IconUserCircle size="20" />,
      to: '/user-profile',
    }
  ];

  return (
    <Box mt={1} sx={{ mt: 1, backgroundColor: (theme) => theme.palette.grey[100] }}>
      <Box justifyContent={'end'} display="flex" sx={{ overflow: 'auto', width: { xs: '333px', sm: 'auto' } }}>
        <Tabs value={value} onChange={handleChange} aria-label="scrollable prevent tabs example" variant="scrollable" scrollButtons="auto">
          {ProfileTabs.map((tab) => {
            return (
              <Tab
                iconPosition="start"
                label={tab.label}
                sx={{ minHeight: '50px' }}
                icon={tab.icon}
                component={Link}
                to={tab.to}
                value={tab.to}
                key={tab.label}
              />
            );
          })}
        </Tabs>
      </Box>
    </Box>
  );
};

export default ProfileTab;
