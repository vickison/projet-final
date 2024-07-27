// edit-admin-modal.component.ts
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Utilisateur } from 'src/app/models/utilisateur';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

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
  msg = '';

  constructor(
    public dialogRef: MatDialogRef<EditAdminModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {utilisateur: Utilisateur},
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private snackBar: MatSnackBar
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

    const config = new MatSnackBarConfig();
    config.duration = 4000; // Durée de la notification en millisecondes
    config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
    config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
    config.panelClass = ['custom-snackbar'];

    
    this.utilisateurService.modifUtilisateur(this.updatedUtilData.utilisateurID, this.updatedUtilData).subscribe({
      next: response => {
        this.msg = 'Admin mis à jour avec succès✅';
        this.snackBar.open(this.msg, 'Fermer', config);
        const index = this.utilisateurs.findIndex(a => a.utilisateurID === response.utilisateurID)
        if(index !== -1){
          this.utilisateurs[index] = response;
        }
        setTimeout(() => {
          this.dialogRef.close(this.updatedUtilData);
        }, 1000)
      },
      error: err => {
        this.msg = 'Échec de mis à jour de l\'Admin❌';
        this.snackBar.open(this.msg, 'Fermer', config);
        console.log("Echec de modifiction d'util.: ", err);
        setTimeout(() => {
          this.dialogRef.close(this.updatedUtilData);
        }, 1000)
      }
      
    });
    
    
  }

  close() {
    // Fermer la fenêtre modale sans sauvegarder les changements
    this.dialogRef.close();
  }
}
