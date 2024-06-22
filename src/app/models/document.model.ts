import { CategorieDocument } from './categorie-document.model';
import { UtilisateurDocument } from './utilisateur-document.model';
import { DocumentTag } from './document-tag.model';
import { AuteurDocument } from './auteur-document.model';

export class Document {
	documentID?: number;
    resume?: string;
    dateCreationDocument?: Date;
    DateModificationDocument?: Date;
    url?: string;
    NombreDeConsultations?: number;
    NombreDePartages?: number;
    langue?: string;
    typeFichier?: string;
    NOTE?: number;
    NombreNotes?: number;
    AuteurCreationDocument?: string;
    AuteurModificationDocument?: string;
    supprimerDocument?: boolean;

    taille?: number;
    format?: string;
    titre?: string;
    file?: File;

    categorieDocuments?: CategorieDocument[];
    utilisateurDocuments?: UtilisateurDocument[];
    documentTags?: DocumentTag[];
    auteurDocuments?: AuteurDocument[];
}
