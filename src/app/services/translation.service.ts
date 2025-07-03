// translation.service.ts
import { Injectable } from '@angular/core';

interface ProductTranslation {
  fr: string;
  en: string;
  es: string;
  kr: string;
}

interface TranslationsDictionary {
  [key: string]: ProductTranslation;
}

@Injectable({ providedIn: 'root' })
export class TranslationService {
  private translations: TranslationsDictionary = {
    construction: { fr: 'construction', en: 'construction', es: 'Construcción' , kr: 'konstriksyon'},
    mine: { fr: 'mines', en: 'mouse', es: 'mines', kr: 'min' },
    sante: { fr: 'sante', en: 'health', es: 'salud', kr: 'lasante' },
    equite_des_genres: { fr: 'equite des genres', en: 'gender equality', es: 'equidad de género', kr: 'ekite nan jan' },
    gestio_des_risques: { fr: 'gestion des risques', en: 'risk management', es: 'gestión de riesgos', kr: 'jesyon risk yo' }
  };

  getTranslation(key: string, language: keyof ProductTranslation): string {
    const normalizedKey = key.toLowerCase();
    return this.translations[normalizedKey]?.[language] || key;
  }

  getAllTranslations(key: string): ProductTranslation {
    const normalizedKey = key.toLowerCase();
    return this.translations[normalizedKey] || { fr: key, en: key, es: key, kr: key };
  }
}