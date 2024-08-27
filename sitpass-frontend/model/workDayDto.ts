import { LocalTime } from './localTime';


export interface WorkDayDto { 
    id?: number;
    facilityId?: number;
    validFrom?: string;
    day?: string;
    fromTime?: LocalTime;
    untilTime?: LocalTime;
}

