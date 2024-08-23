import { Component } from '@angular/core';
import { Document } from '../models/document.model';

@Component({
  selector: 'app-right-part',
  templateUrl: './right-part.component.html',
  styleUrls: ['./right-part.component.scss']
})
export class RightPartComponent {

  selectedDocument: Document | null = null;

  openDocumentViewer(document: Document): void {
    this.selectedDocument = document;
    //console.log(this.selectedDocument);
  }

  closeDocumentViewer(): void {
    this.selectedDocument = null;
  }

}
