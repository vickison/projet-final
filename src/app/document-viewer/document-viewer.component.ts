import { Component, Input, OnInit, SimpleChanges, OnChanges, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { Subscription, Subject } from 'rxjs';
import { DocumentService } from '../services/document.service';
import { Document } from '../models/document.model';
import { takeUntil } from 'rxjs/operators';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-document-viewer',
  templateUrl: './document-viewer.component.html',
  styleUrls: ['./document-viewer.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DocumentViewerComponent implements OnInit, OnChanges{
 
  @Input() selectedDocument: Document | null = null;
  documentUrl: string | null = null;
  documentID: number | undefined;
  isDocumentLiked: boolean | undefined;
  likeButtonVisible: boolean = false;

  constructor(private documentService: DocumentService,
    private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.documentService.getSelectedDocument().subscribe((document: Document | null) => {
      this.selectedDocument = document;
      console.log("Selected document: ", document);
      this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument?.documentID);
      this.documentID = this.selectedDocument?.documentID;
    });

  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('selectedDocument' in changes) {
      this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument?.documentID);
    }
    
  }

  closeViewer(): void {
    this.documentService.documentIsLiked(this.selectedDocument?.documentID).subscribe(res => {
      console.log('liked', res);
      if(confirm(res? 'Unlike 👎 cette illustration': 'Like 👍 cette illustration')){
        //console.log('Hello ng...');
        this.documentService.likeIllustration(this.selectedDocument?.documentID).subscribe({
          next: data => {
            console.log('Like or Unlike successfully...');
          },
          error: err => {
            console.log('Failed to Like or Unlike...');
          }
        });
        setTimeout(() => {
          this.reloadPage();
        }, 300);
      }
      

      
    });
    setTimeout(() => {
      this.documentService.setSelectedDocument(null);
    }, 200);
    
    
  }

  isAudioFormat(format: string): boolean {
    return format.startsWith('audio/');
  }
  
  isVideoFormat(format: string): boolean {
    return format.startsWith('video/');
  }

  isImageFormat(format: string): boolean {
    return format.startsWith('image/');
  }

  getSafeUrl(url: string |  null): SafeResourceUrl | undefined {
    // Check if the URL is defined before sanitizing
    return url ? this.sanitizer.bypassSecurityTrustResourceUrl(url) : undefined;
  }

  reloadPage(): void{
    window.location.reload();
  }

  onCloseButtonClicked(): void{}
}
