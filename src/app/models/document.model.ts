import { CategorieDocument } from './categorie-document.model';
import { UtilisateurDocument } from './utilisateur-document.model';
import { DocumentTag } from './document-tag.model';
import { AuteurDocument } from './auteur-document.model';

export class Document {
	documentID?: number;
    resume?: string;
    datePublication?: Date;
    url?: string;
    nombreDeTelechargements?: number;
    nombreDeConsultations?: number;
    nombreDeCommentaires?: number;
    proprietaire?: string;
    langue?: string;

    taille?: number;
    format?: string;
    titre?: string;
    file?: File;

    categorieDocuments?: CategorieDocument[];
    utilisateurDocuments?: UtilisateurDocument[];
    documentTags?: DocumentTag[];
    auteurDocuments?: AuteurDocument[];
}
