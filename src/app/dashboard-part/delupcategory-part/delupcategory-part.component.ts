import { Component, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Categorie } from 'src/app/models/categorie';
import { CategorieService } from 'src/app/services/categorie.service';
import { MatDialog } from '@angular/material/dialog';
import { EditCategoryModalComponent } from './edit-category-modal/edit-category-modal.component';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-delupcategory-part',
  templateUrl: './delupcategory-part.component.html',
  styleUrls: ['./delupcategory-part.component.scss']
})
export class DelupcategoryPartComponent {
  displayedColumns = ['id', 'nom', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  categorySource: MatTableDataSource<Categorie>;
  categories: Categorie[] = [];
  adminID: number = 0;
  message: String = '';
  classCss: String = '';
  msg = '';
  //deletedCategorieData: Categorie;


  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(private categorieService: CategorieService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar
            ){
    const cat: Array<Categorie> = [];
    this.categorieService.getAllCategories().subscribe(
      (categories: Categorie[]) =>{
        for(const category of categories){
          cat.push(category);
        }
        this.categorySource = new MatTableDataSource(cat);
        this.categorySource.paginator = this.paginator;
        this.categorySource.sort = this.sort;
      },
      (error) => {
        console.error('Erreur: ',error);
        
      }
    );
    this.categorySource = new MatTableDataSource(this.categories);
  }

    
  openEditModal(categorie: Categorie): void {
    const dialogRef = this.dialog.open(EditCategoryModalComponent, {
      width: '40%',
      // data: {user} // Passer les données de l'utilisateur à éditer
      data: {categorie}
    });

    dialogRef.afterClosed().subscribe(updatedCategorie => {
        const cat: Array<Categorie> = [];
      this.categorieService.getAllCategories().subscribe(
        (categories: Categorie[]) =>{
          for(const category of categories){
            cat.push(category);
          }
          this.categorySource = new MatTableDataSource(cat);
          this.categorySource.paginator = this.paginator;
          this.categorySource.sort = this.sort;
        },
        (error) => {
          console.error('Erreur: ',error);
          
        }
      );
      if(updatedCategorie){
        // Logique pour gérer les données mises à jour
        console.log('Dialog closed with data:', updatedCategorie);

        // this.categorieService.creerCategorie(categorie.categorieID, this.adminID, updatedCategorie).subscribe(response => {
        //   const index = this.categories.findIndex(c => c.categorieID === response.categorieID);
        //   if(index !== -1){
        //     this.categories[index] = response;
        //   }
        // })
      }
      
    },
    error => {
      console.error('Error updating categorie ', error);
    });
  
  }

  onDelete(categorie: Categorie){

    const config = new MatSnackBarConfig();
    config.duration = 4000; // Durée de la notification en millisecondes
    config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
    config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
    config.panelClass = ['custom-snackbar'];
    
    this.categorieService.supCategorie(categorie.categorieID, categorie).subscribe({
      next: data => {
        this.msg = 'Catégorie suprrimée avec succès✅';
        this.snackBar.open(this.msg, 'Fermer', config);
        console.log("Suppresion de la catégorie: ", data);
        setTimeout(() => {
          this.reloadPage();
        }, 500);
      },
      error: err => {
        this.msg = 'Échec de Supprimer la Catégorie❌';
        this.snackBar.open(this.msg, 'Fermer', config);
        console.log("Echec de suppresion de la catégorie: ", err);
      }
    });
  }

  reloadPage(): void{
    window.location.reload();
  }


}
