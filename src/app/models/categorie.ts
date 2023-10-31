import { UtilisateurCategorie } from './utilisateur-categorie';
import { CategorieDocument } from './categorie-document.model';

export class Categorie {
	categorieID?: number;
	nom?: string;
	utilisateurCategories?: UtilisateurCategorie[];
	categorieDocuments?: CategorieDocument[];
}
