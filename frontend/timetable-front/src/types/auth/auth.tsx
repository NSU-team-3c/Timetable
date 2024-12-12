export interface Login {
    email: string;
    password: string;
    rememberMe: boolean;
}

export interface ForgotPassword{
    email: string;
}

export interface Register {
    email: string;
    password: string;
    confirmPassword: string;
}