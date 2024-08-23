import { Component, OnInit } from '@angular/core';
import { UtilisateurService } from '../services/utilisateur.service';
import { AuthService } from '../services/auth.service';
import { TokenStorageService } from '../services/token-storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AboutDialogService } from '../services/about-dialog.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-login-part',
  templateUrl: './login-part.component.html',
  styleUrls: ['./login-part.component.scss']
})
export class LoginPartComponent implements OnInit{

  form: any = {
    username: null,
    password: null
  }

  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];


  credentials = { username: '', password: '' };

  constructor(private utilisateurService: UtilisateurService, 
    private authService: AuthService, 
    private tokenStorage: TokenStorageService,
    private router: Router,
    private route: ActivatedRoute,
    private aboutDialogService: AboutDialogService,
    private translateService:TranslateService

    ){
      this.translateService.use('fr');
    }


  ngOnInit(): void {
    if(this.tokenStorage.isLoggedIn()){
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;

    }
  }

  openAboutDialog(): void {
    this.aboutDialogService.openDialog();
  }

  onSubmit(): void{
    const { username, password } = this.form;
    this.authService.login(username, password).subscribe({
      next: data =>{
        //console.log(data)
        this.tokenStorage.saveUser(data);

        this.isLoggedIn = true;
        this.isLoginFailed = false;
        this.roles = this.tokenStorage.getUser().roles;
        //console.log('Login: ',this.isLoggedIn);
        this.router.navigateByUrl('/admin/dashboard');
        
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
        this.reloadPage();
      }
    });
  }

  reloadPage(): void{
    window.location.reload();
  }

  Login(){
    if (this.isFormValid()){
      this.utilisateurService.login(this.credentials).subscribe(
        (response) =>{
          //console.log('Login Successful...', response.token);
          
        },
        (err) =>{
          //console.log('Login failed...', err);
        }
      )
    } else {
      //console.warn('Form is not valid. Please check your inputs.');
    }
    
  }

  private isFormValid(): boolean {
    // Implement your form validation logic here
    // For simplicity, you can add basic checks like ensuring the username and password are not empty
    return this.credentials.username.trim() !== '' && this.credentials.password.trim() !== '';
  }

}
