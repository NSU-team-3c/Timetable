import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import GroupList from '../../components/list/GroupList';


const AddGroup = () => (
    <PageContainer title="Профиль" description="this is Profile page">
        <Box
            p={2}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
            }}
        >
            <GroupList />
        </Box>
    </PageContainer>
);

export default AddGroup;