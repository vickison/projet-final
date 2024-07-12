import { CategorieDocument } from './categorie-document.model';
import { UtilisateurDocument } from './utilisateur-document.model';
import { DocumentTag } from './document-tag.model';
import { AuteurDocument } from './auteur-document.model';

enum Langue{
    Creole='Créole',
    Anglais='Anglais',
    Francais='Français',
    Espagnol='Espagnol'
  }

export class Document {
	documentID?: number;
    resume?: string;
    dateCreationDocument?: Date;
    dateModificationDocument?: Date;
    url?: string;
    nombreDeConsultations?: number;
    nombreDePartages?: number;
    langue?: Langue;
    typeFichier?: string;
    NOTE?: number;
    nombreNotes?: number;
    auteurCreationDocument?: string;
    auteurModificationDocument?: string;
    supprimerDocument?: boolean;
    like?: number;
    unlike?: number;

    taille?: number;
    format?: string;
    titre?: string;
    file?: File;

    categorieDocuments?: CategorieDocument[];
    utilisateurDocuments?: UtilisateurDocument[];
    documentTags?: DocumentTag[];
    auteurDocuments?: AuteurDocument[];
}
