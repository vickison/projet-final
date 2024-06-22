import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { Auteur } from '../models/auteur.model';

@Injectable({
  providedIn: 'root'
})
export class AuteurService {

  private apiUrl = 'http://localhost:8080/api';

	constructor(private http: HttpClient) { }

  getAllAuteurs(): Observable<Auteur[]>{
    const url = `${this.apiUrl}/auteurs/public`;
    return this.http.get<Auteur[]>(url);
  }

  getAuteurs(): Observable<{auteurID: number; nom: string; prenom: string; supprimerAuteur: boolean}[]>{
    const url = `${this.apiUrl}/auteurs/public`;
    return this.http.get<{auteurID: number; nom: string; prenom: string; supprimerAuteur: boolean}[]>(url).pipe(
      catchError((error) => {
        console.log('Error fetching auteurs ', error);
        throw error;
      })
    );
  }

  craateAuteur(utilisateurID: number, auteur: any): Observable<any>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = `${this.apiUrl}/auteurs/ajouter?utilisateurID=${utilisateurID}`;
    return this.http.post(url, auteur, {headers})
  }

  creerAuteur(auteur: any): Observable<any>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const url = `${this.apiUrl}/auteurs/admin/ajouter`;
    return this.http.post(url, auteur, {headers})
  }

  updateAuteur(auteurID: number | undefined, adminID: number, auteur: Auteur): Observable<Auteur>{
    return this.http.put<Auteur>(`${this.apiUrl}/auteurs/update/${auteurID}?adminID=${adminID}`, auteur);
  }

  modifAuteur(auteurID: number | undefined, auteur: Auteur): Observable<Auteur>{
    return this.http.put<Auteur>(`${this.apiUrl}/auteurs/admin/update/${auteurID}`, auteur);
  }

  supAuteur(auteurID: number | undefined, auteur: Auteur): Observable<Auteur>{
    return this.http.put<Auteur>(`${this.apiUrl}/auteurs/admin/delete/${auteurID}`, auteur);
  }
}
