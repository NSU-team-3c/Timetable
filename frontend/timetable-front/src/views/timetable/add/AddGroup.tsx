import React, { useEffect } from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../../components/container/PageContainer';
import GroupList from '../../../components/list/GroupList';
import { AppState, useDispatch, useSelector } from '../../../store/Store';
import { fetchGroups, resetGroupUpdatedFlag } from '../../../store/application/group/groupSlice';


const AddGroup = () => {
    const dispatch = useDispatch();
    const { dataUpdated } = useSelector((state: AppState) => state.groups);
    
    useEffect(() => {
        if (dataUpdated && location.pathname === '/admin/timetable/add-group') {
          dispatch(fetchGroups())
            .then(() => {
              dispatch(resetGroupUpdatedFlag());
            })
            .catch((error) => {
              console.error('Ошибка при обновлении данных:', error);
            });
        }
      }, [dataUpdated, dispatch, location.pathname]);


    return (
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
};

export default AddGroup;


