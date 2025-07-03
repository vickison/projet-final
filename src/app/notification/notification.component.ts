import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-notification',
  template: `
    <h2 mat-dialog-title>Notification</h2>
    <mat-dialog-content>{{ data.message }}</mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close (click)="onClose()">Fermer</button>
    </mat-dialog-actions>
  `,
})
export class NotificationComponent {
  constructor(
    public dialogRef: MatDialogRef<NotificationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string, redirectUrl?: string }
  ) {}

  onClose(): void {
    if (this.data.redirectUrl) {
      // La redirection se fait seulement quand l'utilisateur ferme la notification
      this.dialogRef.afterClosed().subscribe(() => {
        // Vous devrez injecter le Router ici si vous voulez gérer la redirection dans le component
        // Ou mieux, retourner un résultat que l'appelant pourra traiter
      });
    }
    this.dialogRef.close();
  }
}