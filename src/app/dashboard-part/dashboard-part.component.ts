import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ManageAdminComponent } from './manage-admin/manage-admin.component';
import { ManageLabelComponent } from './manage-label/manage-label.component';
import { ManageDocumentComponent } from './manage-document/manage-document.component';
import { ManageCategoriesComponent } from './manage-categories/manage-categories.component';
import { ManageAuteursComponent } from './manage-auteurs/manage-auteurs.component';
import { TokenStorageService } from '../services/token-storage.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-dashboard-part',
  templateUrl: './dashboard-part.component.html',
  styleUrls: ['./dashboard-part.component.scss']
})
export class DashboardPartComponent implements OnInit {

  

  options = [
    { name: 'Admin', icon: 'admin_panel_settings'},
    { name: 'Catégories', icon: 'category' },
    { name: 'Documents', icon: 'description' },
    { name: 'Auteurs', icon: 'person'},
    { name: 'Etiquettes', icon: 'label' }
  ];

  selectedOption: any;
  hoveredOption: any;

  constructor(public dialog: MatDialog, 
    private tokenService: TokenStorageService,
    private router: Router) { }



  ngOnInit(): void {
    if(!this.tokenService.isLoggedIn()){
      this.router.navigate(['/admin/login']);
    }
  }

  selectOption(option: any) {
    this.selectedOption = option;
  }

  showOptions(option: any) {
    this.hoveredOption = option;
  }

  // add(option: any) {
  //   // TODO: implement logic to add a new record for the selected option
  //   console.log('Add', option);
  // }

  // edit(option: any) {
  //   // TODO: implement logic to edit an existing record for the selected option
  //   console.log('Edit', option);
  // }

  // delete(option: any) {
  //   // TODO: implement logic to delete an existing record for the selected option
  //   console.log('Delete', option);
  // }

  openDialog(option: any, button: string) {
    switch (option.name) {
      case 'Admin':
        if (button === 'Ajouter') {
          console.log("toto");
          this.dialog.open(ManageAdminComponent, { width: '40%' });
        }
        // Ajoutez d'autres cas pour les boutons Modifier et Supprimer
        break;

      case 'Catégories':
        if(button == 'Ajouter'){
          this.dialog.open(ManageCategoriesComponent);
        }
        break;
      case 'Etiquettes':
        if (button === 'Ajouter') {
          this.dialog.open(ManageLabelComponent);
        }
        // Ajoutez d'autres cas pour les boutons Modifier et Supprimer
        break;
      case 'Documents':
        if (button === 'Ajouter') {
          this.dialog.open(ManageDocumentComponent);
        }
        // Ajoutez d'autres cas pour les boutons Modifier et Supprimer
        break;
      // Ajoutez d'autres cas pour les autres options
      case 'Auteurs':
        if(button === 'Ajouter'){
          this.dialog.open(ManageAuteursComponent);
        }
        break;
    }
  }



    
}