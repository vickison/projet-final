import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, filter } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private orderSubject = new BehaviorSubject<string>('');
  order$: Observable<string> = this.orderSubject.asObservable();

  constructor() { }

  setOrder(order: string): void{
    this.orderSubject.next(order);
  }

  getOrder(): string{
    return this.orderSubject.value;
  }
}
