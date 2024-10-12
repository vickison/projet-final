import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { HttpClient, HttpEventType, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Document } from '../models/document.model';
import { catchError, map } from 'rxjs/operators';
import { LikeIllustration } from '../models/like-illustration.model';
import { LikeCount } from '../models/like-count.model';
import { environment } from 'src/environments/environment';

enum Langue{
    Creole='Créole',
    Anglais='Anglais',
    Francais='Français',
    Espagnol='Espagnol'
}

interface UploadResponse {
  progress?: number;
  message?: string;
}



@Injectable({
  providedIn: 'root'
})
export class DocumentService {


  private apiUrl = environment.apiUrl;
  private selectedDocumentSubject = new BehaviorSubject<Document | null>(null);
  selectedDocument$ = this.selectedDocumentSubject.asObservable();

  constructor(private http: HttpClient) { }

  getAllDocuments(): Observable<Document[]> {
    const url = `${this.apiUrl}/documents/public`;
    return this.http.get<Document[]>(url);
  }

  getDocument(documentID?: number): Observable<Document>{
    const url = `${this.apiUrl}/documents/public/${documentID}`;
    return this.http.get<Document>(url);
  }

  fetchDocumentById(documentID?: number): Observable<Document | null> {
    return this.http.get<Document>(`${this.apiUrl}/documents/public/document/${documentID}`).pipe(
      catchError((error) => {
        console.error('Error fetching document:', error);
        return of(null); // Return null or handle the error appropriately
      })
    );
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


  getDocumentThumbnail(documentID?: number): string {
    const url = `${this.apiUrl}/documents/public/${documentID}/thumbnail`;
    return url;
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
    langue: Langue,
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

  

  private createQueryParams(categorieID: number[], tagID: number[] | null, auteurID: number[] | null, titre: string | null): string {
    const categorieIDStr = categorieID.join(',');
    const tagIDStr = tagID && tagID.length > 0 ? tagID.join(',') : '';
    const auteurIDStr = auteurID && auteurID.length > 0 ? auteurID.join(',') : '';
    const titreStr = titre && titre.length > 0 ? titre : '';
  
    return `?categorieID=${categorieIDStr}${tagIDStr ? '&tagID=' + tagIDStr : ''}${auteurIDStr ? '&auteurID=' + auteurIDStr : ''}${titreStr ? '&newTitle=' + encodeURIComponent(titreStr) : ''}`;
  }


  creerDocument(
    file: File,
    categorieID: number[],
    tagID: number[] | null, // Ajoutez le type null pour être sûr
    auteurID: number[] | null, // Ajoutez le type null pour être sûr
    resume: string,
    langue: string,
    titre: string | null // Ajoutez le type null pour être sûr
  ): Observable<UploadResponse> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('resume', resume);
    formData.append('titre', titre || ''); // Ajoutez un fallback pour éviter null
    formData.append('langue', langue);
  
    const queryparams = this.createQueryParams(categorieID, tagID, auteurID, titre);
    const uploadUrl = `${this.apiUrl}/documents/admin/ajouter${queryparams}`;
  

  
    return this.http.post(uploadUrl, formData, { reportProgress: true, observe: 'events' }).pipe(
      map(event => {
        if (event.type === HttpEventType.UploadProgress) {
          if (event.total !== undefined) {
            const percentDone = Math.round(100 * event.loaded / event.total);
            return { progress: percentDone }; // Renvoie la progression
          }
        } else if (event instanceof HttpResponse) {
          return { message: 'Document ajouté et mis à jour avec succès✅' };
        }
        return {};
      }),
      catchError(error => {
        return throwError(() => new Error('Échec de l\'ajout du document'));
      })
    );
  }

  
  

  getDocumentByKeyword(keysword: string): Observable<Document[]>{
    const formData = new FormData();
    formData.append('keysword', keysword);
    const url = `${this.apiUrl}/documents/public/rechercher?motCles=${keysword}`;
    return this.http.get<Document[]>(url);
  }

  getDocumentByCriteria(criteres: string): Observable<Document[]>{
    const formData = new FormData();
    formData.append('criteres', criteres);
    const url = `${this.apiUrl}/documents/public/search?criteres=${criteres}`;
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

  unLikeIllustration(documentID: number | undefined){
    return this.http.put(`${this.apiUrl}/documents/public/${documentID}/unlike`, null);
  }

  likeCount(documentID: number | undefined): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/documents/public/${documentID}/like/count`);
  }

  documentIsLiked(documentID: number | undefined): Observable<boolean>{
    return this.http.get<boolean>(`${this.apiUrl}/documents/public/${documentID}/liked`);
  }

  // getDocumentThumbnail(documentID: number): Observable<Blob> {
  //   return this.http.get(`${this.apiUrl}/documents/public/${documentID}/thumbnail`, { responseType: 'blob' });
  // }

  getAuteurDoc(documentID: number | undefined): Observable<string[]>{
    return this.http.get<string[]>(`${this.apiUrl}/documents/public/${documentID}/auteurs`)
  }

}
