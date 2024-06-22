import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';


const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})

export class TokenStorageService {

  constructor() { }

  signOut(): void{
    window.sessionStorage.clear();
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user))
  }


  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if(user){
      return JSON.parse(user)
    }else{
      return {};
    }
  }


  public isLoggedIn(): boolean{
    const user = window.sessionStorage.getItem(USER_KEY);
    if(user){
      return true;
    }else{
      return false;
    }
  }

  getTokenExpirationDate(){
    
  }

  
}
