import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { FacilityPageComponent } from './pages/facility-page/facility-page.component';
import { CreateFacilityPageComponent } from './pages/create-facility-page/create-facility-page.component';
import { BookExerciseComponent } from './components/book-exercise/book-exercise.component';
import { ReviewFormPageComponent } from './pages/review-form-page/review-form-page.component';

export const routes: Routes = [
    { path: '', component: HomePageComponent },
    { path: 'facilities/:id', component: FacilityPageComponent},
    { path: 'create-facility', component: CreateFacilityPageComponent},
    { path: 'facilities/edit/:id', component: CreateFacilityPageComponent },
    { path: 'book-exercise/:facilityId', component: BookExerciseComponent },
    { path: 'leave-review/:facilityId', component: ReviewFormPageComponent },

];
