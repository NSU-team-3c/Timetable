export interface ProfileData {
  surname: string;
  name: string;
  patronymic: string;
  birthday: string;
  email: string;
  phone: string;
  about: string;
  photo: File | null;
  group: number | null;
  role: string;
}

export interface ProfessorFormValues {
  organisation: string;
  education: string;
  courses: string;
  specialization: string;
}

export interface ChangePasswordValues {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
  confirmationCode: string; 
}