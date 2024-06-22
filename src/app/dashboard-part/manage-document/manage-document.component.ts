import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Auteur } from 'src/app/models/auteur.model';
import { AuteurService } from 'src/app/services/auteur.service';
import { CategorieService } from 'src/app/services/categorie.service';
import { DocumentService } from 'src/app/services/document.service';
import { TagService } from 'src/app/services/tag.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

enum Langues{
  Creole='Créole',
  Anglais='Anglais',
  Francais='Français',
  Espagnol='Espagnol'
}

@Component({
  selector: 'app-manage-document',
  templateUrl: './manage-document.component.html',
  styleUrls: ['./manage-document.component.scss']
})
export class ManageDocumentComponent implements OnInit {

  fctrl = new FormControl();

   documentForm: FormGroup  = new FormGroup({});
   categorieOptions: { categorieID: number; nom: string; supprimerCategorie: boolean }[] = [];
   categorieOptionsTemp: { categorieID: number; nom: string; supprimerCategorie: boolean }[] = [];
   utilisateurOptions: { utilisateurID: number; username: string; supprimerUtil: boolean }[] = [];
   tagOptions: { tagID: number; tag: string; supprimerEtiquette: boolean }[] = [];
   tagOptionsTemp: { tagID: number; tag: string; supprimerEtiquette: boolean }[] = [];
   utilisateurOptionsTemp: { utilisateurID: number; username: string ; supprimerUtil: boolean}[] = [];
   auteurOptions: { auteurID: number; nom: string; prenom: string, supprimerAuteur: boolean}[] = [];
   auteurOptionsTemp: { auteurID: number; nom: string; prenom: string, supprimerAuteur: boolean}[] = [];
   auteurTemp: Auteur[] = [];
   selectedFile: File | null = null;
   message: String = '';
   classCss: String = '';
   selectedLangue: Langues = Langues.Creole;
   langues: Langues[] = [Langues.Anglais, Langues.Creole, Langues.Espagnol, Langues.Francais]

   constructor(
      private fb: FormBuilder,
      private categorieService: CategorieService,
      private utilisateurService: UtilisateurService,
      private documentService: DocumentService,
      private tagService: TagService,
      private auteurService: AuteurService,
      private router: Router,
      public dialog: MatDialog
    ) {
      this.documentForm = this.fb.group({
        file: [null, Validators.required],
        resume: [''],
        titre: [''],
        langue: [''],
        categorieDocuments: [null, Validators.required],
        documentTags: [null],
        auteurDocuments: [null]
      });

      
    }
 
   ngOnInit(): void {
      this.loadCategorieOptions();
      this.loadUtilisateurOptions();
      this.loadTagOptions();
      this.loadAuteurOptions();
   }

  reloadPage(): void{
    window.location.reload();
  }
 
   // Méthode pour soumettre le formulaire
   onSubmit() {
     // Vérifier si le formulaire est valide
    if (this.documentForm.valid) {

      const utilisateurID = this.documentForm.get('utilisateurDocuments')?.value;
      const categorieID = this.documentForm.get('categorieDocuments')?.value;
      const tagID = this.documentForm.get('documentTags')?.value;
      const auteurID = this.documentForm.get('auteurDocuments')?.value;
      const titre = this.documentForm.get('titre')?.value;
      const resume = this.documentForm.get('resume')?.value;
      const langue = this.documentForm.get('langue')?.value;
      const file = this.documentForm.get('file')?.value as File;
       

      this.documentService.uploadDocument(
        file,
        utilisateurID, 
        categorieID, 
        tagID,
        auteurID,
        resume,
        langue,
        titre
        ).subscribe(
        (response) => {
          console.log('Document uploaded successfully:', response);
        },
        (error) => {
          console.error('Failed to upload document:', error);
        }
      );
     }
   }

   addDoc() {
    // Vérifier si le formulaire est valide
   if (this.documentForm.valid) {
     //const utilisateurID = this.documentForm.get('utilisateurDocuments')?.value;
     const categorieID = this.documentForm.get('categorieDocuments')?.value;
     const tagID = this.documentForm.get('documentTags')?.value;
     const auteurID = this.documentForm.get('auteurDocuments')?.value;
     const titre = this.documentForm.get('titre')?.value;
     const resume = this.documentForm.get('resume')?.value;
     const langue = this.documentForm.get('langue')?.value;
     const file = this.documentForm.get('file')?.value as File;

     this.documentService.creerDocument(file, categorieID, tagID, auteurID, resume, langue, titre).subscribe({
      next: data => {
        this.message = 'Document ajouté avec succès';
        this.classCss = 'success'
        console.log("Document ajouté avec succès: ", data);
        setTimeout(() => {
          //this.dialog.closeAll();
          this.documentForm.reset();
        }, 5000);
      },
      error: err => {
        this.message = 'Echec d\'ajouter le document';
        this.classCss = 'error'
        console.log("Echec d'ajouter le document: ", err);
      }
     });
    }
  }



  loadCategorieOptions(): void {
    this.categorieService.getCategories().subscribe((categories) => {
      for(const categorie of categories){
        if(!categorie.supprimerCategorie){
          this.categorieOptionsTemp.push({
            ...categorie
          })
        }
      }
      this.categorieOptions = this.categorieOptionsTemp;
    });
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

  loadTagOptions(): void {
    this.tagService.getTags().subscribe((tags) => {
      for(const tag of tags){
        if(!tag.supprimerEtiquette){
          this.tagOptionsTemp.push({
            ...tag
          })
        }
      }
      this.tagOptions = this.tagOptionsTemp;
    });
  }

  loadAuteurOptions(): void{
    this.auteurService.getAuteurs().subscribe((auteurs) => {
      for(const auteur of auteurs){
        if(!auteur.supprimerAuteur){
          this.auteurOptionsTemp.push({
            ...auteur
          })
        }
      }
      this.auteurOptions = this.auteurOptionsTemp;
    });
  }


  onFileChange(event: any): void {
    const fileInput = event.target as HTMLInputElement;
    const file = fileInput.files?.[0];

    if (file) {
      this.documentForm.patchValue({file});
    }
  }

  

}
