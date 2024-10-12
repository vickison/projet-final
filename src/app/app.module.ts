import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {NgFor} from '@angular/common';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { LeftPartComponent } from './left-part/left-part.component';
import { RightPartComponent } from './right-part/right-part.component';
import { HeaderPartComponent } from './header-part/header-part.component';
import { FooterPartComponent } from './footer-part/footer-part.component';
import { ContentPartComponent } from './content-part/content-part.component';
import { DetailPartComponent } from './detail-part/detail-part.component';
import { PageContentPartComponent } from './page-content-part/page-content-part.component';
//import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule} from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import {MatMenuModule} from '@angular/material/menu';
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


//import { MainPartComponent } from './main-part/main-part.component';
import { AdminComponent } from './admin/admin.component';
import { ClientComponent } from './client/client.component';
import{ClientRoutineModule} from 'src/app/client/client-routing.module';
import {AdminRoutineModule } from 'src/app/admin/admin-routing.module';
import { AppRoutineModule } from './app-routing.module';
import { DocumentViewerComponent } from './document-viewer/document-viewer.component';
import { MatCardModule } from '@angular/material/card';
import { LoginPartComponent } from './login-part/login-part.component';
import { MatToolbarModule } from '@angular/material/toolbar';
//import { DocumentComponent } from './models/document/document.component';
import { DashboardPartComponent } from './dashboard-part/dashboard-part.component';
import { ManageAdminComponent } from './dashboard-part/manage-admin/manage-admin.component';
import { ManageLabelComponent } from './dashboard-part/manage-label/manage-label.component';
import { ManageDocumentComponent } from './dashboard-part/manage-document/manage-document.component';
import { ManageCategoriesComponent } from './dashboard-part/manage-categories/manage-categories.component';
import { DelupadminPartComponent } from './dashboard-part/delupadmin-part/delupadmin-part.component';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { ReactiveFormsModule } from '@angular/forms';
import {MatPaginatorModule} from '@angular/material/paginator';

import { NgxMatFileInputModule } from '@angular-material-components/file-input';

import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { PdfViewerModule, PdfViewerComponent } from 'ng2-pdf-viewer';
import { DelupcategoryPartComponent } from './dashboard-part/delupcategory-part/delupcategory-part.component';
import { DelupdocumentPartComponent } from './dashboard-part/delupdocument-part/delupdocument-part.component';
import { DeluptagPartComponent } from './dashboard-part/deluptag-part/deluptag-part.component';
import { DelupauteurPartComponent } from './dashboard-part/delupauteur-part/delupauteur-part.component';

import { DetailDialogService } from './services/detail-dialog.service';
import { AboutDialogComponent } from './about-dialog/about-dialog.component';
import { AboutDialogService } from './services/about-dialog.service';

import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { HttpClient} from "@angular/common/http";
import { ManageAuteursComponent } from './dashboard-part/manage-auteurs/manage-auteurs.component';
import { EditAuteurModalComponent } from './dashboard-part/delupauteur-part/edit-auteur-modal/edit-auteur-modal.component';
import { EditCategoryModalComponent } from './dashboard-part/delupcategory-part/edit-category-modal/edit-category-modal.component';
import { EditDocumentModalComponent } from './dashboard-part/delupdocument-part/edit-document-modal/edit-document-modal.component';
import { EditTagModalComponent } from './dashboard-part/deluptag-part/edit-tag-modal/edit-tag-modal.component';
import { EditAdminModalComponent } from './dashboard-part/delupadmin-part/edit-admin-modal/edit-admin-modal.component';
import { RegisterPartComponent } from './register-part/register-part.component';
import { FlexLayoutModule } from '@angular/flex-layout';

