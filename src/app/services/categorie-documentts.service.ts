import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategorieDocumenttsService {
  private selectedCategorySubject = new BehaviorSubject<number>(0);
  selectedCategorieID$: Observable<number> = this.selectedCategorySubject.asObservable();

  constructor() { }

  setSelectedCategorieID(categorieID: number): void{
    this.selectedCategorySubject.next(categorieID);
  }
}
