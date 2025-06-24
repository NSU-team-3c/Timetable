import { Box, Button, Typography } from "@mui/material";
import { AppState, useSelector } from "../../store/Store";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Spinner from "../../views/spinner/Spinner";
import { UnplacedObjects } from "../../types/timetable/timetable";

const Result = ({setOpen, result, wait} : {setOpen: (value: React.SetStateAction<boolean>) => void, result: any, wait: boolean}) => {
     const {profile} = useSelector((state: AppState) => state.profile);
     const navigate = useNavigate();
   
     return (
       <Box sx={{ width: "100%", margin: '0 auto', padding: 2, position: 'relative' }}>
            {wait ? "Генерация расписания..." : 
            <>

            <Typography>Статус: {result.isGeneratedSuccessfully ? "расписание сгенерировано" : "не удалось сгенерировать расписание"}</Typography>
            {!result.isGeneratedSuccessfully && result.unplacedSubjects &&
            result.unplacedSubjects.map((one : UnplacedObjects) => {
                <>
                <Typography>{one.groupNumber}</Typography>
                <Typography>{one.subjectName}</Typography>
                 <Typography>{one.teacherName}</Typography>
                  <Typography>{one.audienceType}</Typography>
                  </>
            })}
            </>
            }

           <Box mt={2}>
             <Button
               type="submit"
               fullWidth
               variant="contained"
               color="primary"
               onClick={() => setOpen(false)}
             >
               ОК
             </Button>
           </Box>

       </Box>
     );
   };

export default Result;