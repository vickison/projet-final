import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Tag } from 'src/app/models/tag.model';
import { TagService } from 'src/app/services/tag.service';
import { MatDialog } from '@angular/material/dialog';
import { EditTagModalComponent } from './edit-tag-modal/edit-tag-modal.component';
import { Utilisateur } from 'src/app/models/utilisateur';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from 'src/app/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-deluptag-part',
  templateUrl: './deluptag-part.component.html',
  styleUrls: ['./deluptag-part.component.scss']
})
export class DeluptagPartComponent implements OnInit {
  displayedColumns = ['id', 'tag', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  tagSource: MatTableDataSource<Tag> = new MatTableDataSource<Tag>([]);
  tags: Tag[] = [];
  adminID: number = 0;
  utilisateurs: Utilisateur[] = [];
  message = '';
  classCss: String = '';
  clsCss='';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private tagService: TagService,
              private dialog: MatDialog,
              private cdRef: ChangeDetectorRef,
              private snackBar: MatSnackBar
            ){}

  ngOnInit(): void {
      // Charger les tags depuis le service
      this.tagService.getTags().subscribe((tags: Tag[]) => {
        this.tags = tags;
        
        // Initialiser MatTableDataSource avec les données reçues
        this.tagSource = new MatTableDataSource(this.tags);
  
        // Appliquer la pagination et le tri
        this.tagSource.paginator = this.paginator;
        this.tagSource.sort = this.sort;
      }, (error) => {
        console.error('Error fetching documents : ', error);
      });
    }
  
    applyFilter(event: Event): void {
      const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    
      this.tagSource.filterPredicate = (data: Tag, filter: string) => {
        const tag = data.tag ? data.tag.toLowerCase() : ''; // Vérifier si 'tag' existe, sinon on prend une chaîne vide
        
        
        return tag.includes(filter); // Appliquer le filtre
      };
    
      this.tagSource.filter = filterValue; // Applique le filtre aux données de la table
    
      // Si le tableau n'affiche plus de résultats après application du filtre
      if (this.tagSource.paginator) {
        this.tagSource.paginator.firstPage();
      }
    }

  openEditModal(tag: Tag): void {
    const dialogRef = this.dialog.open(EditTagModalComponent, {
      width: '40%',
     // data: {user} // Passer les données de l'utilisateur à éditer
     data: {tag}
    });

    dialogRef.afterClosed().subscribe(updatedTag => {
      const t: Array<Tag>= [];
      this.tagService.getTags().subscribe(
        (tags: Tag[]) =>{
          for(const tag of tags){
            t.push(tag);
          }
          this.tagSource = new MatTableDataSource(t);
          this.tagSource.paginator = this.paginator;
          this.tagSource.sort = this.sort;
        },
        (error) => {
          //console.error('Erreur: ',error);
          
        }
      );
      if(updatedTag){
          // Logique pour gérer les données mises à jour
          //console.log('Dialog closed with data:', updatedTag);
        }
    },
    (error) => {
      //console.error('Error updating tag ', error);
      
    });
  
  }

  updateTableAfterDeletion(tagID: number) {
    // Récupérer les données actuelles sous forme de tableau
    const data = this.tagSource.data;
  
    // Mettre à jour le champ `supprimerUtil` du document pour le marquer comme supprimé
    const updatedData = data.map(tag => {
      if (tag.tagID === tagID) {
        tag.supprimerEtiquette = true;  // Marquer comme supprimé
      }
      return tag;
    });
  
    // Mettre à jour la source de données de la table
    this.tagSource.data = updatedData;
  
    // Forcer la détection des changements
    this.cdRef.detectChanges();
  }

  // onDelete(tagID: number, tag: Tag){
  //   const config = new MatSnackBarConfig();
  //   config.duration = 4000; // Durée de la notification en millisecondes
  //   config.horizontalPosition = 'center'; // Position horizontale: 'start', 'center', 'end'
  //   config.verticalPosition = 'top'; // Position verticale: 'top', 'bottom'
  //   config.panelClass = ['custom-snackbar'];
  //   this.tagService.supTag(tagID, tag).subscribe({
  //     next: data => {
  //       this.message = 'Suppression du label avec succès✅';
  //       this.classCss = 'success';
  //       this.snackBar.open(this.message, 'Fermer', config);
  //       //console.log("Tag supprimer avec succes: ", data);
  //       this.updateTableAfterDeletion(tagID);
  //     },
  //     error: err => {
  //       this.message = 'Echec de suppression du tag❌';
  //       this.classCss = 'error';
  //       this.snackBar.open(this.message, 'Fermer', config);
  //       //console.error("impossible de supprimer le tag: ", err);
  //     }
  //   });
  // }


  onDelete(tagID: number, tag: Tag) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        title: 'Confirmer la suppression',
        message: `Vous allez supprimer le tag "${tag.tag}". Cette action affectera tous les éléments associés.`,
        confirmText: 'Supprimer',
        cancelText: 'Annuler'
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.executeTagDeletion(tagID, tag);
      }
    });
  }

  private executeTagDeletion(tagID: number, tag: Tag) {
    const config: MatSnackBarConfig = {
      duration: 4000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['custom-snackbar', this.clsCss]
    };

    //this.isDeleting = true; // Active l'état de chargement

    this.tagService.supTag(tagID, tag).subscribe({
      next: () => {
        this.message = 'Tag supprimé avec succès ✅';
        this.classCss = 'success';
        this.snackBar.open(this.message, 'Fermer', config);
        this.updateTableAfterDeletion(tagID);
        //this.isDeleting = false;
      },
      error: (err) => {
        this.message = 'Échec de la suppression du tag ❌';
        this.classCss = 'error';
        this.snackBar.open(this.message, 'Fermer', config);
        console.error('Erreur lors de la suppression du tag:', err);
        //this.isDeleting = false;
      }
    });
  }

}
