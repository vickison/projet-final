import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';

// Définir les routes de l application ici
const routes: Routes = [
  {
    path:'admin',
    component: AdminComponent,
    children : [
      {path:'', component: AdminComponent}
    ]
    // Ajouter des routes selon les besoins
  }
 
  // Ajouter des routes selon les besoins
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AdminRoutineModule { }