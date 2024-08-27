import { CommentDto } from './commentDto';


export interface ReviewDto { 
    id?: number;
    commentDto?: CommentDto;
    authorId?: number;
    facilityId?: number;
    authorName?: string;
    createdAt?: string;
    exerciseCount?: number;
    hidden?: boolean;
    staffRate?: number;
    equipmentRate?: number;
    hygieneRate?: number;
    spaceRate?: number;
}

