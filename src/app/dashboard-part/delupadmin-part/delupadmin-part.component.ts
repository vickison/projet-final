import { ChangeDetectorRef, Component, OnInit, Optional, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Utilisateur } from 'src/app/models/utilisateur';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { EditAdminModalComponent } from './edit-admin-modal/edit-admin-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from 'src/app/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-delupadmin-part',
  templateUrl: './delupadmin-part.component.html',
  styleUrls: ['./delupadmin-part.component.scss']
})


export class DelupadminPartComponent implements OnInit{

  displayedColumns = ['id', 'nom', 'prenom', 'email', 'username', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  dataSource: MatTableDataSource<UserData>;
  usersSource: MatTableDataSource<Utilisateur>;
  utilisateurs: Utilisateur[] = [];
  ut: Array<Utilisateur> = [];
  filterValue: string = "";
  message: String = '';
  classCss: String = '';
  msg = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    
    
  }

  constructor( private utilisateurService: UtilisateurService,
               private dialog: MatDialog,
               private snackBar: MatSnackBar,
               private cdRef: ChangeDetectorRef
              ) { 
    
    // Create 100 users
    const utilis: Array<Utilisateur> =[]
    const users: UserData[] = [];
    for (let i = 1; i <= 100; i++) { users.push(createNewUser(i)); }

    this.utilisateurService.getAllUsers().subscribe(
      (utilisateurs: Utilisateur[]) =>{
        for(const utilisateur of utilisateurs){
          utilis.push(utilisateur);
        }
        //console.log(utilis);
        this.usersSource = new MatTableDataSource(utilis)

        this.usersSource.paginator = this.paginator;
        this.usersSource.sort = this.sort;
        
      },
      (error) => {
        //console.error('Erreur: ', error);
      }
    );
    this.usersSource = new MatTableDataSource(this.utilisateurs)

    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(users);
    //this.usersSource = new MatTableDataSource(this.utilisateurs)

    
    
  
  }

  ngAfterViewInit() {
    this.usersSource.paginator = this.paginator;
    this.usersSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.usersSource.filter = filterValue;
  }

  openEditModal(utilisateur: Utilisateur): void {
    utilisateur.password = '';
    const dialogRef = this.dialog.open(EditAdminModalComponent, {
      width: '40%',
     // data: {user} // Passer les données de l'utilisateur à éditer
     data: {utilisateur}
    });

    dialogRef.afterClosed().subscribe(updatedUtil => {
      const utilis: Array<Utilisateur> =[]
      const users: UserData[] = [];
      for (let i = 1; i <= 100; i++) { users.push(createNewUser(i)); }

      this.utilisateurService.getAllUsers().subscribe(
        (utilisateurs: Utilisateur[]) =>{
          for(const utilisateur of utilisateurs){
            utilis.push(utilisateur);
          }
          //console.log(utilis);
          this.usersSource = new MatTableDataSource(utilis)

          this.usersSource.paginator = this.paginator;
          this.usersSource.sort = this.sort;
          
        },
        (error) => {
          //console.error('Erreur: ', error);
        }
      );
      // Logique pour gérer les données mises à jour
      //console.log('Dialog closed with data:', updatedUtil);
      // this.utilisateurService.modifUtilisateur(utilisateur.utilisateurID, updatedUtil).subscribe(response => {
      //   const index = this.utilisateurs.findIndex(a => a.utilisateurID === response.utilisateurID);
      //   if(index ! == -1){
      //     this.utilisateurs[index] = response;
      //   }
      // })
    });
  
  }

  updateTableAfterDeletion(utilisateurID: number) {
    // Récupérer les données actuelles sous forme de tableau
    const data = this.usersSource.data;
  
    // Mettre à jour le champ `supprimerUtil` du document pour le marquer comme supprimé
    const updatedData = data.map(user => {
      if (user.utilisateurID === utilisateurID) {
        user.supprimerUtil = true;  // Marquer comme supprimé
      }
      return user;
    });
  
    // Mettre à jour la source de données de la table
    this.usersSource.data = updatedData;
  
    // Forcer la détection des changements
    this.cdRef.detectChanges();
  }

  // onDelete(utilisateurID: number, utilisateur: Utilisateur){

