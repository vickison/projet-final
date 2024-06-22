import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private searchKrywodSubject = new BehaviorSubject<string>('');
  searchKeyword$: Observable<string> = this.searchKrywodSubject.asObservable();

  constructor() { }

  setSearchKeyword(keywords: string): void{
    this.searchKrywodSubject.next(keywords);
  }

  getSearchKeyword(): string{
    return this.searchKrywodSubject.value;
  }

}
