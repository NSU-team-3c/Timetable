export interface ResultType {
    isGeneratedSuccessfully: boolean;
    events: any[];
    unplacedSubjects: UnplacedObjects[];
}

export interface UnplacedObjects {
    groupNumber: number
    subjectName: string
    teacherName: string
    audienceType: string
}

