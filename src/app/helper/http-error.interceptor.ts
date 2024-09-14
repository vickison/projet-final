import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse, HTTP_INTERCEPTORS } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<any> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          console.warn(`Resource not found: ${req.url}`); // Affiche un avertissement au lieu d'une erreur
        } else {
          console.error(`HTTP error: ${error.message}`);
        }
        return of(error); // Retourne un observable pour éviter des erreurs non gérées
      })
    );
  }
}


export const httpErrorInterceptorProviders = [
    {
        provide: HTTP_INTERCEPTORS, 
        useClass: HttpErrorInterceptor, 
        multi: true
    }
];
