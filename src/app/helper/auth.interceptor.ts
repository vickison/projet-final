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
            catchError((error) => {
                if(error.status === 401){
                    //alert('Session expired, please reconnect via the login page');
                    this.snackBar.open('Probème autorisation. Reconnectez-vous', 'Fermer', {
                        duration: 2000, // Duration in milliseconds
                        horizontalPosition: 'center',
                        verticalPosition: 'top'
                      });
                    this.tokenService.signOut();
                    window.location.reload();
                    this.router.navigate(['/admin/login'])
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