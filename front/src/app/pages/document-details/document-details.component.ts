import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {DocumentApp, DocumentService, Fichier} from '../../services/document.service';
import {MatLine} from '@angular/material/grid-list';

@Component({
  selector: 'app-document-details',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule, MatIconModule, MatProgressSpinnerModule, MatLine],
  templateUrl: './document-details.component.html',
  styleUrls: ['./document-details.component.css']
})
export class DocumentDetailsComponent implements OnInit {
  document?: DocumentApp;
  isLoading = true;

  constructor(private route: ActivatedRoute, private documentService: DocumentService) {}

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
}
