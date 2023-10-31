import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { CategorieService } from 'src/app/services/categorie.service';
import { Categorie } from 'src/app/models/categorie';
import { CategorieDocument } from 'src/app/models/categorie-document.model';
import { ActivatedRoute, Router } from '@angular/router';
import { DocumentService } from 'src/app/services/document.service';
import { Document } from 'src/app/models/document.model';

@Component({
  selector: 'app-left-part',
  templateUrl: './left-part.component.html',
  styleUrls: ['./left-part.component.scss']
})
export class LeftPartComponent implements OnInit{
  isMobile: boolean = false;
  menuOpen: boolean = false;
  isLeftSectionVisible: boolean = true; // Initialisez la visibilité à true
  categories?: Categorie[];
  categorieID?: number;
  categorieDocuments?: CategorieDocument[];
  documents?: Document[] = [];

  constructor(
    private breakpointObserver: BreakpointObserver,
    private categorieService: CategorieService,
    private route: ActivatedRoute,
    private router: Router,
    private documentService: DocumentService
  ) { }

  ngOnInit() {
    this.breakpointObserver.observe([Breakpoints.Handset])
      .subscribe(result => {
        this.isMobile = result.matches;
        console.log('isLeftSectionVisible:', this.isLeftSectionVisible);
         if (!this.isMobile) {
          this.isLeftSectionVisible = false;
           this.menuOpen = false; // Fermer le menu sur les autres écrans
         }
      });

    this.getAllCategories();
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen; // Inverse l'état du menu
    this.isLeftSectionVisible = !this.isLeftSectionVisible; // Inverse l'état de visibilité
    console.log('menuOpen:', this.menuOpen);
  }

  getAllCategories(): void {
    this.categorieService.getAllCategories()
      .subscribe({
        next: (data) => {
          this.categories = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  onCategoryClick(categoryID?: number): void {
      this.documentService.getDocumentsByCategorie(categoryID)
        .subscribe(documents => {
          this.documents = documents;
          console.log(documents);
          this.router.navigate(['/categories', categoryID]);
      });
  }
  
}