  //   const config = new MatSnackBarConfig();
  //   config.duration = 4000; // Durée de la notification en millisecondes
  //   config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
  //   config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
  //   config.panelClass = ['custom-snackbar'];

    
  //   this.utilisateurService.supUtilisateur(utilisateurID, utilisateur).subscribe({
  //     next: data => {
  //       this.msg = 'Admin suprrimé avec succès✅';
  //       this.snackBar.open(this.msg, 'Fermer', config);
  //       //console.log("Suppresion de l'utilisateur: ", data);
  //       this.updateTableAfterDeletion(utilisateurID);
  //     },
  //     error: err => {
  //       this.msg = 'Échec de Supprimer cet Admin❌';
  //       this.snackBar.open(this.msg, 'Fermer', config);
  //       //console.log("Echec de suppresion de l\'utilisateur: ", err);
  //     }
  //   });
  // }


  onDelete(utilisateurID: number, utilisateur: Utilisateur) {
  const dialogRef = this.dialog.open(ConfirmDialogComponent, {
    width: '450px',
    disableClose: true,
    autoFocus: false,
    data: {
      title: 'Confirmation de suppression ADMIN',
      message: this.generateUserDeleteMessage(utilisateur),
      confirmText: 'Confirmer la suppression',
      cancelText: 'Annuler',
      warning: true,
      isCritical: true
    }
  });

  dialogRef.afterClosed().subscribe(confirmed => {
    if (confirmed) {
      this.executeUserDeletion(utilisateurID, utilisateur);
    }
  });
}

private generateUserDeleteMessage(utilisateur: Utilisateur): string {
  let baseMsg = `Vous allez supprimer l'administrateur "${utilisateur.username}".`;
  
  // if (utilisateur.role === 'SUPER_ADMIN') {
  //   baseMsg += '\n\n⚠️ ATTENTION : Cet utilisateur a des privilèges élevés !';
  // }
  
  return baseMsg + '\n\nCette action est irréversible et peut affecter le système.';
}

private executeUserDeletion(utilisateurID: number, utilisateur: Utilisateur) {
  // Déclarer explicitement le tableau de classes
  const basePanelClasses: string[] = ['custom-snackbar'];
  
  const config: MatSnackBarConfig = {
    duration: 6000,
    horizontalPosition: 'center',
    verticalPosition: 'top',
    panelClass: basePanelClasses // Utilisation du tableau typé
  };

  //this.isDeleting = true;

  this.utilisateurService.supUtilisateur(utilisateurID, utilisateur).subscribe({
    next: () => {
      this.msg = `Administrateur "${utilisateur.username}" supprimé avec succès ✅`;
      this.snackBar.open(this.msg, 'Fermer', {
        ...config,
        panelClass: [...basePanelClasses, 'success'] // Spread du tableau typé
      });
      this.updateTableAfterDeletion(utilisateurID);
      //this.isDeleting = false;
      
      // if (this.currentUser.id === utilisateurID) {
      //   this.authService.logout();
      // }
    },
    error: (err) => {
      const errorMsg = err.error?.message || 'Erreur système';
      this.msg = `Échec de suppression : ${errorMsg} ❌`;
      this.snackBar.open(this.msg, 'Fermer', {
        ...config,
        panelClass: [...basePanelClasses, 'error'],
        duration: 8000
      });
      console.error(`Échec suppression admin ID ${utilisateurID}:`, err);
      //this.isDeleting = false;
    }
  });
}

  reloadPage(): void{
    window.location.reload();
  }


}


function createNewUser(id: number): UserData {
  const nom =
      NAMES[Math.round(Math.random() * (NAMES.length - 1))] + ' ' +
      NAMES[Math.round(Math.random() * (NAMES.length - 1))].charAt(0) + '.';

  return {
    id: id.toString(),
    nom: nom,
    prenom: Math.round(Math.random() * 100).toString(),
    email: COLORS[Math.round(Math.random() * (COLORS.length - 1))],
    password: Math.round(Math.random() * 100).toString(),
  };
}

/** Constants used to fill up our data base. */
const COLORS = ['maroon', 'red', 'orange', 'yellow', 'olive', 'green', 'purple',
  'fuchsia', 'lime', 'teal', 'aqua', 'blue', 'navy', 'black', 'gray'];
const NAMES = ['Maia', 'Asher', 'Olivia', 'Atticus', 'Amelia', 'Jack',
  'Charlotte', 'Theodore', 'Isla', 'Oliver', 'Isabella', 'Jasper',
  'Cora', 'Levi', 'Violet', 'Arthur', 'Mia', 'Thomas', 'Elizabeth'];

  export interface UserData {
    id: string;
    nom: string;
    prenom: string;
    email: string;
    password: string;
  }
