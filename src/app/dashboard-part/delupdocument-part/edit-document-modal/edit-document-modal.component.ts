import { Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Utilisateur } from 'src/app/models/utilisateur';
import { DocumentService } from 'src/app/services/document.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { Document } from 'src/app/models/document.model';

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
  


  constructor(
    public dialogRef: MatDialogRef<EditDocumentModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {document: Document},
    private fb: FormBuilder,
    private documentService: DocumentService,
    private utilisateurService: UtilisateurService
  ){
    // this.tagForm = this.fb.group({
    //   //tag: [data.tag, Validators.required]
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });
    this.updatedDocumentData = { ...data.document};
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
    this.documentService.modifDocument(this.updatedDocumentData.documentID, 
      this.updatedDocumentData).subscribe({
        next:response => {
        this.message = 'Label modifié avec succès';
        this.classCss = 'success';
        const index = this.documents.findIndex(d => d.documentID === response.documentID)
        if(index !== -1){
          this.documents[index] = response;
        }
      },
      error: err => {
        this.message = 'Echec de modifier le document';
        this.classCss = 'error';
        console.log("Echec de modifier le document: ", err);
      }
    });
    this.dialogRef.close(this.updatedDocumentData);
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
