import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Auteur } from 'src/app/models/auteur.model';
import { AuteurService } from 'src/app/services/auteur.service';
import { CategorieService } from 'src/app/services/categorie.service';
import { DocumentService } from 'src/app/services/document.service';
import { TagService } from 'src/app/services/tag.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { ManageAuteursComponent } from '../manage-auteurs/manage-auteurs.component';
import { ManageLabelComponent } from '../manage-label/manage-label.component';
import { ManageCategoriesComponent } from '../manage-categories/manage-categories.component';

enum Langue{
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
   selectedLangue: Langue = Langue.Creole;
   langues: Langue[] = [Langue.Anglais, Langue.Creole, Langue.Espagnol, Langue.Francais]
   tagModalOpen: boolean = false;

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
        langue: [Langue.Creole, Validators.required],
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
        this.classCss = 'success';
        console.log("Document ajouté avec succès: ", data);
        console.log('Langue: ', langue);
        setTimeout(() => {
          //this.dialog.closeAll();
          this.documentForm.reset();
        }, 1000);
      },
      error: err => {
        this.message = 'Echec d\'ajouter le document';
        this.classCss = 'error';
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
    const acceptTypes = ['image/png', 'image/jpg', 'image/jpeg', 'application/pdf', 'image/gif', 'image/tiff', 'audio/mp3', 'audio/mpeg', 'video/mp4'];
    const fileInput = event.target as HTMLInputElement;
    const file = fileInput.files?.[0];

    if (file && !acceptTypes.includes(file.type)) {
      this.message = 'Type de fichier invalide! Valide=(pdf, jpg, jpeg, gif, png, tiff, mp3, mp4, mpeg)'+file.type;
      this.classCss = 'error';
      this.documentForm.reset();
    }else if(file && acceptTypes.includes(file.type)){
      this.documentForm.patchValue({file});
    }
  }


  openAuthorDialog(): void {
    const dialogRef = this.dialog.open(ManageAuteursComponent, {
      width: '40%', // Ajuste la taille du dialogue
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.auteurService.getAuteurs().subscribe((auteurs) => {
        this.auteurOptions = [];
        this.auteurOptionsTemp = [];
        for(const auteur of auteurs){
          if(!auteur.supprimerAuteur){
            this.auteurOptionsTemp.push({
              ...auteur
            })
          }
        }
        this.auteurOptions = this.auteurOptionsTemp;
      });
      if (result) {
        this.documentForm.patchValue({ autor: result.autor });
      }
    });
  
  }




  openLabelDialog(): void {
    this.tagModalOpen = true;
    const dialogRef = this.dialog.open(ManageLabelComponent, {
      width: '40%', // Ajuste la taille du dialogue
    });
    dialogRef.afterClosed().subscribe(result => {
      this.tagService.getTags().subscribe((tags) => {
        this.tagOptions = [];
        this.tagOptionsTemp = [];
        for(const tag of tags){
          if(!tag.supprimerEtiquette){
            this.tagOptionsTemp.push({
              ...tag
            })
          }
        }
        this.tagOptions = this.tagOptionsTemp;
      });
      
      if (result) {
        //console.log('Label dialog closed!');
        
      }
    });
  }


  openCatDialog(): void {
    const dialogRef = this.dialog.open(ManageCategoriesComponent, {
      width: '40%', // Ajuste la taille du dialogue
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.categorieService.getCategories().subscribe((categories) => {
        this.categorieOptions = [];
        this.categorieOptionsTemp = [];
        for(const categorie of categories){
          if(!categorie.supprimerCategorie){
            this.categorieOptionsTemp.push({
              ...categorie
            })
          }
        }
        this.categorieOptions = this.categorieOptionsTemp;
      });
      if (result) {
        //this.documentForm.patchValue({ categories: result.categories });
      }
    });
  
  }

  

}
