// manage-admin.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Utilisateur } from 'src/app/models/utilisateur';
import { AuthService } from 'src/app/services/auth.service';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-manage-admin',
  templateUrl: './manage-admin.component.html',
  styleUrls: ['./manage-admin.component.scss']
})
export class ManageAdminComponent implements OnInit{

  form: any = {
    nom: null,
    prenom: null,
    username: null,
    email: null,
    password: null
  };

  isSuccessful = false;
  isSignupFailed = false;
  message = '';
  classCss='';

  // Le groupe de contrôles du formulaire
  userForm: FormGroup  = new FormGroup({});

  constructor(private fb: FormBuilder, 
    private userService: UtilisateurService,
    private authService: AuthService,
    private router: Router,
    public dialog: MatDialog) { 
    this.userForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      admin: [true],
      utilisateurCategories: this.fb.array([]),
      utilisateurDocuments: this.fb.array([]),
      utilisateurTags: this.fb.array([]),
      utilisateurAuteurs: this.fb.array([])
    });
  }

  ngOnInit(): void {
    
  }

  reloadPage(): void{
    window.location.reload();
  }

  register(): void{
    const nom = this.userForm.get('nom')?.value;
    const prenom = this.userForm.get('prenom')?.value;
    const username = this.userForm.get('username')?.value;
    const email = this.userForm.get('email')?.value;
    const password = this.userForm.get('password')?.value;

    this.authService.register(nom, prenom, username, email, password).subscribe({
      next: data => {
        this.message = 'Enregistrement avec succès';
        this.classCss = 'success';
        console.log(data);
        this.isSuccessful = true;
        this.isSignupFailed = false;
        setTimeout(() => {
            //this.dialog.closeAll();
            //this.userForm.reset();
            this.router.navigateByUrl('/admin/login');
            this.dialog.closeAll();
        }, 1000);
      }, 
      error: err =>{
        this.message = 'Echec d\'enregistrement';
        this.classCss = 'error';
        this.isSignupFailed = true;
        setTimeout(() => {
          //this.reloadPage();
          this.userForm.reset();
      }, 1000);
      }
    });
  }

  // Méthode pour soumettre le formulaire
  onSubmit() {
    // Vérifier si le formulaire est valide
    if (this.userForm.valid) {
      const user: Utilisateur = this.userForm.value;
      this.userService.createUser(user).subscribe(
        (response) => {
          console.log('User created successfully:', response);
          // Handle success (e.g., show a success message, redirect to login page)
        },
        (error) => {
          console.error('Error creating user:', error);
          // Handle error (e.g., show an error message)
        }
      );
    }
      // Récupérer les données du formulaire
      //const admin = this.adminForm.value;
      // Ajouter votre logique ici
    }
}


