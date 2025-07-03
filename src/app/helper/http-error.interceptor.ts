import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse, HTTP_INTERCEPTORS } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { NotificationComponent } from '../notification/notification.component';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor( 
          private router: Router,
          private snackBar: MatSnackBar,
          private dialog: MatDialog
      ){}
    
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<any> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          console.warn(`Resource not found: ${req.url}`); // Affiche un avertissement au lieu d'une erreur
        } else if(error.status === 401) {
          const dialogRef = this.dialog.open(NotificationComponent, {
            data: { 
              message: 'Problème d\'authorisation. Veuillez vous reconnecter.',
              redirectUrl: '/admin/login'
            }
          });

          dialogRef.afterClosed().subscribe(() => {
            this.router.navigate(['/admin/login']);
          });
        }
        else {
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
