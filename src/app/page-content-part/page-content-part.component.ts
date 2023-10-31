import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../services/document.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Document } from 'src/app/models/document.model';

@Component({
  selector: 'app-page-content-part',
  templateUrl: './page-content-part.component.html',
  styleUrls: ['./page-content-part.component.scss']
})
export class PageContentPartComponent implements OnInit{
	  categoryID?: number;
	  documents: Document[] = [];

	  constructor(
	    private documentService: DocumentService,
	    private route: ActivatedRoute
	  ) { }

	  ngOnInit(): void {
	    this.route.params.subscribe(params => {
	      this.categoryID = +params['categorieID']; // '+' converts the string to a number
	      // Now, this.categoryId contains the categoryId from the route parameter
	      this.fetchDocuments();
	    });
	  }

	  fetchDocuments() {
	    this.documentService.getDocumentsByCategorie(this.categoryID)
	      .subscribe((documents: Document[]) => {
	        this.documents = documents;
	        console.log('Fetched Documents:', documents);
	      });
	  }

}
