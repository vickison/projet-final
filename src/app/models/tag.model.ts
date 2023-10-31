import { DocumentTag } from './document-tag.model';
import { UtilisateurTag } from './utilisateur-tag.model'

export class Tag {
	tagID?: number;
    tag?: string;
    documentTags?: DocumentTag[];
    utilisateurTags?: UtilisateurTag[];
}
