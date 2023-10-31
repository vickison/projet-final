import { UtilisateurCategorie } from './utilisateur-categorie';
import { UtilisateurDocument } from './utilisateur-document.model';
import { UtilisateurTag } from './utilisateur-tag.model';
import { UtilisateurAuteur } from './utilisateur-auteur.model';

export class Utilisateur {
	utilisateurID?: number;
	nom?: string;
    prenom?: string;
    email?: string;
    password?: string;
    admin?: boolean;
    utilisateurCategories?: UtilisateurCategorie[];
    utilisateurDocuments?: UtilisateurDocument[];
    utilisateurTags?: UtilisateurTag[];
    utilisateurAuteurs?: UtilisateurAuteur[];
}
