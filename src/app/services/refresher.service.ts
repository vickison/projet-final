import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({
  providedIn: 'root'
})
export class RefresherService {

  private shouldRefresh = false;
  private refreshSubject = new Subject<void>();
  private shouldRefreshSubject = new BehaviorSubject<boolean>(false);

  refresh$ = this.refreshSubject.asObservable();
  shouldRefresh$ = this.shouldRefreshSubject.asObservable();

  constructor() { }

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
