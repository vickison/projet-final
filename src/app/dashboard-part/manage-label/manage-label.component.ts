import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Tag } from 'src/app/models/tag.model';
import { TagService } from 'src/app/services/tag.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-manage-label',
  templateUrl: './manage-label.component.html',
  styleUrls: ['./manage-label.component.scss']
})
export class ManageLabelComponent {
  tagForm: FormGroup = new FormGroup({});
  utilisateurOptions: { utilisateurID: number; username: string ; supprimerUtil: boolean}[] = [];
  utilisateurOptionsTemp: { utilisateurID: number; username: string ; supprimerUtil: boolean}[] = [];
  message: String = '';
  classCss: String = '';


  constructor(private fb: FormBuilder,
              private tagService: TagService,
              private utilisateurService: UtilisateurService,
              private tokenStorageService: TokenStorageService,
              public dialog: MatDialog){
    this.tagForm = this.fb.group({
      tag: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUtilisateurOptions();
  }

  onSubmit(){
    if(this.tagForm.valid){
      const utilisateurID = this.tagForm.get('utilisateurTags')?.value;

      if(utilisateurID != null){
        const tag: Tag = this.tagForm.value;
        this.tagService.createTag(utilisateurID, tag).subscribe(
          (response) =>{
            console.log('Tag created successfuly...', response);
          },
          (error) =>{
            console.error('Error creating tag...', error);
            
          }
        );
      }else{
        console.log('utilisateur n\'xiste pas...');
      }
    }else{
      console.log('Form invalid...');
    }
  }

  addTag(){
    if(this.tagForm.valid){
    const tag: Tag = this.tagForm.value;
      this.tagService.creerTag(tag).subscribe({
        next: data => {
          this.message = 'Label ajouté avec succès ';
          this.classCss = 'success';
          console.log("Tag ajouter avec succès: ", data);
          setTimeout(() => {
            //this.dialog.closeAll();
            this.tagForm.reset();
            this.message = '';
            this.classCss = '';
          }, 1000);
        },
        error: err =>{
          this.message = 'Echec d\'ajouter le label';
          this.classCss = 'error';
          console.error("Echec d'enregistrement de tag: ", err);
        }
      });
    }else{
      console.log('Form invalid...');
    }
  }

  reloadPage(): void{
    window.location.reload();
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
