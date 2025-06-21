// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { Accordion, AccordionDetails, AccordionSummary, Box, Divider, Grid, Typography } from '@mui/material';
import { IconChevronDown } from '@tabler/icons-react';

const Questions = () => {
  return (
    <Box>
      <Grid container spacing={3} >
        <Grid item >
          <Box mt={2} mb={1}>
        <Box  p={1} sx={{width: "90%"}}> 
          <Typography variant="h3" textAlign="center" p={2} color='primary' >Часто задаваемые вопросы</Typography>
          <Typography variant="h6" fontWeight={400} sx={{color: "primary.contrastText"}} textAlign="center" mb={4}>Узнайте как использовать этот сайт.</Typography>
          </Box>
          </Box>
          <Box p={1} sx={{width: "90%", margin: "0 auto 0 auto"}}> 
          <Accordion elevation={9}>
            <AccordionSummary
              expandIcon={<IconChevronDown />}
              aria-controls="panel1a-content"
              id="panel1a-header"
            >
              <Typography variant="h6" px={2} py={1}>Что такое University Timetable?</Typography>
            </AccordionSummary>
            
            <Divider />
            <AccordionDetails>
              <Typography variant="subtitle1" pt={1} px={2} color="textSecondary">
                University Timetable - это платформа, где учителя НГУ могут задавать время своей работы, а студенты - просматривать свое расписание.
              </Typography>
            </AccordionDetails>
          </Accordion>
          </Box>
          <Box p={1} sx={{width: "90%", margin: "0 auto 0 auto"}}> 
          <Accordion elevation={9}>
            <AccordionSummary
              expandIcon={<IconChevronDown />}
              aria-controls="panel2a-content"
              id="panel2a-header"
            >
              <Typography variant="h6" px={2} py={1}>Как указать таймслоты?</Typography>
            </AccordionSummary>
            <Divider />
            <AccordionDetails>
              <Typography variant="subtitle1" pt={1} px={2} color="textSecondary">
                Получите подтвержение учетной записи от админа учебного заведения, после чего перейдите на вкладку "Свободное время" и выберите те промежутки времени
                когда можете вести пары.
              </Typography>
            </AccordionDetails>
          </Accordion>
          </Box>
          <Box p={1} sx={{width: "90%", margin: "0 auto 0 auto"}}> 
          <Accordion elevation={9}>
            <AccordionSummary
              expandIcon={<IconChevronDown />}
              aria-controls="panel3a-content"
              id="panel3a-header"
            >
              <Typography variant="h6" px={2} py={1}>Как подтвердить профиль преподавателя?</Typography>
            </AccordionSummary>
            <Divider />
            <AccordionDetails>
              <Typography variant="subtitle1" pt={1} px={2} color="textSecondary">
                Обратитесь с этим вопросом в деканат учебного заведения, в котором собираетесь преподавать.
              </Typography>
            </AccordionDetails>
          </Accordion>
          </Box>
          <Box p={1} sx={{width: "90%", margin: "0 auto 0 auto"}}> 
          <Accordion elevation={9}>
            <AccordionSummary
              expandIcon={<IconChevronDown />}
              aria-controls="panel4a-content"
              id="panel4a-header"
            >
              <Typography variant="h6" px={2} py={1}>Что будет если я не укажу таймслоты?</Typography>
            </AccordionSummary>
            <Divider />
            <AccordionDetails>
              <Typography variant="subtitle1" pt={1} px={2} color="textSecondary">
                Вуз будет считать, что у вас много свободного времени.
              </Typography>
            </AccordionDetails>
          </Accordion>
          </Box>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Questions;