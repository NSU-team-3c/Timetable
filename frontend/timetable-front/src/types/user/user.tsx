export interface ProfileData {
  surname: string;
  name: string;
  patronymic: string | null;
  birthday: string | null;
  email: string;
  phone: string | null;
  about: string | null;
  photo: File   | null;
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