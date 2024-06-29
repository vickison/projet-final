import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Categorie } from 'src/app/models/categorie';
import { CategorieService } from 'src/app/services/categorie.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-manage-categories',
  templateUrl: './manage-categories.component.html',
  styleUrls: ['./manage-categories.component.scss']
})
export class ManageCategoriesComponent {

    // Le groupe de contrôles du formulaire
    catForm: FormGroup  = new FormGroup({}); 
    utilisateurOptions: { utilisateurID: number; username: string; supprimerUtil: boolean }[] = [];
    utilisateurOptionsTemp: { utilisateurID: number; username: string ; supprimerUtil: boolean}[] = [];
    message: String = '';
    classCss: String = '';

    constructor(private fb: FormBuilder,
                private categorieService: CategorieService,
                private utilisateurService: UtilisateurService,
                public dialog: MatDialog) {
      this.catForm = this.fb.group({
        nom: ['', Validators.required]
      });
    }

    ngOnInit(): void {
      this.loadUtilisateurOptions();
    }

    reloadPage(): void{
      window.location.reload();
    }
  
    // Méthode pour soumettre le formulaire
    onSubmit() {
      // Vérifier si le formulaire est valide
      if (this.catForm.valid) {
        // Récupérer les données du formulaire
        const utilisateurID = this.catForm.get('utilisateurCategories')?.value;
        if(utilisateurID != null){
          const category: Categorie = this.catForm.value;
        // Ajouter votre logique ici
        this.categorieService.createCategory(utilisateurID, category).subscribe(
          (response) =>{
            console.log('Category created successfully:', response);
          },
          (error) => {
            console.error('Error creating category:', error);
            // Handle error (e.g., show an error message)
          }
        );
      }else{
          console.log("error...");
          
      }
        
      }
    }

    addCat() {
      // Vérifier si le formulaire est valide
      if (this.catForm.valid) {
        // Récupérer les données du formulaire
        const categorie: Categorie = this.catForm.value;
        // Ajouter votre logique ici
        this.categorieService.creerCategorie(categorie).subscribe({
          next: data => {
            this.message = 'Catégorie ajoutée avec succès ';
            this.classCss = 'success';
            console.log("Catégorie ajoutée avec succès: ", data);
            setTimeout(() => {
              //this.dialog.closeAll();
              this.catForm.reset();
            }, 2000);
          },
          error: err =>{
            this.message = 'Echec d\'ajouter la catégorie';
            this.classCss = 'error';
            console.error("Erreur d'ajouter la catégorie: ", err);
          }
        });
      }else{
        console.log('Form invalid...');
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
