import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
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

  constructor(private fb: FormBuilder,
              private auteurService: AuteurService,
              private utilisateurService: UtilisateurService,
              public dialog: MatDialog){
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
            console.log('Création d\'auteur ',response, ' avec succès.');
            
          },
          (error) =>{
            console.log('Erreur de création d\'auteur ', error);
            
          }
        );
      }
      
    }
  }


  addAuteur(){
    if(this.autForm.valid){
      const auteur: Auteur = this.autForm.value;
      this.auteurService.creerAuteur(auteur).subscribe({
        next: data => {
          this.message = 'Auteur ajouté avec succès ';
          this.classCss = 'success';
          console.log("Auteur ajouté avec succès: ", data);
          setTimeout(() => {
            //this.dialog.closeAll();
            this.autForm.reset();
          }, 2000);
        },
        error: err => {
          this.message = 'Echec d\'ajouter l\'auteur ';
          this.classCss = 'error';
          console.log("Echec d'ajouter l'auteur: ", err);
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
