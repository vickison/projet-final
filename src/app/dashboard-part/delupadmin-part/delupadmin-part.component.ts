import { Component, OnInit, Optional, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Utilisateur } from 'src/app/models/utilisateur';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { EditAdminModalComponent } from './edit-admin-modal/edit-admin-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-delupadmin-part',
  templateUrl: './delupadmin-part.component.html',
  styleUrls: ['./delupadmin-part.component.scss']
})


export class DelupadminPartComponent implements OnInit{

  displayedColumns = ['id', 'nom', 'prenom', 'email', 'username', 'action'];
  dataSource: MatTableDataSource<UserData>;
  usersSource: MatTableDataSource<Utilisateur>;
  utilisateurs: Utilisateur[] = [];
  ut: Array<Utilisateur> = [];
  filterValue: string = "";
  message: String = '';
  classCss: String = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    
    
  }

  constructor( private utilisateurService: UtilisateurService,
               private dialog: MatDialog) { 
    
    // Create 100 users
    const utilis: Array<Utilisateur> =[]
    const users: UserData[] = [];
    for (let i = 1; i <= 100; i++) { users.push(createNewUser(i)); }

    this.utilisateurService.getAllUsers().subscribe(
      (utilisateurs: Utilisateur[]) =>{
        for(const utilisateur of utilisateurs){
          utilis.push(utilisateur);
        }
        console.log(utilis);
        this.usersSource = new MatTableDataSource(utilis)

        this.usersSource.paginator = this.paginator;
        this.usersSource.sort = this.sort;
        
      },
      (error) => {
        console.error('Erreur: ', error);
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
    const dialogRef = this.dialog.open(EditAdminModalComponent, {
      width: '40%',
     // data: {user} // Passer les données de l'utilisateur à éditer
     data: {utilisateur}
    });

    dialogRef.afterClosed().subscribe(updatedUtil => {
      // Logique pour gérer les données mises à jour
      console.log('Dialog closed with data:', updatedUtil);
      // this.utilisateurService.modifUtilisateur(utilisateur.utilisateurID, updatedUtil).subscribe(response => {
      //   const index = this.utilisateurs.findIndex(a => a.utilisateurID === response.utilisateurID);
      //   if(index ! == -1){
      //     this.utilisateurs[index] = response;
      //   }
      // })
    });
  
  }

  onDelete(utilisateurID: number, utilisateur: Utilisateur){
    this.utilisateurService.supUtilisateur(utilisateurID, utilisateur).subscribe({
      next: data => {
        this.message = 'Suppression de l\'utilisateur avec succès';
        this.classCss = 'success';
        console.log("Suppresion de l'utilisateur: ", data);
      },
      error: err => {
        this.message = 'Echec de suppression de l\'utilisateur';
        this.classCss = 'error';
        console.log("Echec de suppresion de l\'utilisateur: ", err);
      }
    });
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
