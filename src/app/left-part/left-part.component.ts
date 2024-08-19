import { ChangeDetectionStrategy, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { CategorieService } from 'src/app/services/categorie.service';
import { Categorie } from 'src/app/models/categorie';
import { CategorieDocument } from 'src/app/models/categorie-document.model';
import { ActivatedRoute, Router } from '@angular/router';
import { DocumentService } from 'src/app/services/document.service';
import { Document } from 'src/app/models/document.model';
import { CategorieDocumenttsService } from '../services/categorie-documentts.service';
import { MenuService } from '../services/menu-service.service';
import { FilterService } from '../services/filter.service';
import { RefresherService } from '../services/refresher.service';

@Component({
  selector: 'app-left-part',
  templateUrl: './left-part.component.html',
  styleUrls: ['./left-part.component.scss']
})
export class LeftPartComponent implements OnInit{
  isMobile: boolean = false;
  isMenuOpen: boolean = false;
  isLeftSectionVisible: boolean = true; // Initialisez la visibilité à true
  categories?: Categorie[];
  categorieID?: number;
  categorieDocuments?: CategorieDocument[];
  documents?: Document[] = [];
  activeId: number | undefined;
  flag?: boolean

  @Output() categorySelected = new EventEmitter<number>();
  @Output() documentsOfCategorie = new EventEmitter<Document[]>();

  

  constructor(
    private breakpointObserver: BreakpointObserver,
    private categorieService: CategorieService,
    private route: ActivatedRoute,
    private router: Router,
    private documentService: DocumentService,
    private menuService: MenuService,
    private categorieDocumenttsService: CategorieDocumenttsService,
    private refresherService: RefresherService,
  ) { }

  ngOnInit() {
    this.breakpointObserver.observe([Breakpoints.Handset])
      .subscribe(result => {
        this.isMobile = result.matches;
        console.log('isLeftSectionVisible:', this.isLeftSectionVisible);
         if (!this.isMobile) {
          this.isLeftSectionVisible = false;
           this.isMenuOpen = false; // Fermer le menu sur les autres écrans
         }
      });
      this.menuService.isMenuOpen$.subscribe(isOpen => {
        this.isMenuOpen = isOpen;
        this.isLeftSectionVisible = !isOpen;
      });

    this.getAllCategories();
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen; // Inverse l'état du menu
    this.isLeftSectionVisible = !this.isLeftSectionVisible; // Inverse l'état de visibilité
    console.log('menuOpen:', this.isMenuOpen);
  }

  getAllCategories(): void {
    this.categorieService.getAllCategories()
      .subscribe({
        next: (data) => {
          this.categories = data.filter(cat => !cat.supprimerCategorie);
          console.log(this.categories);
        },
        error: (e) => console.error(e)
      });
  }

  // setActive(event: any) {
  //   // Remove the 'active' class from all links
  //   const links = document.querySelectorAll('nav ul li a');
  //   links.forEach(link => link.classList.remove('active'));

  //   // Add the 'active' class to the clicked link
  //   event.target.classList.add('active');
  // }

  onCategoryClick(event: any, categoryID?: number): void {
    const links = document.querySelectorAll('nav ul li a');
    links.forEach(link => link.classList.remove('active'));
    
    //const curFlag = this.categorieService.getFlag();
    this.categorieService.setFlag(false);
    // Add the 'active' class to the clicked link
    event.target.classList.add('active');
    this.activeId = categoryID;
    this.documentService.getDocumentsByCategorie(categoryID)
      .subscribe(documents => {
        this.documents = documents.filter(doc => !doc.supprimerDocument);
        this.documentsOfCategorie.emit(documents);
        //console.log(this.documents);
        this.router.navigate(['/categories', categoryID]);
        //this.reloadPage();
    });


    this.refresherService.triggerRefresh();
      
      
  }

  onClicked(categorieID: number): void{
    this.categorieDocumenttsService.setSelectedCategorieID(categorieID);
  }

  onCategorySelectect(category: Categorie): void{
    this.categorySelected.emit(category.categorieID);
  }

  reloadPage(): void{
    window.location.reload();
  }

  refresh() {
    this.refresherService.triggerRefresh();
  }
  
}







