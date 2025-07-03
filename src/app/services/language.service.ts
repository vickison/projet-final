import { Injectable } from  '@angular/core'
import { BehaviorSubject } from 'rxjs'

@Injectable({providedIn: 'root'})
export class LanguageService{
    private currentLanguage = new BehaviorSubject<string>('ht');
    currentLanguage$ = this.currentLanguage.asObservable();

    setLanguage(lang: string){
        this.currentLanguage.next(lang);
    }
}