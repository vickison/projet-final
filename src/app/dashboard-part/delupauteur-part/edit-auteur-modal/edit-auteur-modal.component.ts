import { Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Auteur } from 'src/app/models/auteur.model';
import { Utilisateur } from 'src/app/models/utilisateur';
import { AuteurService } from 'src/app/services/auteur.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-edit-auteur-modal',
  templateUrl: './edit-auteur-modal.component.html',
  styleUrls: ['./edit-auteur-modal.component.scss']
})
export class EditAuteurModalComponent {
  //tagForm: FormGroup;
  updatedAuteurData: Auteur;
  utilisateurs: Utilisateur[] = [];
  selectedUserId: number = 0;
  auteurs: Auteur[] = [];
  message: string = '';
  classCss: string = '';

  constructor(
    public dialogRef: MatDialogRef<EditAuteurModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {auteur: Auteur},
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private auteurService: AuteurService
  ){
    // this.tagForm = this.fb.group({
    //   //tag: [data.tag, Validators.required]
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });
    this.updatedAuteurData = { ...data.auteur};
    this.fetchUsers();
  }

  saveChanges() {
    const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.auteurService.updateAuteur(this.updatedAuteurData.auteurID, selectedUserId, this.updatedAuteurData).subscribe(response => {
      const index = this.auteurs.findIndex(a => a.auteurID === response.auteurID)
      if(index !== -1){
        this.auteurs[index] = response;
      }
    })
    this.dialogRef.close(this.updatedAuteurData);
  }

  onSaveChange() {
    //const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.auteurService.modifAuteur(this.updatedAuteurData.auteurID, this.updatedAuteurData).subscribe({
      next: response => {
        this.message = 'Auteur modifié avec succès';
        this.classCss = 'success';
        const index = this.auteurs.findIndex(a => a.auteurID === response.auteurID)
        if(index !== -1){
          this.auteurs[index] = response;
        }
        setTimeout(() => {
          this.dialogRef.close(this.updatedAuteurData);
        }, 1000);
        
      },
      error: err => {
        this.message = 'Echec de modifiction d\'auteur';
        this.classCss = 'error';
        console.log("Echec de modifiction d'auteur: ", err);
        setTimeout(() => {
          this.dialogRef.close(this.updatedAuteurData);
        }, 1000);
        
      }
      
    });

  }

  close() {
    // Fermer la fenêtre modale sans sauvegarder les changements
    this.dialogRef.close();
  }

  fetchUsers(): void{
    this.utilisateurService.getAllUsers().subscribe(users => {
      this.utilisateurs = users;
    });
  }
}
