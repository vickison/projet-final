import { Component, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { DocumentService } from 'src/app/services/document.service';
import { Document } from 'src/app/models/document.model';
import { MatDialog } from '@angular/material/dialog';
import { EditDocumentModalComponent } from './edit-document-modal/edit-document-modal.component';

@Component({
  selector: 'app-delupdocument-part',
  templateUrl: './delupdocument-part.component.html',
  styleUrls: ['./delupdocument-part.component.scss']
})
export class DelupdocumentPartComponent {
  displayedColumns = ['id', 'titre', 'resume', 'format','action'];
  documentSource: MatTableDataSource<Document>;
  documents: Document[] = [];
  adminID: number = 0;
  message: String = '';
  classCss: String = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  
  
  constructor(private documentService: DocumentService,
              private dialog: MatDialog){
    const doc: Array<Document>=[];
    this.documentService.getAllDocuments().subscribe(
      (documents: Document[]) => {
        for(const document of documents){
          doc.push(document);
        }
        this.documentSource = new MatTableDataSource(doc);
        this.documentSource.paginator = this.paginator;
        this.documentSource.sort = this.sort;
      },
      (error) => {
        console.error('Error fetching documents : ', error);
      }
    );

    this.documentSource = new MatTableDataSource(this.documents);
  }

  openEditModal(document: Document): void {
    const dialogRef = this.dialog.open(EditDocumentModalComponent, {
      width: '40%',
     // data: {user} // Passer les données de l'utilisateur à éditer
     data: {document}
    });

    dialogRef.afterClosed().subscribe(updatedDocument => {
      const doc: Array<Document>=[];
      this.documentService.getAllDocuments().subscribe(
        (documents: Document[]) => {
          for(const document of documents){
            doc.push(document);
          }
          this.documentSource = new MatTableDataSource(doc);
          this.documentSource.paginator = this.paginator;
          this.documentSource.sort = this.sort;
        },
        (error) => {
          console.error('Error fetching documents : ', error);
        }
      );
      if(updatedDocument){
        // Logique pour gérer les données mises à jour
        console.log('Dialog closed with data:', updatedDocument);

        this.documentService.updateDocument(document.documentID, this.adminID, updatedDocument).subscribe(response =>{
          const index = this.documents.findIndex(d => d.documentID === response.documentID);
          this.documents[index] = response;
        })
      }
      
    }, error => {
      console.error('Error updating document ', error);
    });
  
  }

  onDelete(documenteID: number, document: Document){
    this.documentService.supDocument(documenteID, document).subscribe({
      next: data => {
        this.message = 'Suppression du document avec succès';
        this.classCss = 'success';
        console.log("Document supprimer avec succes: ", data);
      },
      error: err => {
        this.message = 'Echec de suppression du document';
        this.classCss = 'error';
        console.log("Echec de supprission avec succes: ", err);
      }
    });
  }
}
