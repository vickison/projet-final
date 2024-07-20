import { Component, OnInit, EventEmitter, Output, ViewChild, Input, ElementRef, ChangeDetectionStrategy, OnDestroy } from '@angular/core';
import { DocumentService } from '../services/document.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Document } from 'src/app/models/document.model';
import { ViewEncapsulation } from '@angular/core';
import { PdfViewerComponent } from 'ng2-pdf-viewer';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { EMPTY, map, Observable, of, Subscription, switchMap } from 'rxjs';
import { DetailDialogService } from '../services/detail-dialog.service';
import { HttpClient } from '@angular/common/http';
import { SearchService } from '../services/search.service';
import { CategorieDocumenttsService } from '../services/categorie-documentts.service';
import { FilterService } from '../services/filter.service';
import { Categorie } from '../models/categorie';
import { OrderService } from '../services/order.service';
import { MatDialog } from '@angular/material/dialog';
import { DetailPartComponent } from '../detail-part/detail-part.component';
import { CategorieService } from '../services/categorie.service';


@Component({
  selector: 'app-content-part',
  templateUrl: './content-part.component.html',
  styleUrls: ['./content-part.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPartComponent implements OnInit, OnDestroy{
  fileSizeFormatted: string='';

  @ViewChild('pdfViewer') pdfViewer!: PdfViewerComponent;
  @ViewChild('videoContainer') videoContainer: ElementRef | undefined

  isHovered = false;
  private previewTimer: any;
  private totalPreviewTime: number = 30000;
  private containerWidthInRem: number = 1;
  progress: number = 0;
  isMouseOverVideo: boolean = false;
  searchKeyword: string = '';
  flag: boolean = false;
  


  startVideoPreview(){
    this.playVideo();
    this.progress = 0;
    this.previewTimer = setInterval(()=>{
      this.progress += (1000 / this.totalPreviewTime) * 100;
      if(this.progress >= 100){
        this.stopVideoPreview();
      }
    }, 1000)
    console.log('vido start...');
    
  }

  stopVideoPreview(){
    clearInterval(this.previewTimer);
    this.pauseVideo();
    this.progress = 0;
    console.log('video stop...');
    
  }

  playVideo(){
    this.videoContainer?.nativeElement.play();
  }

  pauseVideo(){
    this.videoContainer?.nativeElement.pause();
  }

  // getFirstPage(){
  //   this.pdfViewer.pdfViewer.currentPageNumber = 1
  // }

  private categoryID: number | null= null;
  documents: Document[] = [];
  searchResults: Document[] = [];
  tempDocuments: Document[] = [];
  filteredDocuments: Document[] = [];
  orderedDocuments: Document[] = [];
  documentID?: number;
  downloadUrl?: any;
  fileName?: string;
  @Input() selectedDocument: Document | undefined;
  documentUrl: string = '';
  pdfBlob?: Blob;
  selectedCategorieID: number = 0;
  isSearching: boolean = false;
  @Input() docs: Document[]= [];
  @Input() categorieID: number | undefined;
  likeCounts: any = [];
  unLikeCounts: any = [];
  likeMap: any = new Map();
  unLikeMap: any = new Map();

  like: number = 0;
  unlike: number = 0;
  count: number = 0;

  private routeSubscription: Subscription = new Subscription();
  private filterSubscription: Subscription = new Subscription();
  private orderSubscription: Subscription = new Subscription();
  private refreshSubscription: Subscription = new Subscription();
  

  constructor(
    private documentService: DocumentService,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private detailDialogService: DetailDialogService,
    private dialog: MatDialog,
    private http: HttpClient,
    private searchService: SearchService,
    private categorieDocumentsService: CategorieDocumenttsService,
    private filterService: FilterService,
    private orderService: OrderService,
    private router: Router,
    private categorieService: CategorieService
  ) {
    
   }

  ngOnInit(): void {
    
    
    if(!this.categorieService.getFlag()){
      console.log('Without refresh service....');
      this.route.params.subscribe(params =>{
        const categoryID = +params['categorieID'];
        if(!isNaN(categoryID)){
          this.categoryID = categoryID;
          if(this.searchService.getSearchKeyword() === '' && 
            this.filterService.getFilter() === '' && 
            this.orderService.getOrder() === '')
            this.fetchDocuments(this.categoryID);
        }
      });
    }


    this.refreshSubscription = this.filterService.shouldRefresh$.subscribe((shouldRefresh) => {
      if (shouldRefresh) {
        console.log('Refresh requested...');
    
        // Utilisation de switchMap pour gérer les paramètres de l'URL de manière asynchrone
        this.route.params.pipe(
          switchMap(params => {
            // Récupérer l'ID de catégorie à partir des paramètres de l'URL
            const categoryID = +params['categorieID'];
    
            if (!isNaN(categoryID)) {
              // Mettre à jour categoryID et appeler fetchDocuments si les conditions sont remplies
              this.categoryID = categoryID;
              this.categorieService.setFlag(true);
    
              if (this.searchService.getSearchKeyword() === '' && 
                  this.filterService.getFilter() === '' && 
                  this.orderService.getOrder() === '') {
                return this.fetchDocuments(this.categoryID); // Retourner l'observable de fetchDocuments
              } else {
                // Si aucune action n'est nécessaire, retourner un observable vide
                return EMPTY;
              }
            } else {
              // Si categoryID n'est pas un nombre valide, retourner un observable vide
              return EMPTY;
            }
          })
        ).subscribe({
          next: () => {
            console.log('FetchDocuments completed.');
            this.filterService.clearRefresh(); // Nettoyer le rafraîchissement après le traitement
          },
          error: (error) => {
            console.error('Error loading documents', error);
          }
        });
      }
    });
    


    // this.refreshSubscription = this.filterService.shouldRefresh$.subscribe((shouldRefresh) => {
    //   if(shouldRefresh){
    //     console.log('With refresh service....');
    //     this.route.params.subscribe(params =>{
    //       this.categoryID = null;
    //       const categoryID = +params['categorieID'];
    //       if(!isNaN(categoryID)){
    //         this.categorieService.setFlag(true);
    //         this.categoryID = categoryID;
    //         if(this.searchService.getSearchKeyword() === '' && 
    //           this.filterService.getFilter() === '' && 
    //           this.orderService.getOrder() === ''){
    //             this.fetchDocuments(this.categoryID);
    //           }
              
    //       }
    //     });
    //     this.filterService.clearRefresh();
    //   }
      
    // });

    if(this.searchService.getSearchKeyword() !== '' || 
              this.filterService.getFilter() !== '' || 
              this.orderService.getOrder() !== ''){
                this.orderSubscription = this.orderService.order$.subscribe(order => {
                  if(order.trim() !== ''){
                    this.getDocumentsByOrder(order);
                    this.categoryID = null;
                    this.orderService.setOrder('');
                  }
                  else if(this.categoryID !== null){
                    this.fetchDocuments(this.categoryID);
                  }
                });
            
                this.filterSubscription = this.filterService.filter$.subscribe(filter =>{
                  if(filter.trim() !== ''){
                    this.getDocumentsByFilter(filter);
                    this.categoryID = null;
                    this.filterService.setFilter('');
                  }
                  else if (this.categoryID !== null){
                    this.fetchDocuments(this.categoryID);
                  }
                });
            
                
                this.routeSubscription = this.searchService.searchKeyword$.subscribe(keywords =>{
                  if(keywords.trim() !== ''){
                    this.getDocumentBySearch(keywords);
                    this.categoryID = null;
                    this.searchService.setSearchKeyword('');
                  }
                  else if (this.categoryID !== null){
                    this.fetchDocuments(this.categoryID);
                  }
                });
    }
    // this.orderSubscription = this.orderService.order$.subscribe(order => {
    //   if(order.trim() !== ''){
    //     this.getDocumentsByOrder(order);
    //     this.categoryID = null;
    //     this.orderService.setOrder('');
    //   }
    //   else if(this.categoryID !== null){
    //     this.fetchDocuments(this.categoryID);
    //   }
    // });

    // this.filterSubscription = this.filterService.filter$.subscribe(filter =>{
    //   if(filter.trim() !== ''){
    //     this.getDocumentsByFilter(filter);
    //     this.categoryID = null;
    //     this.filterService.setFilter('');
    //   }
    //   else if (this.categoryID !== null){
    //     this.fetchDocuments(this.categoryID);
    //   }
    // });

    
    // this.routeSubscription = this.searchService.searchKeyword$.subscribe(keywords =>{
    //   if(keywords.trim() !== ''){
    //     this.getDocumentBySearch(keywords);
    //     this.categoryID = null;
    //     this.searchService.setSearchKeyword('');
    //   }
    //   else if (this.categoryID !== null){
    //     this.fetchDocuments(this.categoryID);
    //   }
    // });
    
  }

  fetchDocuments(categorieID: number): Observable<null> {
    // Retourner l'Observable de getDocumentsByCategorie pour permettre la gestion de l'asynchronisme
    return this.documentService.getDocumentsByCategorie(categorieID)
      .pipe(
        switchMap((documents: Document[]) => {
          // Mise à jour des documents filtrés
          this.documents = documents.filter(doc => !doc.supprimerDocument);
          this.tempDocuments = this.documents; // Mettre à jour tempDocuments si nécessaire
          console.log('Documents mis à jour: ', this.documents);
  
          // Exemple de mise à jour d'un service ou autre logique nécessaire après récupération des documents
          this.categorieService.setFlag(false);
  
          // Retourner un observable vide ou une autre valeur si nécessaire
          return of(null);
        })
      );
  }


  // fetchDocuments(categorieID: number) {
    
  //   this.tempDocuments = this.documents;
  //   this.documents = [];
  //   this.documentService.getDocumentsByCategorie(categorieID)
  //     .subscribe((documents: Document[]) => {
  //       console.log('Docs Before: ', this.tempDocuments);
  //       this.documents = documents.filter(doc => !doc.supprimerDocument);
  //       this.tempDocuments = this.documents;
  //       console.log('Hello...');
  //     }, (error: any) => {
  //       console.error('Error loading documents', error);
  //     });

      
  //     this.categorieService.setFlag(false);
  // }


  getDocumentUrl(id?: number): string{
    return this.documentService.getDocumentUrl(id);
  }

  downloadDocument() {
    this.documentService.downloadDocument(this.documentID).subscribe(data => {
      const blob = new Blob([data], { type: 'application/octet-stream' });
      this.downloadUrl = window.URL.createObjectURL(blob);
      
    });
  }

  getDocumentBySearch(keywords: string): void{
    //this.tempDocuments = this.documents;
    this.documents = [];
    this.documentService.getDocumentByKeyword(keywords).subscribe((documents: Document[]) => {
      this.documents = documents.filter(doc => !doc.supprimerDocument);
      if(this.tempDocuments.length > 0){
        this.searchResults = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
        console.log('Search in category: ', this.searchResults);
        this.documents = this.searchResults;
        this.categorieService.setFlag(true);
      }
    },(error: any) =>{
      console.error('Error loading documents', error);
      
    });
  }

  getDocumentsByFilter(type: string): void{
    //this.tempDocuments = this.documents;
    this.documents = [];
    this.documentService.getDocumentsByType(type).subscribe((documents: Document[]) =>{
      this.documents = documents.filter(doc => !doc.supprimerDocument);
      if(this.tempDocuments.length > 0){
        this.filteredDocuments = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
        console.log('Filter in category: ', this.filteredDocuments);
        this.documents = this.filteredDocuments;
        this.categorieService.setFlag(true);
        //this.tempDocuments = [];
      }
    }, (error: any) =>{
      console.log('Error fetching documents ', error);
    });
  }

  getDocumentsByOrder(order: string): void{
    //this.tempDocuments = this.documents;
    this.documents = [];
    this.documentService.getDocumentsByOrder(order).subscribe((documents: Document[]) =>{
      this.documents = documents.filter(doc => !doc.supprimerDocument);
      if(this.tempDocuments.length > 0){
        this.orderedDocuments = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
        this.documents = this.orderedDocuments;
      }
    }, (error: any ) => {
      console.log('Error fetching documents ', error);
    })
  }


  getBlobDocument(id?: number): string {
    return this.documentService.getBlobDocument(id);
  }

  onDocumentClick(fileName?: string, documentID?: number): void {
    // Handle the click event, you have access to the document ID (documentId) here
    console.log('Clicked document ID:', documentID);
    if(fileName){
      this.documentService.downloadDocument(documentID).subscribe((blob: Blob) => {
        // Handle the response from the service call
        const link = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);

        link.href = objectUrl;
        link.target = '_blank';

        const fileExtension = this.getFileExtensionFromUrl(fileName);
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
    }else{
      console.error('File name is undefined.');
    }
    
    
  }

  getFileExtensionFromUrl(url: string): string {
    const pathArray = url.split('.');
    return pathArray[pathArray.length - 1];
  }

  @Output() documentClicked = new EventEmitter<Document>();

  onDocumentDBLClicked(document: Document): void {
    this.documentClicked.emit(document);
    console.log("Card dblclicked"+document);
  }

  formatBytes(bytes: number[] | undefined, decimals: number=2): string{
    if(bytes == undefined || bytes?.length == 0) return 'N/A';
    const sizeInBytes = bytes.length;
     const k = 1024;
     const dm = decimals < 0 ? 0 : decimals;

     const size = ['Bytes','KB','MB','GB'];
     const i = Math.floor(Math.log(sizeInBytes)/Math.log(k));
     return parseFloat((sizeInBytes/Math.pow(k, i)).toFixed(dm))+' '+size[i]
  }

  formatFileSize(bytes: number | undefined): string{
    if(bytes == undefined ) return 'N/A';
    else if(typeof bytes !== 'number' ) return 'Invalid file size';
    else if(bytes < 1024){
      return bytes + ' Bytes';
    }else if(bytes < 1024*1024){
      return (bytes / 1024).toFixed(2)+' KB';
    }else if(bytes < 1024*1024*1024){
      return (bytes / (1024*1024)).toFixed(2)+' MB';
    }else{
      return (bytes / (1024*1024*1024)).toFixed(2)+' GB';
    }
  }

  handlePdfLoad(event: any){
    console.log('PDF loaded successfully', event);
    
  }

  handlePdfError(event: any){
    console.error('Error loadind PDF', event);
    
  }

  getSafeUrl(url: string |  null): SafeResourceUrl | undefined {
    // Check if the URL is defined before sanitizing
    return url ? this.sanitizer.bypassSecurityTrustResourceUrl(url) : undefined;
  }

  openDetailDialog(document: Document): void {
    const dialogRef = this.dialog.open(DetailPartComponent, {
      width: '30%',
      data: {document}
    })
    //this.detailDialogService.openDialog();
  }

  ngOnDestroy(): void{
    if(this.routeSubscription){
      this.routeSubscription.unsubscribe();
    }else if(this.filterSubscription){
      this.filterSubscription.unsubscribe();
    }
    else if(this.refreshSubscription){
      this.refreshSubscription.unsubscribe();
    }
  }

  copyLink(){
    const link = window.location.href;
    console.log(link);
    navigator.clipboard.writeText(link).then(() => {
      alert("Lien copié dans le clipboard...");
    }).catch(err =>{
      console.error("Echec de copier le lien...");
    });
  }

  reloadPage(): void{
    window.location.reload();
  }

  // getDocumentByCategory(){
  //   this.documentService.getDocumentsByCategorie(this.categorieID).subscribe(documents =>{
  //     console.log('Emitted ', documents);
      
  //   })
  // }



}
