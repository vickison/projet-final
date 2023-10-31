import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {NgFor} from '@angular/common';

import { AppComponent } from './app.component';
import { LeftPartComponent } from './left-part/left-part.component';
import { RightPartComponent } from './right-part/right-part.component';
import { HeaderPartComponent } from './header-part/header-part.component';
import { FooterPartComponent } from './footer-part/footer-part.component';
import { ContentPartComponent } from './content-part/content-part.component';
import { DetailPartComponent } from './detail-part/detail-part.component';
import { PageContentPartComponent } from './page-content-part/page-content-part.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule} from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import {MatMenuModule} from '@angular/material/menu';
import { HttpClientModule } from "@angular/common/http";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";
//import { MainPartComponent } from './main-part/main-part.component';
import { AdminComponent } from './admin/admin.component';
import { ClientComponent } from './client/client.component';
import{ClientRoutineModule} from 'src/app/client/client-routing.module';
import {AdminRoutineModule } from 'src/app/admin/admin-routing.module';
import { AppRoutineModule } from './app-routing.module';
//import { DocumentComponent } from './models/document/document.component';


@NgModule({
  declarations: [
    AppComponent,
    LeftPartComponent,
    RightPartComponent,
    HeaderPartComponent,
    FooterPartComponent,
    ContentPartComponent,
    DetailPartComponent,
    PageContentPartComponent,
    //MainPartComponent,
    AdminComponent,
    ClientComponent,
    //DocumentComponent,
    
  ],
  imports: [
    HttpClientModule,
    NgFor,
    MatMenuModule,
    FormsModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    BrowserModule,
    NoopAnimationsModule,
    ClientRoutineModule,
    AdminRoutineModule,
    AppRoutineModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule {
   
  constructor(private matIconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer){
    this.matIconRegistry.addSvgIcon(
     "folder",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/folder.svg")
    );}

  

 }


