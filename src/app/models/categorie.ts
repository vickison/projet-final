import { UtilisateurCategorie } from './utilisateur-categorie';
import { CategorieDocument } from './categorie-document.model';

export class Categorie {
	categorieID?: number;
	nom?: string;
	supprimerCategorie?: boolean;
	auteurCreationCategorie?: string;
	dateCreationCategorie?: Date;
	auteurModificationCategorie?: string;
	dateModificationCategorie?: Date;
	utilisateurCategories?: UtilisateurCategorie[];
	categorieDocuments?: CategorieDocument[];
}
