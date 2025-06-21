import React, { useEffect } from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../../components/container/PageContainer';
import ClassroomList from '../../../components/list/ClassroomList';
import { AppState, useDispatch, useSelector } from '../../../store/Store';
import { fetchAuditories, resetAuditoryUpdatedFlag } from '../../../store/application/room/auditorySlice';



const AddClassroom = () => {

    const dispatch = useDispatch();
    const { dataUpdated } = useSelector((state: AppState) => state.rooms);
        
    useEffect(() => {
        if (dataUpdated && location.pathname === '/admin/timetable/add-classroom') {
            dispatch(fetchAuditories())
            .then(() => {
                dispatch(resetAuditoryUpdatedFlag());
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
                <ClassroomList />
            </Box>
        </PageContainer>
    );
};

export default AddClassroom;
