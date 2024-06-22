import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DetailPartComponent } from '../detail-part/detail-part.component';

@Injectable({
  providedIn: 'root'
})
export class DetailDialogService {

  constructor(private dialog: MatDialog) { }


  openDialog(): void {

    this.dialog.open(DetailPartComponent,
      {
        width: '30%',
      }
    );
  }



}