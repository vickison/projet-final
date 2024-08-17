import { Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Utilisateur } from 'src/app/models/utilisateur';
import { DocumentService } from 'src/app/services/document.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { Document } from 'src/app/models/document.model';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

enum Langue{
  Creole='Créole',
  Anglais='Anglais',
  Francais='Français',
  Espagnol='Espagnol'
}

@Component({
  selector: 'app-edit-document-modal',
  templateUrl: './edit-document-modal.component.html',
  styleUrls: ['./edit-document-modal.component.scss']
})
export class EditDocumentModalComponent {
  //tagForm: FormGroup;
  updatedDocumentData: Document;
  documents: Document[] = [];
  selectedUserId: number = 0;
  utilisateurs: Utilisateur[] = [];
  message: string = '';
  classCss: string = '';
  msg = '';
  selectedLangue: Langue = Langue.Creole;
  langues: Langue[] = [Langue.Anglais, Langue.Creole, Langue.Espagnol, Langue.Francais];
  langueData: Langue[] = [];
  


  constructor(
    public dialogRef: MatDialogRef<EditDocumentModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {document: Document},
    private fb: FormBuilder,
    private documentService: DocumentService,
    private utilisateurService: UtilisateurService,
    private snackBar: MatSnackBar
  ){
    // this.tagForm = this.fb.group({
    //   //tag: [data.tag, Validators.required]
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });
    this.updatedDocumentData = { ...data.document};
    console.log('Data updated contuctor: ', this.updatedDocumentData);
    this.fetchUsers();
  }

  saveChanges() {
    const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.documentService.updateDocument(this.updatedDocumentData.documentID, selectedUserId, this.updatedDocumentData).subscribe(response => {
      const index = this.documents.findIndex(d => d.documentID === response.documentID)
      if(index !== -1){
        this.documents[index] = response;
      }
    })
    this.dialogRef.close(this.updatedDocumentData);
  }

  onSaveChange() {
    //const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    const config = new MatSnackBarConfig();
    config.duration = 4000; // Durée de la notification en millisecondes
    config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
    config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
    config.panelClass = ['custom-snackbar'];
    
    this.documentService.modifDocument(this.updatedDocumentData.documentID, this.updatedDocumentData).subscribe({
        next:response => {
          console.log('Data updated: ', response);
          this.msg = 'Document mis à jour avec succès✅';
          this.snackBar.open(this.msg, 'Fermer', config);
          const index = this.documents.findIndex(d => d.documentID === response.documentID)
          if(index !== -1){
            this.documents[index] = response;
          }
          setTimeout(() => {
            this.dialogRef.close(this.updatedDocumentData);
          }, 1000);
      },
      error: err => {
        this.msg = 'Échec de mise à jour de Document❌';
        this.snackBar.open(this.msg, 'Fermer', config);
        console.log("Echec de modifier le document: ", err);
        setTimeout(() => {
          this.dialogRef.close(this.updatedDocumentData);
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
