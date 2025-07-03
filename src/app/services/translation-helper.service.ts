// translation-helper.service.ts
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class TranslationHelperService {
  constructor(private translate: TranslateService) {}

  private normalizeForComparison(name: string | undefined): string {
    if(name != undefined)
      return name?.toLowerCase();
      //.replace(/\s+/g, '_')
      //.replace(/[^\w_]/g, '');
    else return '';
  }

  getTranslatedName(originalName: string | undefined): string | undefined {
    const normalized = this.normalizeForComparison(originalName);
    
    // 1. Accès typé aux traductions
    const currentLang = this.translate.currentLang;
    const translations = this.getTranslationsForLanguage(currentLang);
    
    // 2. Recherche avec typage sécurisé
    if (translations) {
      for (const [key, value] of Object.entries(translations)) {
        if (this.normalizeForComparison(key) === normalized) {
          // Vérification explicite du type
          if (typeof value === 'string') {
            return value;
          }
          return String(value); // Fallback de conversion
        }
      }
    }
    
    return originalName;
  }

  // Méthode helper pour un accès typé aux traductions
  private getTranslationsForLanguage(lang: string): Record<string, string> | undefined {
    try {
      const translations = this.translate.translations[lang]?.CATEGORIES;
      if (translations && typeof translations === 'object') {
        return translations as Record<string, string>;
      }
    } catch (e) {
      console.warn('Error accessing translations', e);
    }
    return undefined;
  }

  hasTranslation(originalName: string): boolean {
    const normalized = this.normalizeForComparison(originalName);
    const translations = this.getTranslationsForLanguage(this.translate.currentLang);
    
    if (!translations) return false;
    
    return Object.keys(translations).some(key => 
      this.normalizeForComparison(key) === normalized
    );
  }
}