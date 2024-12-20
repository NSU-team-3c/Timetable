import React from 'react';
import { Box } from '@mui/material';
import PageContainer from '../../components/container/PageContainer';
import CourseCreateForm from '../../components/forms/courses/CoursesCreateForm';

const Course = () => (
    <PageContainer title="Создание курса" description="this is coures create page">
        <Box
            p={24}
            sx={{
                backgroundColor: '#FFFFFF',
                borderRadius: 0,
                padding: '10%',
            }}
        >
            <CourseCreateForm />
        </Box>
    </PageContainer>
);

export default Course;
