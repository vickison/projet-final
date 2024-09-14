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
import { RefresherService } from '../services/refresher.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { DocumentViewerComponent } from '../document-viewer/document-viewer.component';
import { NavigationService } from '../services/navigation.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';


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
  @ViewChild('videoElement') videoElement!: ElementRef<HTMLVideoElement>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  isHovered = false;
  private previewTimer: any;
  private totalPreviewTime: number = 30000;
  private containerWidthInRem: number = 1;
  progress: number = 0;
  isMouseOverVideo: boolean = false;
  searchKeyword: string = '';
  flag: boolean = false;
  documentThumbnailUrl?: string;
  displayWelcome: boolean = true;
  


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
  catDocuments: Document[] = [];
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
  @Output() documentClicked = new EventEmitter<Document>();
  

  like: number = 0;
  unlike: number = 0;
  count: number = 0;


  pageSize = 12; // Nombre d'éléments par page
  pageSizeOptions: number[] = [5, 10, 20];
  paginatedDocuments: Document[] = [];

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
    private categorieService: CategorieService,
    private refresherService: RefresherService,
    private snackBar: MatSnackBar,
    private translateService: TranslateService,
    private navigationService: NavigationService,
    
  ) {
    
   }

  ngOnInit(): void {
    this.displayWelcome = true;

    
  
    if(!this.categorieService.getFlag()){
      this.displayWelcome = false;
      //console.log('Without refresh service....');
      this.route.params.subscribe(params =>{
        const categoryID = +params['categorieID'];
        if(!isNaN(categoryID)){
          this.categoryID = categoryID;
          this.fetchDocuments2(categoryID);
        }
      });
      //this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
    }

    this.refreshSubscription = this.refresherService.shouldRefresh$.subscribe((shouldRefresh) => {
      if (shouldRefresh) {
        //console.log('Refresh requested...');
        this.displayWelcome = false;
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
                  (this.filterService.getFilter() === '' || this.filterService.getFilter() === 'all') && 
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
            //console.log('FetchDocuments completed.');
            this.refresherService.clearRefresh(); // Nettoyer le rafraîchissement après le traitement
          },
          error: (error) => {
            //console.error('Error loading documents', error);
          }
        });
        //this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
      }
    });

   
    this.orderSubscription = this.orderService.order$.subscribe(order => {
      if(order.trim() !== ''){
        this.displayWelcome = false;
        this.getDocumentsByOrder(order);
        this.categoryID = null;
        this.orderService.setOrder('');
      }
      else if(this.categoryID !== null){
        this.fetchDocuments(this.categoryID);
      }
      //this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
    });

    this.filterSubscription = this.filterService.filter$.subscribe(filter =>{
      if(filter.trim() !== ''){
        this.displayWelcome = false;
        this.getDocumentsByFilter(filter);
        this.categoryID = null;
        this.filterService.setFilter('');
      }
      else if (this.categoryID !== null){
        this.fetchDocuments(this.categoryID);
      }
      //this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
    });

    
    this.routeSubscription = this.searchService.searchKeyword$.subscribe(keywords =>{
      if(keywords.trim() !== ''){
        //this.paginateDocuments({ pageIndex: 0, pageSize: 3, length: this.documents.length });
        this.displayWelcome = false;
        this.getDocumentBySearch(keywords);
        this.categoryID = null;
        this.searchService.setSearchKeyword('');
        
      }
      else if (this.categoryID !== null){
        this.fetchDocuments(this.categoryID);
      }
      //this.paginateDocuments({ pageIndex: 0, pageSize: 3, length: this.documents.length });
    });



    
    
  }



  paginateDocuments(event: PageEvent): void {
    const startIndex = event.pageIndex * event.pageSize;
    const endIndex = startIndex + event.pageSize;
    this.paginatedDocuments = this.documents.slice(startIndex, endIndex);
  }
  

  fetchDocuments(categorieID: number): Observable<null> {
    this.router.navigate(['', categorieID]);
    this.displayWelcome = false;
    return this.documentService.getDocumentsByCategorie(categorieID)
      .pipe(
        switchMap((documents: Document[]) => {
          // Mise à jour des documents filtrés
          this.documents = documents.filter(doc => !doc.supprimerDocument);
          this.tempDocuments = this.documents; // Mettre à jour tempDocuments si nécessaire
          this.catDocuments = this.documents;
          this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
  
          // Exemple de mise à jour d'un service ou autre logique nécessaire après récupération des documents
          this.categorieService.setFlag(false);
  
          // Retourner un observable vide ou une autre valeur si nécessaire
          return of(null);
        })
      );
  }


  fetchDocuments2(categorieID: number) {
    this.router.navigate(['', categorieID]);
    this.displayWelcome = false;
    //this.tempDocuments = this.documents;
    this.documents = [];
    this.documentService.getDocumentsByCategorie(categorieID)
      .subscribe((documents: Document[]) => {
        //console.log('Docs Before: ', this.tempDocuments);
        this.documents = documents.filter(doc => !doc.supprimerDocument);
        this.tempDocuments = this.documents;
        this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
      }, (error: any) => {
        //console.error('Error loading documents', error);
      });

      
      this.categorieService.setFlag(false);
  }


  getDocumentUrl(id?: number): string{
    return this.documentService.getDocumentUrl(id);
  }

  downloadDocument() {
    this.documentService.downloadDocument(this.documentID).subscribe(data => {
      const blob = new Blob([data], { type: 'application/octet-stream' });
      this.downloadUrl = window.URL.createObjectURL(blob);
      
    });
  }

  getDocumentBySearch(criteres: string): void{
    this.displayWelcome = false;
    //this.tempDocuments = this.documents;
    this.documents = [];
    this.documentService.getDocumentByCriteria(criteres).subscribe((documents: Document[]) => {
      this.documents = documents.filter(doc => !doc.supprimerDocument);
      if(this.tempDocuments.length > 0 && this.catDocuments.length > 0){
        this.searchResults = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
        //console.log('Search in category: ', this.searchResults);
        this.documents = this.searchResults;
        this.categorieService.setFlag(true);
        //this.paginateDocuments({ pageIndex: 0, pageSize: 3, length: this.documents.length });
      }else{
        this.tempDocuments = this.documents;
      }
      this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
    },(error: any) =>{
      //console.error('Error loading documents', error);
      
    });
  }

  getDocumentsByFilter(type: string): void{
    this.displayWelcome = false;
    //this.tempDocuments = this.documents;
    //this.documents = [];
    this.documentService.getDocumentsByType(type).subscribe((documents: Document[]) =>{
      this.documents = documents.filter(doc => !doc.supprimerDocument);
      if(this.tempDocuments.length > 0 && this.catDocuments.length > 0){
        this.filteredDocuments = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
        //console.log('Filter in category: ', this.filteredDocuments);
        //console.log('Filter type: ', type);
        this.documents = this.filteredDocuments;
        this.categorieService.setFlag(true);
        //this.paginateDocuments({ pageIndex: 0, pageSize: 3, length: this.documents.length });

      }
      // else if(this.tempDocuments.length > 0 && this.catDocuments.length === 0){
      //   this.filteredDocuments = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
      //   console.log('Filter in category: ', this.filteredDocuments);
      //   console.log('Filter type: ', type);
      //   this.documents = this.filteredDocuments;
      //   this.categorieService.setFlag(true);
      // }
      else{
        this.tempDocuments = this.documents;
      }
      this.paginateDocuments({ pageIndex: 0, pageSize: 3, length: this.documents.length });
    }, (error: any) =>{
      //console.log('Error fetching documents ', error);
    });
  }

  getDocumentsByOrder(order: string): void{
    this.displayWelcome = false;
    //this.tempDocuments = this.documents;
    this.documents = [];
    this.documentService.getDocumentsByOrder(order).subscribe((documents: Document[]) =>{
      this.documents = documents.filter(doc => !doc.supprimerDocument);
      if(this.tempDocuments.length > 0 && this.catDocuments.length > 0){
        this.orderedDocuments = this.documents.filter(obj1 => this.tempDocuments.some(obj2 => obj1.documentID === obj2.documentID));
        this.documents = this.orderedDocuments;
      }else{
        this.tempDocuments = this.documents;
      }
      this.paginateDocuments({ pageIndex: 0, pageSize: 12, length: this.documents.length });
      
    }, (error: any ) => {
      //console.log('Error fetching documents ', error);
    })
  }


  getBlobDocument(id?: number): string {
    return this.documentService.getBlobDocument(id);
  }

  // getDocumentThumbnail(documentId?: number): string {
  //   return this.documentService.getDocumentThumbnail(documentId);
  // }

  getDocumentThumbnail(documentId?: number): string {
    return this.documentService.getDocumentThumbnail(documentId);
  }

  onDownloadBtnClick(fileName?: string, documentID?: number): void {
    // Handle the click event, you have access to the document ID (documentId) here
    //console.log('Clicked document ID:', documentID);
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
        //console.error('Error downloading document:', error);
      }
      );
    }else{
      //console.error('File name is undefined.');
    }
    
    
  }

  getFileExtensionFromUrl(url: string): string {
    const pathArray = url.split('.');
    return pathArray[pathArray.length - 1];
  }

 

  onDocumentClicked(document: Document): void {
    console.log('URL:',this.router.url);
    this.navigationService.setPreviousUrl(this.router.url);
    this.documentClicked.emit(document);
    // setTimeout(() => {
    //   this.router.navigate([`/${this.categoryID}/${document.documentID}`]);
    // }, 0);
    window.history.pushState({}, '', `/${this.categoryID}/${document.documentID}`);
    
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
    //console.log('PDF loaded successfully', event);
    
  }

  handlePdfError(event: any){
    //console.error('Error loadind PDF', event);
    
  }

  getSafeUrl(url: string |  null): SafeResourceUrl | undefined {
    // Check if the URL is defined before sanitizing
    return url ? this.sanitizer.bypassSecurityTrustResourceUrl(url) : undefined;
  }

  openDetailDialog(document: Document): void {
    const dialogRef = this.dialog.open(DetailPartComponent, {
      //width: '30%',
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

  // copyLink(){
  //   const link = window.location.href;
  //   console.log(link);
  //   navigator.clipboard.writeText(link).then(() => {
  //     alert("Lien copié dans le clipboard...");
  //   }).catch(err =>{
  //     console.error("Echec de copier le lien...");
  //   });
  // }

  copyLink() {
    const url = window.location.href;
    const tempInput = document.createElement('input');
    tempInput.value = url;
    document.body.appendChild(tempInput);

    tempInput.select();
    tempInput.setSelectionRange(0, 99999); // Pour les appareils mobiles
  
    try {
      // Essayez de copier le texte dans le presse-papiers
      const successful = document.execCommand('copy');
      if (successful) {
        //console.log('URL copied successfully');
        this.translateService.get("URL copiée dans le presse-papier").subscribe(translateMsg => {
          this.showSnackBar(translateMsg);
        });
      } else {
        //console.error('Failed to copy URL');
        this.translateService.get("Echec de copier URL dans le presse-papier").subscribe(translateMsg => {
          this.showSnackBar(translateMsg);
        });
      }
    } catch (err) {
      //console.error('Error copying URL:', err);
      this.translateService.get("Echec de copier URL dans le presse-papier").subscribe(translateMsg => {
        this.showSnackBar(translateMsg);
      });
    }
  
    // Nettoyez l'élément temporaire
    document.body.removeChild(tempInput);
  }

  private showSnackBar(message: string) {
    this.snackBar.open(message, 'Fermer', {
      duration: 2000, // Duration in milliseconds
      horizontalPosition: 'center',
      verticalPosition: 'top'
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

  trackById(index: number, item: Document): number | undefined{
    return item.documentID
  }




}