import { authInterceptorProviders } from './helper/auth.interceptor';
import { AuthInterceptor } from './helper/auth.interceptor';
import { SuperAdminComponent } from './super-admin/super-admin.component';
import { LikeButtonComponent } from './like-button/like-button.component';
import { SnackBarLikeComponent } from './snack-bar-like/snack-bar-like.component';
import { httpErrorInterceptorProviders } from './helper/http-error.interceptor';
//import { AuthInterceptor } from './interceptors/auth.interceptor';
import { CommonModule } from '@angular/common';

export function HttpLoaderFactory(http:HttpClient){
  return new TranslateHttpLoader(http);
}

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
    DocumentViewerComponent,
    LoginPartComponent,
    //DocumentComponent,
    DashboardPartComponent,
    ManageAdminComponent,
    ManageLabelComponent,
    ManageDocumentComponent,
    ManageCategoriesComponent,
    DelupadminPartComponent,
    DelupcategoryPartComponent,
    DelupdocumentPartComponent,
    DeluptagPartComponent,
    DelupauteurPartComponent,
    AboutDialogComponent,
    ManageAuteursComponent,
    EditAuteurModalComponent,
    EditCategoryModalComponent,
    EditDocumentModalComponent,
    EditTagModalComponent,
    EditAdminModalComponent,
    RegisterPartComponent,
    SuperAdminComponent,
    LikeButtonComponent,
    SnackBarLikeComponent
    
  ],
  imports: [
    HttpClientModule,
    NgFor,
    MatMenuModule,
    MatToolbarModule,
    FormsModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule,
    MatProgressBarModule,
    BrowserAnimationsModule,
    BrowserModule,
    ClientRoutineModule,
    AdminRoutineModule,
    AppRoutineModule,
    MatCardModule,
    MatSidenavModule,
    MatListModule,
    ReactiveFormsModule,
    NgxExtendedPdfViewerModule,
    PdfViewerModule,
    MatPaginatorModule,
    MatTableModule,
    RouterModule,
    MatProgressSpinnerModule,
    NgxMatFileInputModule ,
    FlexLayoutModule,
    CommonModule,
    TranslateModule.forRoot(
      {
      loader:{
        provide:TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps:[HttpClient]
      }
    
    }
    )
  ],
  providers: [AboutDialogService, HttpClient, DetailDialogService, authInterceptorProviders, httpErrorInterceptorProviders],
  bootstrap: [AppComponent]
})

export class AppModule {
   
  constructor(private matIconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer){
      this.matIconRegistry.addSvgIcon("folder",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/folder6.svg"));
    
      this.matIconRegistry.addSvgIcon("telecharger",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/preview.svg"));

      this.matIconRegistry.addSvgIcon("download",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/telecharger.svg"));
    
      this.matIconRegistry.addSvgIcon("partage",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/link.svg"));
    
      this.matIconRegistry.addSvgIcon("contenupdf",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/contenupdf.svg"));

      this.matIconRegistry.addSvgIcon("image",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/image.svg"));

      this.matIconRegistry.addSvgIcon("video",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/video.svg"));

      this.matIconRegistry.addSvgIcon("aud",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/aud.svg"));

      this.matIconRegistry.addSvgIcon("selectall",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/selectall.svg"));
    
      this.matIconRegistry.addSvgIcon("like",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/like.svg"));

      this.matIconRegistry.addSvgIcon("dislike",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/dislike.svg"));

      this.matIconRegistry.addSvgIcon("hand-hello",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/hand-hello.svg")
      );

      this.matIconRegistry.addSvgIcon("hand-gauche",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/hand-gauche.svg")
      );

      this.matIconRegistry.addSvgIcon("hand-haut",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/hand-haut.svg")
      );

      this.matIconRegistry.addSvgIcon("add",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/add.svg"));

      this.matIconRegistry.addSvgIcon("mod",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/mod.svg"));

      this.matIconRegistry.addSvgIcon("sup",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/sup.svg"));
      
      this.matIconRegistry.addSvgIcon("tbars",
        this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/threeBars.svg")
      );
    }
 }


