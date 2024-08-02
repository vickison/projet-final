import { Injectable } from "@angular/core";
import {
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpRequest
} from "@angular/common/http";
 import { Observable } from "rxjs";


 @Injectable({
    providedIn:'root'
 })
 export class HttpCoreInterceptor implements HttpInterceptor {


    constructor() { }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    
            request =request.clone({

                setHeaders: {
                    "ContentType" : "appliccation/json",
                    "Access-Control-Allow-Origin": "*"
                }
            })
            
            return next.handle(request)
    }
 }