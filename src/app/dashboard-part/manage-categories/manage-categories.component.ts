import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
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
    msg = '';

    constructor(private fb: FormBuilder,
                private categorieService: CategorieService,
                private utilisateurService: UtilisateurService,
                public dialog: MatDialog,
                private snackBar: MatSnackBar
              ) {
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
            //console.log('Category created successfully:', response);
          },
          (error) => {
            //console.error('Error creating category:', error);
            // Handle error (e.g., show an error message)
          }
        );
      }else{
          //console.log("error...");
          
      }
        
      }
    }

    addCat() {

      const config = new MatSnackBarConfig();
      config.duration = 4000; // Durée de la notification en millisecondes
      config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
      config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
      config.panelClass = ['custom-snackbar'];
      // Vérifier si le formulaire est valide
      if (this.catForm.valid) {
        // Récupérer les données du formulaire
        const categorie: Categorie = this.catForm.value;
        // Ajouter votre logique ici
        this.categorieService.creerCategorie(categorie).subscribe({
          next: data => {
            this.msg = 'Catégorie créée avec succès✅';
            this.snackBar.open(this.msg, 'Fermer', config);
            //console.log("Catégorie ajoutée avec succès: ", data);
            setTimeout(() => {
              //this.dialog.closeAll();
              this.catForm.reset();
              this.message = '';
              this.classCss = '';
            }, 1000);
          },
          error: err =>{
            this.msg = 'Échec d\'ajouter Catégorie❌';
            this.snackBar.open(this.msg, 'Fermer', config);
            //console.error("Erreur d'ajouter la catégorie: ", err);
          }
        });
      }else{
        //console.log('Form invalid...');
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
