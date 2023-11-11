import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../services/document.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Document } from 'src/app/models/document.model';


@Component({
  selector: 'app-content-part',
  templateUrl: './content-part.component.html',
  styleUrls: ['./content-part.component.scss']
})
export class ContentPartComponent implements OnInit{

  categoryID?: number;
  documents: Document[] = [];
  documentID?: number;
  downloadUrl?: string;
  fileName?: string;

  constructor(
    private documentService: DocumentService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.categoryID = +params['categorieID']; // '+' converts the string to a number
      // Now, this.categoryId contains the categoryId from the route parameter
      this.documentID = +params['documentID']
      this.fetchDocuments();
    });
  }

  fetchDocuments() {
    this.documentService.getDocumentsByCategorie(this.categoryID)
      .subscribe((documents: Document[]) => {
        this.documents = documents;
        console.log('Fetched Documents:', documents);
      });
  }

  downloadDocument() {
    this.documentService.downloadDocument(this.documentID).subscribe(data => {
      const blob = new Blob([data], { type: 'application/octet-stream' });
      this.downloadUrl = window.URL.createObjectURL(blob);
    });
  }

  onDocumentClick(fileName?: string, documentID?: number): void {
    // Handle the click event, you have access to the document ID (documentId) here
    console.log('Clicked document ID:', documentID);
  
    // Now you can fetch the document details or perform any other action based on the document ID
    this.documentService.downloadDocument(documentID).subscribe((blob: Blob) => {
      // Handle the response from the service call
      const link = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);

      link.href = objectUrl;
      link.target = '_blank';

      const fileExtension = this.getFileExtensionFromUrl('document_url.pdf');
      link.download = `document_${new Date().toISOString()}.${fileExtension}`;

      document.body.appendChild(link);
      link.click();

      URL.revokeObjectURL(objectUrl);
      document.body.removeChild(link);
    },
    (error) => {
      console.error('Error downloading document:', error);
    }
    );
  }

  getFileExtensionFromUrl(url: string): string {
    const pathArray = url.split('.');
    return pathArray[pathArray.length - 1];
  }

}
