import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Tag } from '../models/tag.model';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private tokenService: TokenStorageService) { }

  createTag(utilisateurID: number, tag: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/tags/ajouter?utilisateurID=${utilisateurID}`, tag, { headers});
  }

  creerTag(tag: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json'});
    return this.http.post(`${this.apiUrl}/tags/admin/ajouter`, tag, { headers });
  }

  getTags(): Observable<{ tagID: number; tag: string; supprimerEtiquette: boolean }[]> {
    const url = `${this.apiUrl}/tags/public`;

    return this.http.get<{ tagID: number; tag: string ; supprimerEtiquette: boolean}[]>(url).pipe(
      catchError((error) => {
        console.error('Error fetching categories:', error);
        throw error;
      })
    );
  } 

  updateTag(tagID: number | undefined, adminID: number, tag: Tag): Observable<Tag>{
    return this.http.put<Tag>(`${this.apiUrl}/tags/update/${tagID}?adminID=${adminID}`, tag);
  }

  modifTag(tagID: number | undefined, tag: Tag): Observable<Tag>{
    return this.http.put<Tag>(`${this.apiUrl}/tags/admin/update/${tagID}`, tag);
  }

  delete(tagID: number | undefined, adminID: number, tag: Tag): Observable<Tag>{
    return this.http.put<Tag>(`${this.apiUrl}/tags/delete/${tagID}?adminID=${adminID}`, tag);
  }

  supTag(tagID: number | undefined, tag: Tag): Observable<Tag>{
    return this.http.put<Tag>(`${this.apiUrl}/tags/admin/delete/${tagID}`, tag);
  }

}
