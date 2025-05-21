import React, { useEffect } from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../../components/container/PageContainer';
import SubjectList from '../../../components/list/SubjectList';
import { AppState, useDispatch, useSelector } from '../../../store/Store';
import { fetchSubjects, resetSubjectUpdatedFlag } from '../../../store/application/subject/subjectSlice';


const AddSubject = () => {
    const dispatch = useDispatch();
    const { dataUpdated } = useSelector((state: AppState) => state.subjects);

    useEffect(() => {
        if (dataUpdated && location.pathname === '/admin/timetable/add-subject') {
            dispatch(fetchSubjects())
            .then(() => {
                dispatch(resetSubjectUpdatedFlag());
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
                <SubjectList />
            </Box>
        </PageContainer>
    );
};

export default AddSubject;
