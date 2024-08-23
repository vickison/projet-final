import { Component, OnInit } from '@angular/core';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { Utilisateur } from 'src/app/models/utilisateur';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit{
	utilisateurs?: Utilisateur[];

	constructor(private utilisateurService: UtilisateurService) { }

	ngOnInit(): void {
    	this.retrieveUtilisateur();
  	}

  	retrieveUtilisateur(): void {
    this.utilisateurService.getAllUsers()
      .subscribe({
        next: (data) => {
          this.utilisateurs = data;
          //console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
}
