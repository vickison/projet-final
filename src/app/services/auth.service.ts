import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8080/api';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient) { }

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
    return this.http.post(baseUrl, {}, httpOptions);
  }
}
