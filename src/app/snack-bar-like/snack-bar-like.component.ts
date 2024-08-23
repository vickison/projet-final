import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { DocumentService } from '../services/document.service';

@Component({
  selector: 'app-snack-bar-like',
  templateUrl: './snack-bar-like.component.html',
  styleUrls: ['./snack-bar-like.component.scss']
})
export class SnackBarLikeComponent {

  isLiked: boolean;

  constructor(
    public snackBarRef: MatSnackBarRef<SnackBarLikeComponent>,
    @Inject(MAT_SNACK_BAR_DATA) public data: any,
    private documentService: DocumentService,
  ){
    this.isLiked = data.isLiked;
    //console.log('is liked: ', this.isLiked )
  }

  onLikeClick(){
    if(this.isLiked){
      this.documentService.unLikeIllustration(this.data.docID).subscribe(() => {
        this.isLiked = false;
        this.snackBarRef.dismiss();
      });
    }else{
      this.documentService.likeIllustration(this.data.docID).subscribe(() => {
        this.isLiked = true;
        this.snackBarRef.dismiss();
      });
    }
  }

}
