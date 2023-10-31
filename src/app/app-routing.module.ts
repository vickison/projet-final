import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LeftPartComponent } from './left-part/left-part.component';
import { PageContentPartComponent } from './page-content-part/page-content-part.component';

// Définir les routes de l application ici
const routes: Routes = [
 
  // Ajouter des routes selon les besoins
  {path: 'categories/:categorieID', component: PageContentPartComponent},
  //{path: 'categories/:categorieID/documents', component: PageContentPartComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutineModule { }