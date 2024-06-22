import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Utilisateur } from '../models/utilisateur';
//import { CookieService } from 'ngx-cookie-service';
import { tap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {
  private baseUrl = 'http://localhost:8080/api'

  constructor(private http: HttpClient,
    /*private cookieService: CookieService*/) { }

  

  getAllUsers(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.baseUrl}/users`);
  }
  

  adminLogin(username?: string, password?: string): Observable<any>{
    const loginUrl = `${this.baseUrl}/admin/login`;
    const credentials = {username: username, password: password};
    return this.http.post<any>(loginUrl, credentials);
  }

  login(credentials: { username: string; password: string }): Observable<any>{
    return this.http.post(`${this.baseUrl}/users/login`, credentials, httpOptions)
  }

  register(infos: {nom: string, prenom: string, username: string, email: string, password: string}): Observable<any>{
    return this.http.post(`${this.baseUrl}/users/register`, infos, httpOptions)
  }

  private getCookie(name: string): string {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
      const [cookieName, cookieValue] = cookie.split('=');
      if (cookieName.trim() === name) {
        return cookieValue;
      }
    }
    return '';
  }
/*
  setToken(token: string): void {
    this.cookieService.set('authToken', token);
  }

  getToken(): string {
    return this.cookieService.get('authToken');
  }
*/
  storeToken(token?: string): void {
    // Set the token in an HttpOnly cookie for better security
    document.cookie = `jwtToken=${token}; Path=/; HttpOnly; Secure; SameSite=Strict`;
  }


  createUser(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.baseUrl}/users/admin/register`, user, { headers });
  }

  createSuperAdmin(user: any): Observable<any>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.baseUrl}/users/root/register`, user, {headers})
  }


  getUtilisateurs(): Observable<{ utilisateurID: number; username: string; email: string; supprimerUtil: boolean }[]> {
    const url = `${this.baseUrl}/users`;

    return this.http.get<{ utilisateurID: number; username: string; email: string; supprimerUtil: boolean }[]>(url).pipe(
      catchError((error) => {
        console.error('Error fetching utilisateurs:', error);
        throw error;
      })
    );
  }

  updateUtilisateur(utilisateurID: number, adminID: number, utilisateur: Utilisateur): Observable<Utilisateur>{
    return this.http.put<Utilisateur>(`${this.baseUrl}/users/update/${utilisateurID}?adminID=${adminID}`, utilisateur);
  }

  modifUtilisateur(utilisateurID: number | undefined, utilisateur: Utilisateur): Observable<Utilisateur>{
    return this.http.put<Utilisateur>(`${this.baseUrl}/users/admin/update/${utilisateurID}`, utilisateur);
  }

  supUtilisateur(utilisateurID: number | undefined, utilisateur: Utilisateur): Observable<Utilisateur>{
    return this.http.put<Utilisateur>(`${this.baseUrl}/users/admin/delete/${utilisateurID}`, utilisateur);
  }

}
