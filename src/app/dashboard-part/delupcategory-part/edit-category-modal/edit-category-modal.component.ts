import { Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Categorie } from 'src/app/models/categorie';
import { Utilisateur } from 'src/app/models/utilisateur';
import { CategorieService } from 'src/app/services/categorie.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-edit-category-modal',
  templateUrl: './edit-category-modal.component.html',
  styleUrls: ['./edit-category-modal.component.scss']
})
export class EditCategoryModalComponent {
  
  //tagForm: FormGroup;
  updatedCategorieData: Categorie;
  utilisateurs: Utilisateur[] = [];
  selectedUserId: number = 0;
  categories: Categorie[] = [];
  message: string = '';
  classCss: string = '';

  constructor(
    public dialogRef: MatDialogRef<EditCategoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {categorie: Categorie},
    private fb: FormBuilder ,
    private utilisateurService: UtilisateurService,
    private categorieService: CategorieService
  ){
    // this.tagForm = this.fb.group({
    //   //tag: [data.tag, Validators.required]
    //   // prenom: [data.prenom, Validators.required],
    //   // email: [data.email, [Validators.required, Validators.email]],
    //   // password: [data.password, [Validators.required, Validators.minLength(8)]]
    // });
    this.updatedCategorieData = { ...data.categorie}
    //this.fetchUsers();
  }

  saveChanges() {
    const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.categorieService.updateCategorie(this.updatedCategorieData.categorieID, selectedUserId, this.updatedCategorieData).subscribe(response => {
      const index = this.categories.findIndex(c => c.categorieID === response.categorieID)
      if(index !== -1){
        this.categories[index] = response;
      }
    })

    this.dialogRef.close(this.updatedCategorieData);
  }

  onSaveChange() {
    console.log(this.updatedCategorieData);
    
    //const selectedUserId = this.selectedUserId;
    // Envoyer les données mises à jour au composant principal
    //this.dialogRef.close(this.adminForm.value);
    this.categorieService.modifCategorie(this.updatedCategorieData.categorieID, this.updatedCategorieData).subscribe({
      next: response => {
        this.message = 'Catégorie modifiée avec succès';
        this.classCss = 'success';
        const index = this.categories.findIndex(c => c.categorieID === response.categorieID)
        if(index !== -1){
          this.categories[index] = response;
        }
      },
      error: err => {
        this.message = 'Echec de modifier la catégorie';
        this.classCss = 'error';
        console.log("Echec de modification: ", err);
      }
    });
    this.dialogRef.close(this.updatedCategorieData);
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
