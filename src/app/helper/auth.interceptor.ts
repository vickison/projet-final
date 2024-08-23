import { HTTP_INTERCEPTORS, HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError} from "rxjs";
import { TokenStorageService } from "../services/token-storage.service";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";

const TOKEN_HEADER_KEY = 'Authorization';


@Injectable()
export class AuthInterceptor implements HttpInterceptor{
    constructor(private tokenService: TokenStorageService, 
        private router: Router,
        private snackBar: MatSnackBar
    ){}
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        req = req.clone({
            withCredentials: true
        })
        return next.handle(req).pipe(
            catchError((error: HttpErrorResponse) => {
                if(error.status === 401){
                    const errorMessage = error.error?.message || 'Une erreur est survenue.';
                    if (errorMessage === 'Identifiants incorrects.') {
                        this.snackBar.open('Identifiants incorrects. Veuillez réessayer.', 'Fermer', {
                          duration: 5000,
                          horizontalPosition: 'center',
                          verticalPosition: 'top'
                        });
                        this.router.navigate(['/admin/login']);
                    }else{
                        this.snackBar.open('Session expirée. Veuillez vous reconnecter.', 'Fermer', {
                            duration: 5000, 
                            horizontalPosition: 'center',
                            verticalPosition: 'top'
                        });
                        this.router.navigate(['/admin/login']);
                    }
                    this.tokenService.signOut();
                    
                }else if (error.status === 403) {
                    
                    this.snackBar.open('Accès refusé. Veuillez vérifier vos autorisations.', 'Fermer', {
                      duration: 2000,
                      horizontalPosition: 'center',
                      verticalPosition: 'top'
                    });

                    this.router.navigate(['/admin/login']);
                }

                return throwError(() => error);
            })
        )
    }
}

export const authInterceptorProviders = [
    {
        provide: HTTP_INTERCEPTORS, 
        useClass: AuthInterceptor, 
        multi: true
    }
];