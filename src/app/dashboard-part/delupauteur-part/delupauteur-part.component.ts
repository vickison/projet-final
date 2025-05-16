import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { AuteurService } from 'src/app/services/auteur.service';
import { Auteur } from 'src/app/models/auteur.model';
import { EditAuteurModalComponent } from './edit-auteur-modal/edit-auteur-modal.component';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from 'src/app/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-delupauteur-part',
  templateUrl: './delupauteur-part.component.html',
  styleUrls: ['./delupauteur-part.component.scss']
})
export class DelupauteurPartComponent implements OnInit{

  displayedColumns = ['id', 'nom', 'prenom', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  auteurSource: MatTableDataSource<Auteur> = new MatTableDataSource<Auteur>([]);
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
              private snackBar: MatSnackBar,
              private cdRef: ChangeDetectorRef
            ){}

  ngOnInit(): void {
    // Charger les categories depuis le service
    this.auteurService.getAllAuteurs().subscribe((auteurs: Auteur[]) => {
      this.auteurs = auteurs;
      
      // Initialiser MatTableDataSource avec les données reçues
      this.auteurSource = new MatTableDataSource(this.auteurs);

      // Appliquer la pagination et le tri
      this.auteurSource.paginator = this.paginator;
      this.auteurSource.sort = this.sort;
    }, (error) => {
      console.error('Error fetching documents : ', error);
    });
  }
    
  applyFilter2(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
  
    this.auteurSource.filterPredicate = (data: Auteur, filter: string) => {
      const nom = data.nom ? data.nom.toLowerCase() : ''; // Vérifier si 'nom' existe, sinon on prend une chaîne vide
      
      
      return nom.includes(filter); // Appliquer le filtre
    };
  
    this.auteurSource.filter = filterValue; // Applique le filtre aux données de la table
  
    // Si le tableau n'affiche plus de résultats après application du filtre
    if (this.auteurSource.paginator) {
      this.auteurSource.paginator.firstPage();
    }
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
          //console.error('Erreur: ', error);
        }
      );
      if(updatedAuteur){
        // Logique pour gérer les données mises à jour
        //console.log('Dialog closed with data:', updatedAuteur);

        this.auteurService.modifAuteur(auteur.auteurID, updatedAuteur).subscribe(response => {
          const index = this.auteurs.findIndex(a => a.auteurID === response.auteurID);
          if(index ! == -1){
            this.auteurs[index] = response;
          }
        })

      }
      
    }, error => {
      //console.error('Error updating auteur ', error);
      
    });
  
  }

  updateTableAfterDeletion(auteurID: number) {
    // Récupérer les données actuelles sous forme de tableau
    const data = this.auteurSource.data;
  
    // Mettre à jour le champ `supprimerUtil` du document pour le marquer comme supprimé
    const updatedData = data.map(au => {
      if (au.auteurID === auteurID) {
        au.supprimerAuteur = true;  // Marquer comme supprimé
      }
      return au;
    });
  
    // Mettre à jour la source de données de la table
    this.auteurSource.data = updatedData;
  
    // Forcer la détection des changements
    this.cdRef.detectChanges();
  }

  // onDelete(auteurID: number, auteur: Auteur){

  //   const config = new MatSnackBarConfig();
  //   config.duration = 4000; // Durée de la notification en millisecondes
  //   config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
  //   config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
  //   config.panelClass = ['custom-snackbar'];
    
  //   this.auteurService.supAuteur(auteurID, auteur).subscribe({
  //     next: data => {
  //       this.msg = 'Auteur suprrimé avec succès✅';
  //       this.snackBar.open(this.msg, 'Fermer', config);
  //       //console.log("Suppresion de Auteur: ", data);
  //       this.updateTableAfterDeletion(auteurID);
  //     },
  //     error: err => {
  //       this.msg = 'Échec de Supprimer l\'Auteur❌';
  //       this.snackBar.open(this.msg, 'Fermer', config);
  //       //console.log("Echec de suppresion de l\'auteur: ", err);
  //     }
  //   });
  // }

  onDelete(auteurID: number, auteur: Auteur) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '450px',
      disableClose: true,
      autoFocus: false,
      data: {
        title: 'Confirmation de suppression',
        message: this.generateAuteurDeleteMessage(auteur),
        confirmText: 'Confirmer la suppression',
        cancelText: 'Annuler',
        warning: true
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.executeAuteurDeletion(auteurID, auteur);
      }
    });
  }

  private generateAuteurDeleteMessage(auteur: Auteur): string {
    let baseMsg = `Vous allez supprimer l'auteur "${auteur.prenom} ${auteur.nom}".`;
    
    // if (auteur.nombreLivres > 0) {
    //   baseMsg += ` Attention : cet auteur est associé à ${auteur.nombreLivres} livre(s).`;
    // }
    
    return baseMsg + ' Cette action est irréversible.';
  }

private executeAuteurDeletion(auteurID: number, auteur: Auteur) {
  // Définir le type explicitement comme tableau de strings
  const panelClasses: string[] = ['custom-snackbar'];
  
  const config: MatSnackBarConfig = {
    duration: 5000,
    horizontalPosition: 'center',
    verticalPosition: 'top',
    panelClass: panelClasses // Utilisation du tableau typé
  };

  //this.isDeleting = true;

  this.auteurService.supAuteur(auteurID, auteur).subscribe({
    next: () => {
      this.msg = `Auteur "${auteur.prenom} ${auteur.nom}" supprimé avec succès ✅`;
      this.snackBar.open(this.msg, 'Fermer', {
        ...config,
        panelClass: [...panelClasses, 'success'] // Spread du tableau typé
      });
      this.updateTableAfterDeletion(auteurID);
      //this.isDeleting = false;
    },
    error: (err) => {
      const errorMsg = err.error?.message || 'Une erreur est survenue';
      this.msg = `Échec de suppression : ${errorMsg} ❌`;
      this.snackBar.open(this.msg, 'Fermer', {
        ...config,
        panelClass: [...panelClasses, 'error'],
        duration: 7000
      });
      console.error(`Échec suppression auteur ID ${auteurID}:`, err);
      //this.isDeleting = false;
    }
  });
}

  reloadPage(): void{
    window.location.reload();
  }

}
