import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { Dashboard } from './components/dashboard/dashboard';
import { RestaurantMenu } from './components/restaurant-menu/restaurant-menu';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'dashboard', component: Dashboard },
    { path: 'restaurant/:id', component: RestaurantMenu }
];
