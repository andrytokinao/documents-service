import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../env/environments';

export interface Fichier {
  fileName: string;
  path: string;
}

export interface DocumentApp {
  id: string;
  titre: string;
  description?: string;
  fichiers: Fichier[];
}

@Injectable({
  providedIn: 'root'
})
export class DocumentService {

  constructor(private http: HttpClient) {}

  createDocument(title: string): Observable<DocumentApp> {
    const formData = new FormData();
    formData.append('title', title);
    return this.http.get<DocumentApp>(`${environment.DOCUMENT_SERVICE_API}create`, {
      params: { title }
    });
  }

  uploadFile(file: File, documentId: string): Observable<Fichier> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentId', documentId);

    return this.http.post<Fichier>(`${environment.DOCUMENT_SERVICE_API}upload`, formData);
  }

  getDocumentById(id: string): Observable<DocumentApp> {
    return this.http.get<DocumentApp>(`${environment.DOCUMENT_SERVICE_API}${id}`);
  }
}
