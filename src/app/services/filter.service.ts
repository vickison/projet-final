import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  private filterSubject = new BehaviorSubject<string>('all');
  filter$: Observable<string> = this.filterSubject.asObservable();
 

  constructor() { }

  setFilter(filter: string): void{
    this.filterSubject.next(filter);
  }

  getFilter(): string{
    return this.filterSubject.value;
  }

}
