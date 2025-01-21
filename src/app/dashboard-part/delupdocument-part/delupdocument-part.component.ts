import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { DocumentService } from 'src/app/services/document.service';
import { Document } from 'src/app/models/document.model';
import { MatDialog } from '@angular/material/dialog';
import { EditDocumentModalComponent } from './edit-document-modal/edit-document-modal.component';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-delupdocument-part',
  templateUrl: './delupdocument-part.component.html',
  styleUrls: ['./delupdocument-part.component.scss']
})
export class DelupdocumentPartComponent implements OnInit{
  displayedColumns = ['id', 'titre', 'resume', 'format', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le','action'];
  documentSource: MatTableDataSource<Document> = new MatTableDataSource<Document>([]);
  documents: Document[] = [];
  adminID: number = 0;
  message: String = '';
  classCss: String = '';
  msg = '';

  @Output() documentClicked = new EventEmitter<Document>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  
  
  constructor(private documentService: DocumentService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private cdRef: ChangeDetectorRef
            ){
    // const doc: Array<Document>=[];
    // this.documentService.getAllDocuments().subscribe(
    //   (documents: Document[]) => {
    //     for(const document of documents){
    //       doc.push(document);
    //     }
    //     this.documentSource = new MatTableDataSource(doc);
    //     this.documentSource.paginator = this.paginator;
    //     this.documentSource.sort = this.sort;
    //   },
    //   (error) => {
    //     //console.error('Error fetching documents : ', error);
    //   }
    // );

    // this.documentSource = new MatTableDataSource(this.documents);
  }

  ngOnInit(): void {
    // Charger les documents depuis le service
    this.documentService.getAllDocuments().subscribe((documents: Document[]) => {
      this.documents = documents;
      
      // Initialiser MatTableDataSource avec les données reçues
      this.documentSource = new MatTableDataSource(this.documents);

      // Appliquer la pagination et le tri
      this.documentSource.paginator = this.paginator;
      this.documentSource.sort = this.sort;
    }, (error) => {
      console.error('Error fetching documents : ', error);
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
  
    this.documentSource.filterPredicate = (data: Document, filter: string) => {
      const titre = data.titre ? data.titre.toLowerCase() : ''; // Vérifier si 'titre' existe, sinon on prend une chaîne vide
      const resume = data.resume ? data.resume.toLowerCase() : ''; // Vérifier si 'resume' existe, sinon on prend une chaîne vide
      
      return titre.includes(filter) || resume.includes(filter); // Appliquer le filtre
    };
  
    this.documentSource.filter = filterValue; // Applique le filtre aux données de la table
  
    // Si le tableau n'affiche plus de résultats après application du filtre
    if (this.documentSource.paginator) {
      this.documentSource.paginator.firstPage();
    }
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
          //console.error('Error fetching documents : ', error);
        }
      );
      if(updatedDocument){
        // Logique pour gérer les données mises à jour
       // console.log('Dialog closed with data:', updatedDocument);

        // this.documentService.modifDocument(document.documentID, updatedDocument).subscribe(response =>{
        //   const index = this.documents.findIndex(d => d.documentID === response.documentID);
        //   this.documents[index] = response;
        // })
      }
      
    }, error => {
      //console.error('Error updating document ', error);
    });
  
  }

  updateTableAfterDeletion(documenteID: number) {
    // Récupérer les données actuelles sous forme de tableau
    const data = this.documentSource.data;
  
    // Mettre à jour le champ `supprimerUtil` du document pour le marquer comme supprimé
    const updatedData = data.map(doc => {
      if (doc.documentID === documenteID) {
        doc.supprimerDocument = true;  // Marquer comme supprimé
      }
      return doc;
    });
  
    // Mettre à jour la source de données de la table
    this.documentSource.data = updatedData;
  
    // Forcer la détection des changements
    this.cdRef.detectChanges();
  }

  onDelete(documenteID: number, document: Document){

    const config = new MatSnackBarConfig();
    config.duration = 4000; // Durée de la notification en millisecondes
    config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
    config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
    config.panelClass = ['custom-snackbar'];
    
    this.documentService.supDocument(documenteID, document).subscribe({
      next: data => {
        this.msg = 'Document suprrimé avec succès✅';
        this.snackBar.open(this.msg, 'Fermer', config);
        //console.log("Document supprimer avec succes: ", data);
        // setTimeout(() => {
        //   //this.dialog.closeAll();
        //   // this.documentSource.forEach(doc => doc.supprimerDocument = true);
        //   // this.cdRef.detectChanges();
        // }, 500);
        this.updateTableAfterDeletion(documenteID);
      },
      error: err => {
        this.msg = 'Échec de Supprimer Document❌';
        this.snackBar.open(this.msg, 'Fermer', config);
        //console.log("Echec de supprission: ", err);
      }
    });
  }

  reloadPage(): void{
    window.location.reload();
  }
}
