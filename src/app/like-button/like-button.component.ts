import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LikeService } from '../services/like.service';
import { DocumentService } from '../services/document.service';

@Component({
  selector: 'app-like-button',
  templateUrl: './like-button.component.html',
  styleUrls: ['./like-button.component.scss']
})
export class LikeButtonComponent {

  @Input() isLiked: boolean | undefined;
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Input() documentID: number | undefined;

  constructor( private documentService: DocumentService){}

  toggleLike() {
    // Logique pour changer l'état "j'aime" ou "je n'aime plus"
    this.documentService.likeIllustration(this.documentID);
    this.isLiked = !this.isLiked;
  }

  close() {
    this.closeClicked.emit();
  }

}
