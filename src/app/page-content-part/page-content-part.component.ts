import { Component } from '@angular/core';
import { Document } from '../models/document.model';


@Component({
  selector: 'app-page-content-part',
  templateUrl: './page-content-part.component.html',
  styleUrls: ['./page-content-part.component.scss']
})
export class PageContentPartComponent {
  selectedDocument: Document | null = null;

  openDocumentViewer(document: Document): void {
    this.selectedDocument = document;
    console.log(this.selectedDocument);
  }

  closeDocumentViewer(): void {
    this.selectedDocument = null;
  }
}
