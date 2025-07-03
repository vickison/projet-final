import { UtilisateurCategorie } from './utilisateur-categorie';
import { UtilisateurDocument } from './utilisateur-document.model';
import { UtilisateurTag } from './utilisateur-tag.model';
import { UtilisateurAuteur } from './utilisateur-auteur.model';

export class UtilisateurUpdateDTO {
    nom?: string;
    prenom?: string;
    username?: string;
    email?: string;
    password?: string;
    admin?: boolean;
    addresseIP?: string;
    superAdmin?: boolean;
}