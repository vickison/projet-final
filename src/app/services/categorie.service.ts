import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categorie } from '../models/categorie';
import { CategorieDocument } from '../models/categorie-document.model';



@Injectable({
  providedIn: 'root'
})
export class CategorieService {

	private apiUrl = 'http://localhost:8080/api'
	constructor(private http: HttpClient) { }

	getAllCategories(): Observable<Categorie[]> {
      const url = `${this.apiUrl}/categories`;
    	return this.http.get<Categorie[]>(url);
  }
}
