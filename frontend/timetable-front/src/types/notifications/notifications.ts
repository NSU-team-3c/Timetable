export interface Notification {
    id?: number;
    object: string;
    message: string;
    updatedBy: string;
    updatedAt: string;
    subMessage?: string;
}