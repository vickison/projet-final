import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { UtilisateurService } from '../services/utilisateur.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-super-admin',
  templateUrl: './super-admin.component.html',
  styleUrls: ['./super-admin.component.scss']
})
export class SuperAdminComponent implements OnInit{
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

  userForm: FormGroup  = new FormGroup({});

  constructor(private userService: UtilisateurService,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder) {
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

    this.authService.registerSuperAdmin(nom, prenom, username, email, password).subscribe({
      next: data => {
        this.message = 'Enregistrement avec succès';
        this.classCss = 'success';
        console.log(data);
        this.isSuccessful = true;
        this.isSignupFailed = false;
        setTimeout(() => {
          this.router.navigateByUrl('/admin/dashboard');
        }, 5000);
        
      }, 
      error: err =>{
        this.message = 'Enregistrement avec succès';
        this.classCss = 'success';
        this.isSignupFailed = true;
        setTimeout(() => {
          this.reloadPage();
        }, 5000);
      }
    });
  }

}
