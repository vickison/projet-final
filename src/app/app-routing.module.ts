import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LeftPartComponent } from './left-part/left-part.component';
import { PageContentPartComponent } from './page-content-part/page-content-part.component';
import { LoginPartComponent } from './login-part/login-part.component';
import { ManageAdminComponent } from './dashboard-part/manage-admin/manage-admin.component';
import { ManageCategoriesComponent } from './dashboard-part/manage-categories/manage-categories.component';
import { ManageDocumentComponent } from './dashboard-part/manage-document/manage-document.component';
import { ContentPartComponent } from './content-part/content-part.component';
import { ManageLabelComponent } from './dashboard-part/manage-label/manage-label.component';
import { DashboardPartComponent } from './dashboard-part/dashboard-part.component';
import { ClientComponent } from './client/client.component';
import { SuperAdminComponent } from './super-admin/super-admin.component';
import { DocumentViewerComponent } from './document-viewer/document-viewer.component';
// Définir les routes de l application ici
const routes: Routes = [
  // Ajouter des routes selon les besoins
  {path: 'admin/dashboard', component: DashboardPartComponent},
  {path: 'admin/dashboard/categories', component: ManageCategoriesComponent},
  {path: ':categorieID', component: ClientComponent},
  {path: 'admin/login', component: LoginPartComponent},
  {path: 'admin/register', component: ManageAdminComponent},
  {path: 'admin/dashboard/documents', component: ManageDocumentComponent},
  {path: 'admin/dashboard/tags', component: ManageLabelComponent},
  //{path: 'root/admin/register', component: SuperAdminComponent},
  //{ path: ':categorieID/:documentID', component: ClientComponent },
  
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutineModule { }