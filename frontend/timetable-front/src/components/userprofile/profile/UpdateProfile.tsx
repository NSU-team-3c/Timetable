import React, { useEffect, useState } from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, Box, FormControl, FormHelperText } from '@mui/material';
import CameraAltIcon from '@mui/icons-material/CameraAlt';
import { Link, useNavigate } from 'react-router-dom'; 
import { ProfileData } from '../../../types/user/user';
import { AppState, dispatch, useSelector } from '../../../store/Store';
import { fetchProfile, updateProfile } from "../../../store/profile/profileSlice";
import { format } from 'date-fns';

const UpdateProfile = ({setUpdateProfile} : {setUpdateProfile: (value: React.SetStateAction<boolean>) => void}) => {
     const {profile} = useSelector((state: AppState) => state.profile);
     const [photoPreview, setPhotoPreview] = useState<string | null>(null);
     const navigate = useNavigate();
   
     useEffect(() => {
       dispatch(fetchProfile())
   
       if (profile?.photoUrl) {
         setPhotoPreview(URL.createObjectURL(profile.photoUrl));
       }
   
     }, [dispatch]);
   
     const formik = useFormik<ProfileData>({
       enableReinitialize: true,
       initialValues: {
         surname: profile?.surname || '',
         name: profile?.name || '',
         patronymic: profile?.patronymic || '',
         birthday: profile?.birthday ? format(new Date(profile.birthday), 'dd.MM.yy') : '', 
         email: profile?.email || '',
         phone: profile?.phone || '',
         about: profile?.about || '',
         photoUrl: profile?.photoUrl || null,
         role: profile?.role || '',
         group: profile?.group || null,
       },
   
       validationSchema: yup.object({
         name: yup.string().required('Имя обязательно'),
         surname: yup.string().required('Фамилия обязательна'),
         patronymic: yup.string().nullable(),
         birthday: yup.date().nullable(),
         email: yup.string().email('Неверный формат почты').required('Почта обязательна'),
         phone: yup.string().nullable(),
         about: yup.string().nullable(),
         group: yup.number().nullable(),
         photo: yup
           .mixed()
           .nullable()
           .notRequired()
           .test('fileSize', 'Размер файла слишком большой', (value) => {
             if (!value) return true; 
             if (value instanceof File) {
               return value.size <= 5000000; 
             }
             return false;
           })
           .test('fileType', 'Неверный формат файла', (value) => {
             if (!value) return true; 
             if (value instanceof File) {
               return ['image/jpeg', 'image/png', 'image/gif'].includes(value.type);
             }
             return false;
           }),
       }),
   
       onSubmit: async (values) => {
         console.log('Submitting profile:', values); 
         dispatch(updateProfile(values));
         navigate('/profile', { replace: true });

         setUpdateProfile(() => false)
       },
     });
   
     const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
       const file = event.target.files ? event.target.files[0] : null;
       if (file) {
         setPhotoPreview(URL.createObjectURL(file));
         formik.setFieldValue('photo', file); 
       }
     };
   
     return (
       <Box sx={{ width: "100%", margin: '0 auto', padding: 2, position: 'relative' }}>
         <form onSubmit={formik.handleSubmit}>
           {/* Поле для загрузки фотографии */}
           <Box
             sx={{
               position: 'absolute',
               top: 0,
               right: 0,
               zIndex: 1, 
             }}
           >
             <FormControl fullWidth margin="normal">
               <input
                 type="file"
                 accept="image/*"
                 id="photo-upload"
                 style={{ display: 'none' }} 
                 onChange={handleFileChange}
               />
               <label htmlFor="photo-upload">
                 <Box
                   sx={{
                     width: 150,
                     height: 165,
                     borderRadius: '5%', 
                     border: '0.1% solid #ddd',
                     display: 'flex',
                     justifyContent: 'center',
                     alignItems: 'center',
                     backgroundColor: '#f0f0f0',
                     cursor: 'pointer',
                     position: 'relative',
                   }}
                 >
                   {photoPreview ? (
                     <img
                       src={photoPreview}
                       alt="Preview"
                       style={{
                         width: '100%',
                         height: '100%',
                         objectFit: 'cover',
                         borderRadius: '5%',
                       }}
                     />
                   ) : (
                     <CameraAltIcon sx={{ fontSize: 30, color: '#555' }} />
                   )}
                 </Box>
               </label>
               {formik.touched.photoUrl && formik.errors.photoUrl && (
                 <FormHelperText error>{formik.errors.photoUrl}</FormHelperText>
               )}
             </FormControl>
           </Box>
   
           {/* Основная форма */}
           <Box display="flex" flexDirection="column" gap={2}>
             <TextField
               label="Фамилия"
               fullWidth
               variant="outlined"
               margin="normal"
               {...formik.getFieldProps('surname')}
               error={formik.touched.surname && Boolean(formik.errors.surname)}
               helperText={formik.touched.surname && formik.errors.surname}
             />
   
             <TextField
               label="Имя"
               fullWidth
               variant="outlined"
               margin="normal"
               {...formik.getFieldProps('name')}
               error={formik.touched.name && Boolean(formik.errors.name)}
               helperText={formik.touched.name && formik.errors.name}
             />
   
             <TextField
               label="Отчество"
               fullWidth
               variant="outlined"
               margin="normal"
               {...formik.getFieldProps('patronymic')}
               error={formik.touched.patronymic && Boolean(formik.errors.patronymic)}
               helperText={formik.touched.patronymic && formik.errors.patronymic}
             />
   
             <TextField
               label="Дата рождения"
               fullWidth
               variant="outlined"
               margin="normal"
               {...formik.getFieldProps('birthday')}
               error={formik.touched.birthday && Boolean(formik.errors.birthday)}
               helperText={formik.touched.birthday && formik.errors.birthday}
               placeholder="ДД.ММ.ГГГГ"
             />
   
   
             { profile ? (!(profile.role.split(', ').includes('student') || 
               (profile.role.split(', ').includes('administrator'))) ? 
                 <TextField
                   label="Группа"
                   fullWidth
                   variant="outlined"
                   margin="normal"
                   {...formik.getFieldProps('group')}
                   error={formik.touched.group && Boolean(formik.errors.group)}
                   helperText={formik.touched.group && formik.errors.group}
                 />
               : '' ) : ''
             }
             
   
             <TextField
               label="Номер телефона"
               fullWidth
               variant="outlined"
               margin="normal"
               {...formik.getFieldProps('phone')}
               error={formik.touched.phone && Boolean(formik.errors.phone)}
               helperText={formik.touched.phone && formik.errors.phone}
             />
   
             <Link to="/profile/change-password" style={{ color: '#1976d2', textAlign: 'left', display: 'block' }}>
               Изменить пароль {'>'}
             </Link>
   
             <TextField
               label="О себе"
               fullWidth
               variant="outlined"
               margin="normal"
               {...formik.getFieldProps('about')}
               error={formik.touched.about && Boolean(formik.errors.about)}
               helperText={formik.touched.about && formik.errors.about}
             />
           </Box>
           <Box mt={2}>
             <Button
               type="submit"
               fullWidth
               variant="contained"
               color="primary"
               disabled={formik.isSubmitting}
               onClick={() => console.log('Button clicked')}
             >
               Сохранить
             </Button>
           </Box>
         </form>
       </Box>
     );
   };

export default UpdateProfile;