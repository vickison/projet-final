import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Document } from '../models/document.model';
import { catchError } from 'rxjs/operators';
import { LikeIllustration } from '../models/like-illustration.model';
import { LikeCount } from '../models/like-count.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {


  private apiUrl = 'http://localhost:8080/api';
  private selectedDocumentSubject = new BehaviorSubject<Document | null>(null);

  constructor(private http: HttpClient) { }

  getAllDocuments(): Observable<Document[]> {
    const url = `${this.apiUrl}/documents/public`;
    return this.http.get<Document[]>(url);
  }

  getDocument(documentID?: number): Observable<Document>{
    const url = `${this.apiUrl}/documents/public/${documentID}`;
    return this.http.get<Document>(url);
  }

   getDocumentsByCategorie(categorieID?: number): Observable<Document[]> {
    const url = `${this.apiUrl}/categories/public/${categorieID}/documents`;
    return this.http.get<Document[]>(url);
  }

  downloadDocument(documentID?: number): Observable<Blob> {
    const url = `${this.apiUrl}/documents/public/download/${documentID}`;
    return this.http.get(url, { responseType: 'blob' });
  }

  getBlobDocument(documentID?: number): string {
    return `${this.apiUrl}/documents/public/download/${documentID}`;
  }

  setSelectedDocument(document: Document | null): void {
    this.selectedDocumentSubject.next(document);
  }

  getSelectedDocument(): Observable<Document | null> {
    return this.selectedDocumentSubject.asObservable();
  }

  getDocumentUrl(documentID?: number): string {
    return `${this.apiUrl}/documents/public/${documentID}`;
  }

  getDocUrl(documentID?: number): Observable<string> {
    return of(`${this.apiUrl}/documents/public/${documentID}`);
  }

  getDocumentId(documentId?: number): Observable<number | undefined>{
    return of(documentId);
  }



  uploadDocument(
    file: File,
    utilisateurID: number, 
    categorieID: number[], 
    tagID: number[],
    auteurID: number[],
    resume: string,
    langue: string,
    titre: string
    ): Observable<any> {
      let uploadUrl = '';
      let queryparams = '';
      const categorieIDStr = categorieID.join(',');
      const tagIDStr = tagID.join(',');
      const auteurIDStr = auteurID.join(',');
      const formData = new FormData();
      formData.append('file', file);
      formData.append('description', resume);
      formData.append('titre', titre);
      formData.append('langue', langue);
      //formData.append('paysPublication', paysPublication);

      if(tagID === null && auteurID === null && titre.length <=0 ){
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}`;
      }else if(auteurID === null && tagID.length > 0 && titre === null){
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}&tagID=${tagIDStr}`;
      }else if(auteurID === null && tagID.length > 0 && titre.length > 0){
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}&tagID=${tagIDStr}&newTile=${titre}`;
      }else if(auteurID.length > 0 && tagID === null && titre === null){
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}&auteurID=${auteurIDStr}`;
      }else if(auteurID.length > 0 && tagID === null && titre.length > 0){
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}&auteurID=${auteurIDStr}}&newTile=${titre}`;
      }else if(auteurID.length > 0 && tagID.length > 0 && titre === null){
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}&tagID=${tagIDStr}&auteurID=${auteurIDStr}`;
      }else{
        queryparams = `?utilisateurID=${utilisateurID}&categorieID=${categorieIDStr}&tagID=${tagIDStr}&auteurID=${auteurIDStr}&newTile=${titre}`;
      }
      uploadUrl = `${this.apiUrl}/documents/ajouter${queryparams}`;
      return this.http.post(uploadUrl, formData);
  }

  creerDocument(
    file: File,
    categorieID: number[], 
    tagID: number[],
    auteurID: number[],
    resume: string,
    langue: string,
    titre: string
    ): Observable<any> {

      let uploadUrl = '';
      let queryparams = '';
      const categorieIDStr = categorieID.join(',');
      const tagIDStr = tagID  === null ? '' : tagID.join(',');
      const auteurIDStr = auteurID === null ? '' : auteurID.join(',');
      const formData = new FormData();
      formData.append('file', file);
      formData.append('resume', resume);
      formData.append('titre', titre);
      formData.append('langue', langue);
      //formData.append('paysPublication', paysPublication);

      if(tagIDStr.length === 0 && auteurIDStr.length === 0 && titre.length === 0 ){
        queryparams = `?categorieID=${categorieIDStr}`;
      }else if(tagIDStr.length ===0 && auteurIDStr.length === 0 && titre.length > 0 ){
        queryparams = `?categorieID=${categorieIDStr}&newTitle=${titre}`;
      }else if(tagIDStr.length ===0 && tagIDStr.length > 0 && titre.length === 0){
        queryparams = `?categorieID=${categorieIDStr}&tagID=${tagIDStr}`;
      }else if(auteurIDStr.length === 0 && tagIDStr.length > 0 && titre.length > 0){
        queryparams = `?categorieID=${categorieIDStr}&tagID=${tagIDStr}&newTitle=${titre}`;
      }else if(auteurIDStr.length > 0 && tagIDStr.length ===0 && titre.length === 0){
        queryparams = `?categorieID=${categorieIDStr}&auteurID=${auteurIDStr}`;
      }else if(auteurIDStr.length > 0 && tagIDStr.length ===0 && titre.length > 0){
        queryparams = `?categorieID=${categorieIDStr}&auteurID=${auteurIDStr}}&newTitle=${titre}`;
      }else if(auteurIDStr.length > 0 && tagIDStr.length > 0 && titre.length === 0){
        queryparams = `?categorieID=${categorieIDStr}&tagID=${tagIDStr}&auteurID=${auteurIDStr}`;
      }else if(auteurIDStr.length > 0 && tagIDStr.length > 0 && titre.length > 0){
        queryparams = `?categorieID=${categorieIDStr}&tagID=${tagIDStr}&auteurID=${auteurIDStr}&newTitle=${titre}`;
      }
      uploadUrl = `${this.apiUrl}/documents/admin/ajouter${queryparams}`;
      console.log(uploadUrl);
      return this.http.post(uploadUrl, formData);
  }

  getDocumentByKeyword(keysword: string): Observable<Document[]>{
    const formData = new FormData();
    formData.append('keysword', keysword);
    const url = `${this.apiUrl}/documents/public/search?keywords=${keysword}`;
    return this.http.get<Document[]>(url);
  }

  getDocumentsByType(type: string): Observable<Document[]>{
    const url = `${this.apiUrl}/documents/public/types/${type}`;
    return this.http.get<Document[]>(url);
  }

  getDocumentsByOrder(order: string): Observable<Document[]>{
    const url = `${this.apiUrl}/documents/public/sorted?sortBy=${order}`;
    return this.http.get<Document[]>(url);
  }

  updateDocument(documenteID: number | undefined, 
                 adminID: number, 
                 document: Document): Observable<Document>{
    return this.http.put<Document>(`${this.apiUrl}/documents/update/${documenteID}?adminID=${adminID}`, document);
  }

  modifDocument(documenteID: number | undefined,
    document: Document): Observable<Document>{
  return this.http.put<Document>(`${this.apiUrl}/documents/admin/update/${documenteID}`, document);
}

  deleteDocument(documenteID: number | undefined, 
    adminID: number, 
    document: Document): Observable<Document>{
      return this.http.put<Document>(`${this.apiUrl}/documents/delete/${documenteID}?adminID=${adminID}`, document);
  }

  supDocument(documenteID: number | undefined, document: Document): Observable<Document>{
    return this.http.put<Document>(`${this.apiUrl}/documents/admin/delete/${documenteID}`, document);
  }

  likeIllustration(documentID: number | undefined){
    return this.http.put(`${this.apiUrl}/documents/public/${documentID}/like`, null);
  }

  likeCount(documentID: number | undefined): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/documents/public/${documentID}/like/count`);
  }

  documentIsLiked(documentID: number | undefined): Observable<boolean>{
    return this.http.get<boolean>(`${this.apiUrl}/documents/public/${documentID}/liked`);
  }

}
