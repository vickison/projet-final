// edit-admin-modal.component.ts
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Utilisateur } from 'src/app/models/utilisateur';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { finalize, map, switchMap } from 'rxjs/operators';
import { UtilisateurUpdateDTO } from 'src/app/models/utilisateurUpdateDTO';
import { PasswordConfirmDialogComponent } from 'src/app/password-confirm-dialog/password-confirm-dialog.component';
import { EMPTY } from 'rxjs/internal/observable/empty';
import { TokenStorageService } from 'src/app/services/token-storage.service';

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
  updatedUtilDataV2: UtilisateurUpdateDTO;
  msg = '';
  isSaving: boolean = false;
  showPasswordSection = false;
  passwordConfirm = '';

  constructor(
    public dialogRef: MatDialogRef<EditAdminModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {utilisateur: Utilisateur},
    @Inject(MAT_DIALOG_DATA) public dataV2: {utilisateur: UtilisateurUpdateDTO},
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private tokenService: TokenStorageService,
  ) {
    // this.adminForm = this.fb.group({
    //   // nom: [data.nom, Validators.required],
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });

    this.updatedUtilData = {...data.utilisateur}
    this.updatedUtilDataV2 = {...dataV2.utilisateur}
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

    console.log("Hello...");

    
    this.utilisateurService.modifUtilisateur(this.updatedUtilData.utilisateurID, this.updatedUtilData).subscribe({
      next: response => {
        console.log("Data: ", response);
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

    console.log("Hello...");
    
    
  }

  onSaveChangeWithConfirmation() {
    const config = new MatSnackBarConfig();
    config.duration = 4000;
    config.horizontalPosition = 'center';
    config.verticalPosition = 'top';
    config.panelClass = ['custom-snackbar'];
  
    // 1. Ouvrir le dialogue de confirmation
    const dialogRef2 = this.dialog.open(PasswordConfirmDialogComponent, {
      width: '450px',
      data: {
        title: 'Confirmation requise',
        message: 'Veuillez entrer votre mot de passe administrateur pour confirmer',
        cancelText: 'Annuler',
        confirmText: 'Confirmer'
      }
    });
  
    // 2. Après fermeture du dialogue
    dialogRef2.afterClosed().pipe(
      // 3. Vérifier si un mot de passe a été saisi
      switchMap(adminPassword => {
        if (!adminPassword) {
          this.snackBar.open('Opération annulée', 'Fermer', config);
          return EMPTY;
        }
  
        // 4. Vérifier le mot de passe admin
        return this.utilisateurService.verifyPassword(
          Number(this.tokenService.getIdUser()), // ID de l'admin actuel
          adminPassword
        ).pipe(
          map(isValid => ({ isValid, adminPassword }))
        );
      }),
      // 5. Procéder à la modification si le mot de passe est valide
      switchMap(({ isValid, adminPassword }) => {
        console.log("Valid: ", isValid);
        if (!isValid) {
          this.snackBar.open('Mot de passe incorrect', 'Fermer', config);
          return EMPTY;
        }
  
        // 6. Inclure le mot de passe vérifié dans la requête de modification
        const updateData = {
          ...this.updatedUtilData,
          // // Ajouter le mot de passe vérifié
        };

        const utilisateurDTO: UtilisateurUpdateDTO = {
          nom: this.updatedUtilData.nom,
          prenom: this.updatedUtilData.prenom,
          username: this.updatedUtilData.username,
          email: this.updatedUtilData.email,
          password: this.updatedUtilData.password || '', // Champ obligatoire
          admin: this.updatedUtilData.admin,
          addresseIP: this.updatedUtilData.addresseIP,
          superAdmin: this.updatedUtilData.superAdmin
        };
          

        console.log("Data: ", updateData);
  
        return this.utilisateurService.modifUtilisateur(
          this.updatedUtilData.utilisateurID,
          utilisateurDTO
        );
      })
    ).subscribe({
      next: (response) => {
        this.msg = 'Modification réussie ✅';
        this.snackBar.open(this.msg, 'Fermer', config);
        
        const index = this.utilisateurs.findIndex(u => u.utilisateurID === response.utilisateurID);
        if (index !== -1) {
          this.utilisateurs[index] = response;
        }
        
        setTimeout(() => this.dialogRef.close(response), 1000);
      },
      error: (err) => {
        this.msg = err.status === 403 
          ? 'Permission refusée ❌' 
          : 'Échec de la modification ❌';
        this.snackBar.open(this.msg, 'Fermer', config);
        setTimeout(() => this.dialogRef.close(), 1000);
      }
    });
  }


  onSaveChangeV2(): void {
    const config = this.getSnackBarConfig();
    if (this.isSaving) return;
    this.isSaving = true; // Pour désactiver le bouton pendant la sauvegarde

    if (this.showPasswordSection && this.updatedUtilData.password !== this.passwordConfirm) {
      this.snackBar.open('Les mots de passe ne correspondent pas', 'Fermer', { duration: 3000 });
      this.isSaving = false;
      return;
    }

    this.utilisateurService.modifUtilisateurV2(
        this.updatedUtilData.utilisateurID,
        this.prepareUpdateData()
    )
    .pipe(
        finalize(() => this.isSaving = false)
    )
    .subscribe({
        next: (response) => this.handleSuccess(response, config),
        error: (err) => this.handleError(err, config)
    });
  }

  private prepareUpdateData(): any {
      const dataV2 = {...this.updatedUtilDataV2};
      
      // Nettoyage des données sensibles
      if (!dataV2.password) delete dataV2.password;
      //delete dataV2.passwordConfirm;
      
      return dataV2;
  }

  private handleSuccess(response: any, config: MatSnackBarConfig): void {
      // Mise à jour de la liste locale
      const index = this.utilisateurs.findIndex(u => u.utilisateurID === response.utilisateurID);
      if (index !== -1) {
          this.utilisateurs[index] = response;
      }

      this.snackBar.open('Mise à jour réussie ✅', 'Fermer', config);
      
      setTimeout(() => {
          this.dialogRef.close({
              success: true,
              updatedUser: response,
              refreshRequired: true
          });
      }, 1000);
  }

  private handleError(err: any, config: MatSnackBarConfig): void {
      console.error('Update error:', err);
      
      const errorMessage = err.error?.message 
          ? `Échec: ${err.error.message} ❌`
          : 'Une erreur est survenue ❌';
      
      this.snackBar.open(errorMessage, 'Fermer', config);
      
      setTimeout(() => {
          this.dialogRef.close({
              success: false,
              error: err
          });
      }, 1000);
  }

  private getSnackBarConfig(): MatSnackBarConfig {
      return {
          duration: 4000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['custom-snackbar']
      };
  }

  togglePasswordSection(): void {
    this.showPasswordSection = !this.showPasswordSection;
    if (!this.showPasswordSection) {
      this.updatedUtilData.password = '';
      this.passwordConfirm = '';
    }
  }

  close() {
    // Fermer la fenêtre modale sans sauvegarder les changements
    this.dialogRef.close();
  }
}
