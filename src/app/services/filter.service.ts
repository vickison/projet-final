import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FilterService {
  private shouldRefresh = false;
  private filterSubject = new BehaviorSubject<string>('all');
  filter$: Observable<string> = this.filterSubject.asObservable();
  private refreshSubject = new Subject<void>();
  private shouldRefreshSubject = new BehaviorSubject<boolean>(false);

  refresh$ = this.refreshSubject.asObservable();
  shouldRefresh$ = this.shouldRefreshSubject.asObservable();

  constructor() { }

  setFilter(filter: string): void{
    this.filterSubject.next(filter);
  }

  getFilter(): string{
    return this.filterSubject.value;
  }

  triggerRefresh() {
    if (!this.shouldRefreshSubject.value) {
      this.shouldRefreshSubject.next(true);
      this.refreshSubject.next();
    }
  }

  clearRefresh() {
    this.shouldRefreshSubject.next(false);
  }
}
