import { DocumentTag } from './document-tag.model';
import { UtilisateurTag } from './utilisateur-tag.model'

export class Tag {
	tagID?: number;
    tag?: string;
    supprimerEtiquette?: boolean;
    documentTags?: DocumentTag[];
    utilisateurTags?: UtilisateurTag[];
}
