import React, { useEffect } from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../../components/container/PageContainer';
import ProfessorsList from '../../../components/list/ProfessorsList';
import { AppState, useDispatch, useSelector } from '../../../store/Store';
import { fetchProfessors, resetProfessorUpdatedFlag } from '../../../store/application/professor/professorSlice';


const AddProfessor = () => {
    const dispatch = useDispatch();
    const { dataUpdated } = useSelector((state: AppState) => state.professors);
        
    useEffect(() => {
        if (dataUpdated && location.pathname === '/admin/timetable/add-professor') {
            dispatch(fetchProfessors())
            .then(() => {
                dispatch(resetProfessorUpdatedFlag());
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
                <ProfessorsList />
            </Box>
        </PageContainer>
    );
};

export default AddProfessor;
