import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { Auteur } from 'src/app/models/auteur.model';
import { AuteurService } from 'src/app/services/auteur.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-manage-auteurs',
  templateUrl: './manage-auteurs.component.html',
  styleUrls: ['./manage-auteurs.component.scss']
})
export class ManageAuteursComponent implements OnInit{

  autForm: FormGroup = new FormGroup({});
  utilisateurOptions: { utilisateurID: number; username: string; supprimerUtil: boolean }[] = [];
  utilisateurOptionsTemp: { utilisateurID: number; username: string ; supprimerUtil: boolean}[] = [];
  message: String = '';
  classCss: String = '';
  msg = '';

  constructor(private fb: FormBuilder,
              private auteurService: AuteurService,
              private utilisateurService: UtilisateurService,
              public dialog: MatDialog,
              private snackBar: MatSnackBar
            ){
    this.autForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: [''],
    });
  }

  ngOnInit(): void {
    this.loadUtilisateurOptions();
  }

  reloadPage(): void{
    window.location.reload();
  }

  onSubmit(){
    if(this.autForm.valid){
      const utilisateurID = this.autForm.get('utilisateurAuteurs')?.value;
      if(utilisateurID !== null){
       const auteur: Auteur = this.autForm.value;

        this.auteurService.craateAuteur(utilisateurID, auteur).subscribe(
          (response) => {
            //console.log('Création d\'auteur ',response, ' avec succès.');
            
          },
          (error) =>{
            //console.log('Erreur de création d\'auteur ', error);
            
          }
        );
      }
      
    }
  }


  addAuteur(){

    const config = new MatSnackBarConfig();
    config.duration = 4000; // Durée de la notification en millisecondes
    config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
    config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
    config.panelClass = ['custom-snackbar'];

    if(this.autForm.valid){
      const auteur: Auteur = this.autForm.value;
      this.auteurService.creerAuteur(auteur).subscribe({
        next: data => {
          //this.message = 'Auteur ajouté avec succès ';
          //this.classCss = 'success';
          this.msg = 'Auteur créé avec succès✅';
            //this.classCss = 'success';
            //console.log("Document ajouté avec succès: ", event.data);
            this.snackBar.open(this.msg, 'Fermer', config);
          //console.log("Auteur ajouté avec succès: ", data);
          setTimeout(() => {
            //this.dialog.closeAll();
            this.autForm.reset();
            this.message = '';
            this.classCss = '';
          }, 1000);
        },
        error: err => {
          //this.message = 'Echec d\'ajouter l\'auteur ';
          //this.classCss = 'error';
          this.msg = 'Échec d\'ajouter Auteur❌';
            //this.classCss = 'success';
            //console.log("Document ajouté avec succès: ", event.data);
          this.snackBar.open(this.msg, 'Fermer', config);
          //console.log("Echec d'ajouter l'auteur: ", err);
        }
      });
    }
  }

  loadUtilisateurOptions(): void {
    this.utilisateurService.getUtilisateurs().subscribe((utilisateurs) => {
      for(const utilisateur of utilisateurs){
        if(!utilisateur.supprimerUtil){
          this.utilisateurOptionsTemp.push({
            ...utilisateur
          })
        }
      }
      this.utilisateurOptions = this.utilisateurOptionsTemp;
    });
  }

}
