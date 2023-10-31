import { AuteurDocument} from './auteur-document.model';
import { UtilisateurAuteur} from './utilisateur-auteur.model'

export class Auteur {
	auteurID?: number;
    nom?: string;
    prenom?: string;
    email?: string;
    nationalite?: string;
    auteurDocuments?: AuteurDocument[];
    utilisateurAuteurs?: UtilisateurAuteur[];
}
