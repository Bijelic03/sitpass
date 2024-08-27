import { DisciplineDto } from './disciplineDto';
import { WorkDayDto } from './workDayDto';


export interface FacilityDto { 
    id?: number;
    name?: string;
    description?: string;
    createdAt?: string;
    address?: string;
    city?: string;
    totalRating?: number;
    active?: boolean;
    images?: Array<string>;
    workDays?: Array<WorkDayDto>;
    disciplines?: Array<DisciplineDto>;
}

