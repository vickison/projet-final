import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-password-confirm-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>
      <p>{{ data.message }}</p>
      <mat-form-field appearance="outline" class="w-full">
        <mat-label>Mot de passe</mat-label>
        <input matInput 
               type="password" 
               [(ngModel)]="password" 
               placeholder="Votre mot de passe admin"
               autocomplete="current-password">
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button [mat-dialog-close]="false">{{ data.cancelText || 'Annuler' }}</button>
      <button mat-raised-button 
              color="primary" 
              [mat-dialog-close]="password" 
              [disabled]="!password"
              cdkFocusInitial>
        {{ data.confirmText || 'Confirmer' }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .w-full { width: 100%; }
  `]
})
export class PasswordConfirmDialogComponent {
  password: string = '';

  constructor(
    public dialogRef: MatDialogRef<PasswordConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}
}