import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError, from } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenStorageService } from './services/token-storage.service';

@Injectable()
export class HttpCoreInterceptor /*implements HttpInterceptor*/ {
  constructor(
    private tokenService: TokenStorageService,
    private router: Router
  ) {}

  // token: string | null = '';

  // intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
  //   return next.handle(request).pipe(
  //     catchError((error: HttpErrorResponse) => {
  //       if (error.status === 401) {
  //         // Redirection vers la page de login
  //         this.router.navigate(['/login'], {
  //           queryParams: { 
  //             returnUrl: this.router.url 
  //           }
  //         });
  //       }
  //       return throwError(error);
  //     })
  //   );
  // }

  // intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  //   this.token = this.tokenService.getToken();
  //   // Vérifie d'abord si le token est expiré avant même d'envoyer la requête
  //   console.log("Token expired: ", this.tokenService.isTokenExpired(this.token))
  //   if (this.tokenService.isTokenExpired(this.token)) {
  //     this.tokenService.signOut();
  //     this.router.navigate(['/admin/login']);
  //     return throwError(() => new Error('Session expired'));
  //   }

  //   // Clone la requête pour ajouter les headers
  //   const authReq = this.addAuthHeader(req);

  //   return next.handle(authReq).pipe(
  //     catchError((error) => {
  //       if (error.status === 401 || error.status === 403) {
  //         this.tokenService.signOut();
  //         this.router.navigate(['/admin/login']);
  //         this.handleAuthError();
  //       }
  //       return throwError(() => error);
  //     })
  //   );
  // }

  private addAuthHeader(request: HttpRequest<any>): HttpRequest<any> {
    const token = this.tokenService.getToken();
    
    if (!token) {
      return request.clone({
        setHeaders: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        }
      });
    }

    return request.clone({
      setHeaders: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: `Bearer ${token}`
      }
    });
  }

  private handleAuthError(): Observable<never> {
    this.tokenService.signOut();
    this.router.navigate(['/admin/login'], {
      queryParams: { sessionExpired: true }
    });
    return throwError(() => new Error('Authentication failed'));
  }
}