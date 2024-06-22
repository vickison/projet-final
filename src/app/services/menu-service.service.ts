import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private isMenuOpenSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public isMenuOpen$: Observable<boolean> = this.isMenuOpenSubject.asObservable();

  constructor() {} 

  toggleMenu(): void { 

    this.isMenuOpenSubject.next(!this.isMenuOpenSubject.value);
  }

  closeMenu()
  {
    this.isMenuOpenSubject.next(false);
  }


}


 
