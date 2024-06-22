import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categorie } from '../models/categorie';
import { CategorieDocument } from '../models/categorie-document.model';
import { catchError } from 'rxjs/operators';



@Injectable({
  providedIn: 'root'
})
export class CategorieService {

	private apiUrl = 'http://localhost:8080/api';

	constructor(private http: HttpClient) { }

	getAllCategories(): Observable<Categorie[]> {
    	const url = `${this.apiUrl}/categories/public`;
    	return this.http.get<Categorie[]>(url);
  }

  createCategory(utilisateurID: number, category: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/categories/ajouter?utilisateurID=${utilisateurID}`, category, { headers });
  }

  creerCategorie(categorie: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/categories/admin/ajouter`, categorie, { headers });
  }

  getCategories(): Observable<{ categorieID: number; nom: string; supprimerCategorie: boolean }[]> {
    const url = `${this.apiUrl}/categories/public`;

    return this.http.get<{ categorieID: number; nom: string ; supprimerCategorie: boolean}[]>(url).pipe(
      catchError((error) => {
        console.error('Error fetching categories:', error);
        throw error;
      })
    );
  }

  updateCategorie(categorieID: number | undefined, adminID: number, categorie: Categorie): Observable<Categorie>{
    return this.http.put<Categorie>(`${this.apiUrl}/categories/update/${categorieID}?adminID=${adminID}`, categorie);
  }

  modifCategorie(categorieID: number | undefined, categorie: Categorie): Observable<Categorie>{
    return this.http.put<Categorie>(`${this.apiUrl}/categories/admin/update/${categorieID}`, categorie);
  }

  deleteCategorie(categorieID: number | undefined, adminID: number, categorie: Categorie): Observable<Categorie>{
    return this.http.put<Categorie>(`${this.apiUrl}/categories/delete/${categorieID}?adminID=${adminID}`, categorie);
  }

  supCategorie(categorieID: number | undefined, categorie: Categorie): Observable<Categorie>{
    return this.http.put<Categorie>(`${this.apiUrl}/categories/admin/delete/${categorieID}`, categorie);
  }

}
