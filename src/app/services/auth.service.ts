import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PasswordConfirmDialogComponent } from '../password-confirm-dialog/password-confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

const baseUrl = environment.apiUrl;

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

 @Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient,
    private dialog: MatDialog
  ) { }

  login(username: string, password: string ): Observable<any>{
    return this.http.post(`${baseUrl}/users/signin`, 
    { username, 
      password}, 
      httpOptions);
  }

  register(nom: string, prenom: string, username: string, email: string, password: string): Observable<any>{
    return this.http.post(`${baseUrl}/users/admin/register`,  {nom, prenom, username, email, password}, httpOptions);
  }

  registerSuperAdmin(nom: string, prenom: string, username: string, email: string, password: string): Observable<any>{
    return this.http.post(`${baseUrl}/users/root/register`, {nom, prenom, username, email, password});
  }

  logout(): Observable<any>{
    return this.http.post(`${baseUrl}/users/signout`, {}, httpOptions);
  }

  requestAdminPassword(): Observable<string> {
    return new Observable(observer => {
      const dialogRef = this.dialog.open(PasswordConfirmDialogComponent, {
        width: '400px',
        disableClose: true, // L'utilisateur doit explicitement annuler ou confirmer
        data: {
          title: 'Authentification requise',
          message: 'Veuillez entrer votre mot de passe administrateur pour confirmer cette action',
          confirmText: 'Confirmer',
          cancelText: 'Annuler'
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          observer.next(result); // Envoie le mot de passe
          observer.complete();
        } else {
          observer.error('Opération annulée par l\'utilisateur');
        }
      });
    });
  }
}
