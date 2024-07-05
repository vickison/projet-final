import { AuteurDocument} from './auteur-document.model';
import { UtilisateurAuteur} from './utilisateur-auteur.model'

export class Auteur {
	auteurID?: number;
    nom?: string;
    prenom?: string;
    email?: string;
    supprimerAuteur?: boolean;
    auteurCreationAuteur?: string;
    dateCreationAuteur?: Date;
    auteurModificationAuteur?: string;
    dateModificationAuteur?: Date;
    auteurDocuments?: AuteurDocument[];
    utilisateurAuteurs?: UtilisateurAuteur[];
}
