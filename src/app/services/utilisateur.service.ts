import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Utilisateur } from '../models/utilisateur';

const baseUrl = 'http://localhost:8080/api/utilisateur'

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(baseUrl);
  }
}
