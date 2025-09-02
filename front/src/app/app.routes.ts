import { Routes } from '@angular/router';
import {DocumentListComponent} from './pages/document-list/document-list.component';
import {DocumentComponent} from './pages/document/document.component';
import {HomeComponent} from './pages/home/home.component';
import {DocumentDetailsComponent} from './pages/document-details/document-details.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'documents', component: DocumentListComponent },
  { path: 'document/new', component: DocumentComponent },
  { path: 'document/:id', component: DocumentDetailsComponent },
  { path: '**', redirectTo: '' }
];
