import { UtilisateurCategorie } from './utilisateur-categorie';
import { UtilisateurDocument } from './utilisateur-document.model';
import { UtilisateurTag } from './utilisateur-tag.model';
import { UtilisateurAuteur } from './utilisateur-auteur.model';
import { AdminUtilisateur } from './admin-utilisateur.model';

export class Utilisateur {
	utilisateurID?: number;
	nom?: string;
    prenom?: string;
    username?: string;
    email?: string;
    password?: string;
    admin?: boolean;
    addresseIP?: string;
    supprimerUtil?: boolean;
    superAdmin?: boolean;
    auteurCreationUtil?: string;
    auteurModificationUtil?: string;
    dateCreationUtil?: Date;
    dateModificationUtil?: Date;
    utilisateurCategories?: UtilisateurCategorie[];
    utilisateurDocuments?: UtilisateurDocument[];
    utilisateurTags?: UtilisateurTag[];
    utilisateurAuteurs?: UtilisateurAuteur[];
    adminUtilisateurs?: AdminUtilisateur[];
}
