import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Categorie } from 'src/app/models/categorie';
import { CategorieService } from 'src/app/services/categorie.service';
import { MatDialog } from '@angular/material/dialog';
import { EditCategoryModalComponent } from './edit-category-modal/edit-category-modal.component';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from 'src/app/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-delupcategory-part',
  templateUrl: './delupcategory-part.component.html',
  styleUrls: ['./delupcategory-part.component.scss']
})
export class DelupcategoryPartComponent implements OnInit{
  displayedColumns = ['id', 'nom', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  categorySource: MatTableDataSource<Categorie> = new MatTableDataSource<Categorie>([]);
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
              private snackBar: MatSnackBar,
              private cdRef: ChangeDetectorRef
            ){
    // const cat: Array<Categorie> = [];
    // this.categorieService.getAllCategories().subscribe(
    //   (categories: Categorie[]) =>{
    //     for(const category of categories){
    //       cat.push(category);
    //     }
    //     this.categorySource = new MatTableDataSource(cat);
    //     this.categorySource.paginator = this.paginator;
    //     this.categorySource.sort = this.sort;
    //   },
    //   (error) => {
    //     //console.error('Erreur: ',error);
        
    //   }
    // );
    // this.categorySource = new MatTableDataSource(this.categories);
  }

  ngOnInit(): void {
    // Charger les categories depuis le service
    this.categorieService.getAllCategories().subscribe((categories: Categorie[]) => {
      this.categories = categories;
      
      // Initialiser MatTableDataSource avec les données reçues
      this.categorySource = new MatTableDataSource(this.categories);

      // Appliquer la pagination et le tri
      this.categorySource.paginator = this.paginator;
      this.categorySource.sort = this.sort;
    }, (error) => {
      console.error('Error fetching documents : ', error);
    });
  }
    
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
  
    this.categorySource.filterPredicate = (data: Categorie, filter: string) => {
      const nom = data.nom ? data.nom.toLowerCase() : ''; // Vérifier si 'nom' existe, sinon on prend une chaîne vide
      
      
      return nom.includes(filter); // Appliquer le filtre
    };
  
    this.categorySource.filter = filterValue; // Applique le filtre aux données de la table
  
    // Si le tableau n'affiche plus de résultats après application du filtre
    if (this.categorySource.paginator) {
      this.categorySource.paginator.firstPage();
    }
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
          //console.error('Erreur: ',error);
          
        }
      );
      if(updatedCategorie){
        // Logique pour gérer les données mises à jour
        //console.log('Dialog closed with data:', updatedCategorie);

        // this.categorieService.creerCategorie(categorie.categorieID, this.adminID, updatedCategorie).subscribe(response => {
        //   const index = this.categories.findIndex(c => c.categorieID === response.categorieID);
        //   if(index !== -1){
        //     this.categories[index] = response;
        //   }
        // })
      }
      
    },
    error => {
      //console.error('Error updating categorie ', error);
    });
  
  }

  updateTableAfterDeletion(categorieID: number | undefined) {
    // Récupérer les données actuelles sous forme de tableau
    const data = this.categorySource.data;
  
    // Mettre à jour le champ `supprimerUtil` du document pour le marquer comme supprimé
    const updatedData = data.map(cat => {
      if (cat.categorieID === categorieID) {
        cat.supprimerCategorie= true;  // Marquer comme supprimé
      }
      return cat;
    });
  
    // Mettre à jour la source de données de la table
    this.categorySource.data = updatedData;
  
    // Forcer la détection des changements
    this.cdRef.detectChanges();
  }
  
  // onDelete(categorie: Categorie){

  //   const config = new MatSnackBarConfig();
  //   config.duration = 4000; // Durée de la notification en millisecondes
  //   config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
  //   config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
  //   config.panelClass = ['custom-snackbar'];
    
  //   this.categorieService.supCategorie(categorie.categorieID, categorie).subscribe({
  //     next: data => {
  //       this.msg = 'Catégorie suprrimée avec succès✅';
  //       this.snackBar.open(this.msg, 'Fermer', config);
  //       //console.log("Suppresion de la catégorie: ", data);
  //       this.updateTableAfterDeletion(categorie.categorieID);
  //     },
  //     error: err => {
  //       this.msg = 'Échec de Supprimer la Catégorie❌';
  //       this.snackBar.open(this.msg, 'Fermer', config);
  //       //console.log("Echec de suppresion de la catégorie: ", err);
  //     }
  //   });
  // }


  onDelete(categorie: Categorie) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Confirmation de suppression',
        message: `Vous êtes sur le point de supprimer la catégorie "${categorie.nom}". Tous les éléments associés pourraient être affectés.`,
        confirmText: 'Supprimer',
        cancelText: 'Annuler'
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.executeCategoryDeletion(categorie);
      }
    });
  }

private executeCategoryDeletion(categorie: Categorie) {
  const config: MatSnackBarConfig = {
    duration: 4000,
    horizontalPosition: 'center',
    verticalPosition: 'top',
    panelClass: ['custom-snackbar']
  };

  //this.isDeleting = true; // Si vous utilisez un spinner

  this.categorieService.supCategorie(categorie.categorieID, categorie).subscribe({
    next: () => {
      this.snackBar.open('Catégorie supprimée avec succès ✅', 'Fermer', {
        ...config,
        panelClass: ['custom-snackbar', 'success']
      });
      this.updateTableAfterDeletion(categorie.categorieID);
      //this.isDeleting = false;
    },
    error: (err) => {
      this.snackBar.open('Échec de la suppression de la catégorie ❌', 'Fermer', {
        ...config,
        panelClass: ['custom-snackbar', 'error']
      });
      console.error("Échec de suppression de la catégorie: ", err);
      //this.isDeleting = false;
    }
  });
}

  reloadPage(): void{
    window.location.reload();
  }


}
