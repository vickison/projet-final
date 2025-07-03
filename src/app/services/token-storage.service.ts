import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';


const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})

export class TokenStorageService {

  constructor(private router: Router,) { }

  // Récupère le token JWT
  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  signOut(): void{
    window.sessionStorage.clear();
    this.router.navigate(['/admin/login']);
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


  public getIdUser(): any{
    const user = window.sessionStorage.getItem(USER_KEY);
    
    if(user){
      const userObj = JSON.parse(user);
      userObj.id = Number(userObj.id);
      return userObj.id;
    }else{
      return
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

  // Vérifie si le token est expiré (gère le cas null)
  public isTokenExpired(token: string | null): boolean {
    const tokenToCheck = token ?? this.getToken();
    if (!tokenToCheck) return true;

    try {
      const decoded: any = jwtDecode(tokenToCheck);
      return decoded.exp < Date.now() / 1000;
    } catch (e) {
      return true;
    }
  }

  public getTokenExpirationDate(token: string): Date | null{
    try {
      const decoded: any = jwtDecode(token);
      if (decoded.exp === undefined) return null;
      return new Date(decoded.exp * 1000);
    } catch (e) {
      return null;
    }
  }

   // Récupère le temps restant avant expiration (en ms)
   public getTokenRemainingTime(): number | null {
    const token = this.getToken();
    if (!token) return null;

    const expirationDate = this.getTokenExpirationDate(token);
    if (!expirationDate) return null;

    return expirationDate.getTime() - Date.now();
  }

  
}
