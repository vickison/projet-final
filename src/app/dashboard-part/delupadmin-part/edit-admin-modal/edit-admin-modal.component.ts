// edit-admin-modal.component.ts
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Utilisateur } from 'src/app/models/utilisateur';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-edit-admin-modal',
  templateUrl: './edit-admin-modal.component.html',
  styleUrls: ['./edit-admin-modal.component.scss']
})
export class EditAdminModalComponent {
  //adminForm: FormGroup;
  utilisateurs: Utilisateur[] = [];
  message: String = '';
  classCss: String = '';
  updatedUtilData: Utilisateur;

  constructor(
    public dialogRef: MatDialogRef<EditAdminModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {utilisateur: Utilisateur},
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService
  ) {
    // this.adminForm = this.fb.group({
    //   // nom: [data.nom, Validators.required],
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });

    this.updatedUtilData = {...data.utilisateur}
  }

  // saveChanges() {
  //   // Envoyer les données mises à jour au composant principal
  //   this.dialogRef.close(this.adminForm.value);
  // }

  onSaveChange() {
    //const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.utilisateurService.modifUtilisateur(this.updatedUtilData.utilisateurID, this.updatedUtilData).subscribe({
      next: response => {
        this.message = 'Auteur modifié avec succès';
        this.classCss = 'success';
        const index = this.utilisateurs.findIndex(a => a.utilisateurID === response.utilisateurID)
        if(index !== -1){
          this.utilisateurs[index] = response;
        }
      },
      error: err => {
        this.message = 'Echec de modifiction d\'utilisateur';
        this.classCss = 'error';
        console.log("Echec de modifiction d'util.: ", err);
      }
      
    });
    this.dialogRef.close(this.updatedUtilData);
  }

  close() {
    // Fermer la fenêtre modale sans sauvegarder les changements
    this.dialogRef.close();
  }
}
