import { Component } from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatCard, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/select';
import {DocumentApp, DocumentService, Fichier} from '../../services/document.service';
import {NgForOf, NgIf} from '@angular/common';
import {MatList, MatListItem} from '@angular/material/list';
import {Router} from '@angular/router';
import {MatButton} from '@angular/material/button';
import {MatInput} from '@angular/material/input';

@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  imports: [
    MatCard,
    MatLabel,
    NgIf,
    MatCardTitle,
    MatFormField,
    ReactiveFormsModule,
    MatList,
    MatListItem,
    NgForOf,
    MatButton,
    MatInput
  ],
  styleUrls: ['./document.component.css']
})
export class DocumentComponent {
  document?: DocumentApp;
  uploadedFiles: Fichier[] = [];
  isLoading = false;


  constructor(private fb: FormBuilder, private documentService: DocumentService, private router:Router) {
    this.documentForm = this.fb.group({
      title: ['', Validators.required],
      file: [null]
    });
  }

  documentForm :any ;
  createDocument() {
    const title = this.documentForm.value.title!;
    this.isLoading = true;
    this.documentService.createDocument(title).subscribe({
      next: (doc) => {
        this.document = doc;
        this.isLoading = false;
        this.router.navigate(['document',doc.id]);
      },
      error: () => (this.isLoading = false)
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file && this.document) {
      this.isLoading = true;
      this.documentService.uploadFile(file, this.document.id).subscribe({
        next: (fichier) => {
          this.uploadedFiles.push(fichier);
          this.isLoading = false;
        },
        error: () => (this.isLoading = false)
      });
    }
  }
}
