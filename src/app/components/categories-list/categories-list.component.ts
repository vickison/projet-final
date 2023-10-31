import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategorieService } from 'src/app/services/categorie.service';
import { Categorie } from 'src/app/models/categorie.model';

@Component({
  selector: 'app-categories-list',
  templateUrl: './categories-list.component.html',
  styleUrls: ['./categories-list.component.css']
})
export class CategoriesListComponent implements OnInit{
	
	categories?: Categorie[];

	constructor(private categorieService: CategorieService) { }

	ngOnInit(): void {
    	this.retrieveCategories();
  	}

  	retrieveCategories(): void {
    this.categorieService.getAll()
      .subscribe({
        next: (data) => {
          this.categories = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
}
