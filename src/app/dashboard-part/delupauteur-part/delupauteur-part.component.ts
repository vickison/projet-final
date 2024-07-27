import { Component, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { AuteurService } from 'src/app/services/auteur.service';
import { Auteur } from 'src/app/models/auteur.model';
import { EditAuteurModalComponent } from './edit-auteur-modal/edit-auteur-modal.component';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-delupauteur-part',
  templateUrl: './delupauteur-part.component.html',
  styleUrls: ['./delupauteur-part.component.scss']
})
export class DelupauteurPartComponent {

  displayedColumns = ['id', 'nom', 'prenom', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  auteurSource: MatTableDataSource<Auteur>;
  auteurs: Auteur[] = [];
  filterValue: string = "";
  adminID: number = 0;
  message: String = '';
  classCss: String = '';
  msg = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private auteurService: AuteurService, 
              private dialog: MatDialog,
              private snackBar: MatSnackBar
            ){
    const aus: Array<Auteur> = [];
    this.auteurService.getAllAuteurs().subscribe(
      (auteurs: Auteur[]) =>{
        for (const auteur of auteurs) {
          aus.push(auteur);
        }
        this.auteurSource = new MatTableDataSource(aus);
        this.auteurSource.paginator  = this.paginator;
        this.auteurSource.sort = this.sort;
      },
      (error) => {
        console.error('Erreur: ', error);
      }
    );

    this.auteurSource = new MatTableDataSource(this.auteurs);
  }

  ngAfterViewInit() {
    this.auteurSource.paginator = this.paginator;
    this.auteurSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.auteurSource.filter = filterValue;
  }

  openEditModal(auteur: Auteur): void {
    const dialogRef = this.dialog.open(EditAuteurModalComponent, {
      width: '40%',
     // data: {user} // Passer les données de l'utilisateur à éditer
      data: {auteur}
    });

    dialogRef.afterClosed().subscribe(updatedAuteur => {
      const aus: Array<Auteur> = [];
      this.auteurService.getAllAuteurs().subscribe(
        (auteurs: Auteur[]) =>{
          for (const auteur of auteurs) {
            aus.push(auteur);
          }
          this.auteurSource = new MatTableDataSource(aus);
          this.auteurSource.paginator  = this.paginator;
          this.auteurSource.sort = this.sort;
        },
        (error) => {
          console.error('Erreur: ', error);
        }
      );
      if(updatedAuteur){
        // Logique pour gérer les données mises à jour
        console.log('Dialog closed with data:', updatedAuteur);

        this.auteurService.modifAuteur(auteur.auteurID, updatedAuteur).subscribe(response => {
          const index = this.auteurs.findIndex(a => a.auteurID === response.auteurID);
          if(index ! == -1){
            this.auteurs[index] = response;
          }
        })

      }
      
    }, error => {
      console.error('Error updating auteur ', error);
      
    });
  
  }

  onDelete(auteurID: number, auteur: Auteur){

    const config = new MatSnackBarConfig();
    config.duration = 4000; // Durée de la notification en millisecondes
    config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
    config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
    config.panelClass = ['custom-snackbar'];
    
    this.auteurService.supAuteur(auteurID, auteur).subscribe({
      next: data => {
        this.msg = 'Auteur suprrimé avec succès✅';
        this.snackBar.open(this.msg, 'Fermer', config);
        console.log("Suppresion de Auteur: ", data);
        setTimeout(() => {
          this.reloadPage();
        }, 500);
      },
      error: err => {
        this.msg = 'Échec de Supprimer l\'Auteur❌';
        this.snackBar.open(this.msg, 'Fermer', config);
        console.log("Echec de suppresion de l\'auteur: ", err);
      }
    });
  }

  reloadPage(): void{
    window.location.reload();
  }

}
