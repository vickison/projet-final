import { Component, ViewChild } from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Tag } from 'src/app/models/tag.model';
import { TagService } from 'src/app/services/tag.service';
import { MatDialog } from '@angular/material/dialog';
import { EditTagModalComponent } from './edit-tag-modal/edit-tag-modal.component';
import { Utilisateur } from 'src/app/models/utilisateur';

@Component({
  selector: 'app-deluptag-part',
  templateUrl: './deluptag-part.component.html',
  styleUrls: ['./deluptag-part.component.scss']
})
export class DeluptagPartComponent {
  displayedColumns = ['id', 'tag', 'cree_par', 'cree_le', 'modifie_par', 'modifie_le', 'action'];
  tagSource: MatTableDataSource<Tag>;
  tags: Tag[] = [];
  adminID: number = 0;
  utilisateurs: Utilisateur[] = [];
  message: String = '';
  classCss: String = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private tagService: TagService,
              private dialog: MatDialog){
    const t: Array<Tag>= [];
    this.tagService.getTags().subscribe(
      (tags: Tag[]) =>{
        for(const tag of tags){
          t.push(tag);
          console.log('tag: ', typeof(tag.dateCreationEtiquette))
        }
        this.tagSource = new MatTableDataSource(t);
        this.tagSource.paginator = this.paginator;
        this.tagSource.sort = this.sort;
      },
      (error) => {
        console.error('Erreur: ',error);
        
      }
    );
    this.tagSource = new MatTableDataSource(this.tags);
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
          console.error('Erreur: ',error);
          
        }
      );
      if(updatedTag){
          // Logique pour gérer les données mises à jour
          console.log('Dialog closed with data:', updatedTag);
        }
    },
    (error) => {
      console.error('Error updating tag ', error);
      
    });
  
  }

  onDelete(tagID: number, tag: Tag){
    this.tagService.supTag(tagID, tag).subscribe({
      next: data => {
        this.message = 'Suppression du label avec succès';
        this.classCss = 'success';
        console.log("Tag supprimer avec succes: ", data);
      },
      error: err => {
        this.message = 'Echec de suppression du tag';
        this.classCss = 'error';
        console.error("impossible de supprimer le tag: ", err);
      }
    });
  }

}
