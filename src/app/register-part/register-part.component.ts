import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register-part',
  templateUrl: './register-part.component.html',
  styleUrls: ['./register-part.component.scss']
})
export class RegisterPartComponent implements OnInit{
  form: any = {
    nom: null,
    prenom: null,
    username: null,
    email: null,
    password: null
  };

  isSuccessful = false;
  isSignupFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService){}

  ngOnInit(): void {
    
  }

  onSubmit(): void{
    const {nom, prenom, username, email, password} = this.form;

    this.authService.register(nom, prenom, username, email, password).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignupFailed = false;
        
      }, 
      error: err =>{
        this.errorMessage = err.error.message;
        this.isSignupFailed = true;
      }
    });
  }
}