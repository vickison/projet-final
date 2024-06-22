import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Document } from '../models/document.model';

@Component({
  selector: 'app-detail-part',
  templateUrl: './detail-part.component.html',
  styleUrls: ['./detail-part.component.scss']
})
export class DetailPartComponent {

  selectedDocument: Document;



  constructor(
    public dialogRef: MatDialogRef<DetailPartComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {document: Document}
  ){

      this.selectedDocument = {...data.document}
      console.log("Détail: ", this.selectedDocument.documentTags);
      
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
