import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {MatLine} from '@angular/material/grid-list';
import {EditorComponent} from '../../comon/quill-editor/quill-editor.component';
import {HttpClient, HttpEvent} from '@angular/common/http';
import {DocumentService} from '../../services/document.service';
import {DocumentApp, Fichier, Uploading} from '../../models';
import {environment} from '../../../env/environments';

@Component({
  selector: 'app-document-details',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule, MatIconModule, MatProgressSpinnerModule, MatLine, EditorComponent],
  templateUrl: './document-details.component.html',
  styleUrls: ['./document-details.component.css']
})
export class DocumentDetailsComponent implements OnInit {
  document?: DocumentApp;
  uploadings: Uploading[]=[];
  filesToUploads: any;
  isLoading = true;
  description: String = 'zazazaza';

  constructor(
    private route: ActivatedRoute,
    private documentService: DocumentService,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.documentService.getDocumentById(id).subscribe({
        next: (doc) => {
          this.document = doc;
          this.isLoading = false;
        },
        error: () => (this.isLoading = false)
      });
    }
  }

  openFile(file: Fichier) {
    window.open(file.path, '_blank');
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    console.debug("drop ici ");
    if (input.files) {
      console.debug("input.files",input.files);
      this.filesToUploads = input.files;
      for (let i = 0; i < input.files.length; i++) {
        let uploading: Uploading = new class implements Uploading {
          file: File = input.files?.item(i)!;
          progression: number = 0;
          status:  '' = '';
        }
        this.uploadings.push(uploading);
      }
    }
  }

  removeFile(index: number) {
    this.uploadings.splice(index, 1);
  }

  uploadFils() {
    let documentId:string = '';
    if (this.document)
      documentId = this.document.id;
    this.documentService.uploadDocument(this.uploadings,documentId ).subscribe( complerte => {
      alert(true);
    }, error => {
      alert(JSON.stringify(error));
    })
  }

  downloadUrl(file: Fichier) {
    return environment.DOCUMENT_SERVICE_API+"download/"+file.filePath	;
  }
}
