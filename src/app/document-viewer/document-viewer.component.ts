import { Component, Input, OnInit, SimpleChanges, OnChanges, ViewEncapsulation, EventEmitter, Output, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { Subscription, Subject } from 'rxjs';
import { DocumentService } from '../services/document.service';
import { Document } from '../models/document.model';
import { takeUntil } from 'rxjs/operators';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarLikeComponent } from '../snack-bar-like/snack-bar-like.component';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NavigationService } from '../services/navigation.service';

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
    private snackBar: MatSnackBar,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute,
    private navigationService: NavigationService,
  ) {
    
  }

  ngOnInit(): void {
    
    this.subscription.add(
      this.documentService.selectedDocument$.subscribe((document: Document | null) => {
        this.selectedDocument = document;

        // Update the document URL and ID
        if (this.selectedDocument) {
          this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument.documentID);
          this.documentID = this.selectedDocument.documentID;
        } else {
          // Reset URL and ID if no document is selected
          this.documentUrl = null;
          this.documentID = undefined;
        }
      })
    );

    this.route.queryParams.subscribe(params => {
      const illustrationId = params['illustration'];
      if (illustrationId) {
        // Fetch the document by ID
        this.router.navigate([], {
          queryParams: { illustration: illustrationId },
          queryParamsHandling: 'merge' // Keep other existing query parameters
        });
      }
    });


  }

  loadDocument(categoryID: number, documentID: number): void {
    // Supposez que vous ayez une méthode pour obtenir le document par ID
    this.documentService.getDocument(documentID).subscribe(doc => {
      this.selectedDocument = doc;
      this.documentUrl = this.documentService.getDocumentUrl(documentID);
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedDocument'] && this.selectedDocument) {
      this.documentUrl = this.documentService.getDocumentUrl(this.selectedDocument?.documentID);
    }
  }



  closeViewerPage(): void {

    this.closeViewer.emit();
    // const previousUrl = this.navigationService.getPreviousUrl();
    // if (previousUrl) {
    //   console.log(previousUrl);
    //   //this.router.navigate([previousUrl]);
    //   window.history.pushState({}, '', previousUrl);
    // }
    this.router.navigate([], { queryParams: { illustration: null }, queryParamsHandling: 'merge' });
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
