import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Document } from '../models/document.model';
import { DocumentService } from '../services/document.service';

@Component({
  selector: 'app-detail-part',
  templateUrl: './detail-part.component.html',
  styleUrls: ['./detail-part.component.scss']
})
export class DetailPartComponent implements OnInit{

  selectedDocument: Document;
  auteurs: string[] = [];




  constructor(
    public dialogRef: MatDialogRef<DetailPartComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {document: Document},
    private documentService: DocumentService,
  ){

      this.selectedDocument = {...data.document}
      //console.log("Détail: ", this.selectedDocument.documentTags);
      
  }

  ngOnInit(): void {
    this.documentService.getAuteurDoc(this.selectedDocument.documentID).subscribe(data =>{
      this.auteurs = data;
    })

    this.getAuteurFormat();
  }

  getAuteurFormat(): string {
    if (this.auteurs.length === 0) {
      return '';
    } else if (this.auteurs.length === 1) {
      return this.auteurs[0];
    } else {
      return this.auteurs.join(', ');
    }
  }

  formatFileSize(bytes: number | undefined): string{
    if(bytes == undefined ) return 'N/A';
    else if(typeof bytes !== 'number' ) return 'Invalid file size';
    else if(bytes < 1024){
      return bytes + ' Bytes';
    }else if(bytes < 1024*1024){
      return (bytes / 1024).toFixed(2)+' KB';
    }else if(bytes < 1024*1024*1024){
      return (bytes / (1024*1024)).toFixed(2)+' MB';
    }else{
      return (bytes / (1024*1024*1024)).toFixed(2)+' GB';
    }
  }

}
