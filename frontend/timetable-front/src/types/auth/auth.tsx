export interface LoginFormValues {
    email: string;
    password: string;
    rememberMe: boolean;
}

export interface ForgotPasswordValues {
    email: string;
}

export interface RegisterFormValues {
    email: string;
    password: string;
    confirmPassword: string;
}