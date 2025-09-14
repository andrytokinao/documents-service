import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpEventType, HttpRequest} from '@angular/common/http';
import {BehaviorSubject, catchError, finalize, Observable, of, tap} from 'rxjs';
import {environment} from '../../env/environments';
import {DocumentApp, Fichier, Uploading} from '../models';


@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  isComplete:BehaviorSubject<Boolean> = new BehaviorSubject<Boolean>(false);

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



  uploadDocument(uploadings: Uploading[], documentId: string) {
    this.isComplete = new BehaviorSubject<Boolean>(false);
    let index = 0;
    let count = 0;
    return new Observable<Boolean>(observer => {
      this.sendSequentialUpload(index, documentId, uploadings).subscribe(uploades => {
        if (uploades.type === HttpEventType.Response) {
          observer.next(true);
          observer.complete();
        }
      }, er => {
        observer.error(er);
        observer.complete();
      })
    });
  }

  sendSequentialUpload(index: number, documentId: String, uploadings: Uploading[]): Observable<any> {

    if (uploadings === undefined || uploadings.length === index) {
      this.isComplete.next(true);
      return of('Toutes les uploadées');

    }
    let current = uploadings[index];


    return this.upload(current.file, documentId).pipe(
      tap((event) => {
        if (event.type === HttpEventType.UploadProgress) {
          const progress = Math.round((event.loaded / (event.total || 1)) * 100);
          console.log(`Progression de ${current.file.name}: ${progress}%`);
          current.status = 'uploading';
          current.progression = progress;
        } else if (event.type === HttpEventType.Response) {
          console.log(`Upload terminé pour ${current.file.name}`);
          const uploaded: Fichier = JSON.parse(event.body);
          current.status = 'success';

          index++;
        }
      }),
      catchError((error) => {
        console.error(`Erreur lors de l'upload de ${current.file.name}:`, error);
        current.status = 'error';
        index++;
        return of(null);
      }),
      finalize(() => {
        this.sendSequentialUpload(index, documentId, uploadings).subscribe();
      })
    );

  }

  upload(file: File, documentId: String): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const req = new HttpRequest('POST', `${environment.DOCUMENT_SERVICE_API}upload?documentId=` + documentId , formData, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

}
