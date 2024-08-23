import { Component, Input, OnInit, SimpleChanges, OnChanges, ViewEncapsulation, EventEmitter, Output } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { Subscription, Subject } from 'rxjs';
import { DocumentService } from '../services/document.service';
import { Document } from '../models/document.model';
import { takeUntil } from 'rxjs/operators';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarLikeComponent } from '../snack-bar-like/snack-bar-like.component';

@Component({
  selector: 'app-document-viewer',
  templateUrl: './document-viewer.component.html',
  styleUrls: ['./document-viewer.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DocumentViewerComponent implements OnInit, OnChanges{
 
  @Input() selectedDocument: Document | null = null;
  @Output() closeViewer = new EventEmitter<void>();
  documentUrl: string | null = null;
  documentID: number | undefined;
  isDocumentLiked: boolean | undefined;
  likeButtonVisible: boolean = false;
  private subscription: Subscription = new Subscription();

  constructor(
    private documentService: DocumentService,
    private sanitizer: DomSanitizer,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    // this.documentService.getSelectedDocument().subscribe((document: Document | null) => {
    //   this.selectedDocument = document;
    //   console.log("Selected document: ", document);
    //   this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument?.documentID);
    //   this.documentID = this.selectedDocument?.documentID;
    // });

    this.subscription.add(
      this.documentService.selectedDocument$.subscribe((document: Document | null) => {
        this.selectedDocument = document;
        //console.log("Selected document: ", document);

        // Met à jour l'URL et l'ID du document
        if (this.selectedDocument) {
          this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument.documentID);
          this.documentID = this.selectedDocument.documentID;
        } else {
          // Si aucun document n'est sélectionné, réinitialiser l'URL et l'ID
          this.documentUrl = null;
          this.documentID = undefined;
        }
      })
    );

  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('selectedDocument' in changes) {
      this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument?.documentID);
    }
    
  }

  closeViewerPage(): void {
    // let docID;
    // let isLiked;
    // this.documentService.documentIsLiked(this.selectedDocument?.documentID).subscribe(res => {
    //   console.log('liked', res);   
    // });
    setTimeout(() => {
      //this.documentService.setSelectedDocument(null);
      this.closeViewer.emit();
      //this.reloadPage();
    }, 100);
    
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
