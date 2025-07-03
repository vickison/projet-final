import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { interval, Observable, of, Subscription } from 'rxjs';
import { Utilisateur } from '../models/utilisateur';
//import { CookieService } from 'ngx-cookie-service';
import { map, switchMap, tap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { UtilisateurUpdateDTO } from '../models/utilisateurUpdateDTO';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {
  private baseUrl = environment.apiUrl;
  private tokenCheckInterval = 60000; // 1 minute
  private checkSubscription?: Subscription;

  constructor(private http: HttpClient,
    private authService: AuthService,
    private router: Router,
    /*private cookieService: CookieService*/) { }

  verifyPassword(userId?: number, password?: string): Observable<boolean>{
    return this.http.post<{isValid: boolean}>(
      `${this.baseUrl}/users/verify-password`, 
      { userId, password }
    ).pipe(
      map(response => response.isValid),
      catchError(() => of(false))
    );
  }

  getUser(userId?: number): Observable<Utilisateur>{
    return this.http.get<Utilisateur>(`${this.baseUrl}/users/${userId}`)
  } 

  

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
        //console.error('Error fetching utilisateurs:', error);
        throw error;
      })
    );
  }

  updateUtilisateur(utilisateurID: number, adminID: number, utilisateur: Utilisateur): Observable<Utilisateur>{
    return this.http.put<Utilisateur>(`${this.baseUrl}/users/update/${utilisateurID}?adminID=${adminID}`, utilisateur);
  }

  modifUtilisateur(utilisateurID: number | undefined, utilisateur: UtilisateurUpdateDTO): Observable<Utilisateur>{
    return this.http.put<Utilisateur>(`${this.baseUrl}/users/admin/update/${utilisateurID}`, utilisateur);
  }

  supUtilisateur(utilisateurID: number | undefined, utilisateur: Utilisateur): Observable<Utilisateur>{
    return this.http.put<Utilisateur>(`${this.baseUrl}/users/admin/delete/${utilisateurID}`, utilisateur);
  }



  modifUtilisateurV2(utilisateurID: number | undefined, utilisateur: UtilisateurUpdateDTO, currentPassword?: string): Observable<any> {
    // Création d'un objet sécurisé sans mot de passe vide
    const payload = this.sanitizeUserData(utilisateur);
    
    let url = `${this.baseUrl}/users/modif/update/${utilisateurID}`;
    
    if (currentPassword) {
      url += `?currentPassword=${encodeURIComponent(currentPassword)}`;
    }

    return this.http.put<Utilisateur>(url, payload);
  }

  private sanitizeUserData(user: UtilisateurUpdateDTO): any {
    const sanitized = {...user};
    
    // Supprimer le mot de passe s'il est vide ou non défini
    if (!sanitized.password || sanitized.password === '') {
      delete sanitized.password;
    }
    
    // Supprimer les champs sensibles non nécessaires
    //delete sanitized.passwordConfirm;
    
    return sanitized;
  }


  // Nouvelle méthode pour démarrer la vérification périodique
  startTokenValidation(): void {
    this.stopTokenValidation(); // Arrête toute vérification en cours

    this.checkSubscription = interval(this.tokenCheckInterval)
      .pipe(
        switchMap(() => this.validateToken())
      )
      .subscribe({
        next: (isValid) => {
          console.log("Valid: ", isValid)
          if (!isValid) {
            this.router.navigate(['/admin/login'], { 
              queryParams: { sessionExpired: true } 
            });
          }
        },
        error: () => this.router.navigate(['/admin/login'])
      });
  }


  // Arrête la vérification périodique
  stopTokenValidation(): void {
    if (this.checkSubscription) {
      this.checkSubscription.unsubscribe();
    }
  }


  validateToken(): Observable<boolean> {
    return this.http.get<{ valid: boolean }>(
      `${this.baseUrl}/users/validate`, 
      { withCredentials: true }
    ).pipe(
      map(response => response.valid),
      catchError(() => of(false))
    );
  }

}
