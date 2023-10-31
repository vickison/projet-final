import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Document } from '../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {

private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getAllDocuments(): Observable<Document[]> {
    const url = `${this.apiUrl}/documents`;
    return this.http.get<Document[]>(url);
  }

   getDocumentsByCategorie(categorieID?: number): Observable<Document[]> {
    const url = `${this.apiUrl}/categories/${categorieID}/documents`;
    return this.http.get<Document[]>(url);
  }
}
