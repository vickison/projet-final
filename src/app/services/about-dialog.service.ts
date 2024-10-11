import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AboutDialogComponent } from '../about-dialog/about-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class AboutDialogService {

  constructor(private dialog: MatDialog) { }


  openDialog(): void {

    this.dialog.open(AboutDialogComponent,
      {
        width: '35%',
      }
    );
  }



}
