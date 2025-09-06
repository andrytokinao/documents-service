import { Routes } from '@angular/router';
import {DocumentListComponent} from './pages/document-list/document-list.component';
import {DocumentComponent} from './pages/document/document.component';
import {HomeComponent} from './pages/home/home.component';
import {DocumentDetailsComponent} from './pages/document-details/document-details.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'list', component: DocumentListComponent },
  { path: 'new', component: DocumentComponent },
  { path: ':id', component: DocumentDetailsComponent },
  { path: '**', redirectTo: '' }
];
