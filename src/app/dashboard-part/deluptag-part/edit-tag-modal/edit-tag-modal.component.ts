import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Tag } from 'src/app/models/tag.model';
import { Utilisateur } from 'src/app/models/utilisateur';
import { TagService } from 'src/app/services/tag.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-edit-tag-modal',
  templateUrl: './edit-tag-modal.component.html',
  styleUrls: ['./edit-tag-modal.component.scss']
})
export class EditTagModalComponent {

  //tagForm: FormGroup;
  updatedTagData: Tag;
  utilisateurs: Utilisateur[] = [];
  selectedUserId: number = 0;
  tags: any[] = [];
  message: string = '';
  classCss: string = '';

  constructor(
    public dialogRef: MatDialogRef<EditTagModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {tag: Tag},
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private tagService: TagService
  ){
    // this.tagForm = this.fb.group({
    //   //tag: [data.tag, Validators.required]
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });
    this.updatedTagData = { ...data.tag}
    this.fetchUsers();
  }

  saveChanges() {
    const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.tagService.updateTag(this.updatedTagData.tagID, selectedUserId, this.updatedTagData).subscribe(response => {
      const index = this.tags.findIndex(t => t.tagID === response.tagID);
      if(index !== -1){
        this.tags[index] = response;
      }
    })

    this.dialogRef.close(this.updatedTagData);
  }

  onSaveChange() {
    //const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.tagService.modifTag(this.updatedTagData.tagID, this.updatedTagData).subscribe({
      next: response => {
        this.message = 'Label modifié avec succès';
        this.classCss = 'success';
        const index = this.tags.findIndex(t => t.tagID === response.tagID);
        if(index !== -1){
          this.tags[index] = response;
        }
        setTimeout(() => {
          this.dialogRef.close(this.updatedTagData);
        }, 1000)
      },
      error: err => {
        this.message = 'Echec de modifier le label';
        this.classCss = 'error';
        console.log("Impossible de modifier l'etiquette: ", err);
        setTimeout(() => {
          this.dialogRef.close(this.updatedTagData);
        }, 1000)
      }
    });

    // setTimeout(() => {
    //   this.dialogRef.close(this.updatedTagData);
    // }, 1000)

    //this.dialogRef.close(this.updatedTagData);
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
