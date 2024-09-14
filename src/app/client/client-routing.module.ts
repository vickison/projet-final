import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientComponent } from './client.component';

// Définir les routes de l application ici
const routes: Routes = [
  {
  path:'',
  component: ClientComponent,
  children : [
    {path:'', component: ClientComponent},
  ]
  // Ajouter des routes selon les besoins
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class ClientRoutineModule { }