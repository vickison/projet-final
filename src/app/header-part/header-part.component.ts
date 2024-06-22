import { Component, OnInit,HostListener, Output, EventEmitter } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AboutDialogService } from '../services/about-dialog.service';
import { TranslateService } from '@ngx-translate/core';
import { DocumentService } from '../services/document.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Document } from '../models/document.model';
import { SearchService } from '../services/search.service';
import { FilterService } from '../services/filter.service';
import { OrderDocumentsService } from '../services/order-documents.service';
import { OrderService } from '../services/order.service';
import { MenuService } from '../services/menu-service.service';

interface Language{
  value: string;
  viewValue : string;
}



@Component({
  selector: 'app-header-part',
  templateUrl: './header-part.component.html',
  styleUrls: ['./header-part.component.scss']
})

export class HeaderPartComponent implements OnInit {

  formControl = new FormControl();
  documentForm: FormGroup = new FormGroup({});
  keywords: string = '';
  searchResults: Document[] = [];
  

  @Output() filterApplied = new EventEmitter<string>();
  

  searchTerm: string = '';
  lang:string = 'ht';

  clearSearchKeyword() {
    this.keywords = '';
    // Méthode appelée lorsque l'utilisateur clique sur l'icône en forme de croix pour effacer le texte de la recherche
  }


  reloadPage(): void {
    window.location.reload(); // Recharge la page actuelle
  }

  ChangeLang(Lang:any)
  {
    const selectedLanguage = Lang.target.value;

    localStorage.setItem('lang', selectedLanguage);

    this.translateService.use(selectedLanguage);

  }




  isListVisible: boolean = false ; 
  isTablet: boolean = false;
  isMobile: boolean = false;


  // value = 'assets/creole.png';  

  // language: Language[] = [
  //   {value:'kr', viewValue:'assets/creole.png'}, // Chemin vers l'image du drapeau français
  //   {value:'En',viewValue:'assets/english.png'}, // Chemin vers l'image du drapeau anglais
  //   {value:'Fr', viewValue:'assets/french.png'}, // Chemin vers l'image du drapeau créole
  // ];
  //  // selectedLanguage = this.language[2].value;
  // // selectedLanguage = this.language.find(lang => lang.value === this.value)?.value || ''; 
  // selectedLanguage= this.value;



  constructor(private breakpointObserver: BreakpointObserver, 
  private aboutDialogService: AboutDialogService, 
  private translateService:TranslateService,
  private documentService: DocumentService,
  private fb: FormBuilder,
  private sarchService: SearchService,
  private filterService: FilterService,
  private orderService: OrderService,
  private menuService: MenuService) {
    this.documentForm = this.fb.group({
      keysword: ['', Validators.required]
    });
  }

  onOrderClick(order: string): void{
    this.orderService.setOrder(order);
  }

  onFilterClick(filter: string): void{
    this.filterService.setFilter(filter);
    console.log(filter);
    
  }

  onInputChange(keywords: string): void {
    // Méthode appelée lorsque du texte est saisi dans la barre de recherche
    this.sarchService.setSearchKeyword(keywords);
  }


  openAboutDialog(): void {
  this.aboutDialogService.openDialog();
  }


  ngOnInit(): void {

  this.lang = localStorage.getItem('lang') || 'ht';


  this.breakpointObserver.observe([Breakpoints.Handset])
  .subscribe(result => {
    this.isMobile = result.matches;
    if (this.isMobile) {
      console.log("Mobile détectée.");
      }

  });

  this.breakpointObserver.observe([Breakpoints.Tablet])
  .subscribe(result => {
    this.isTablet = result.matches;
    if (this.isTablet) {
      console.log("Tablette détectée.");
      }

  });

  
  }

  toggleListVisibility() {
    this.isListVisible = !this.isListVisible;
  } 


  @HostListener('document:click',['$event'])
  handleClickOutside(event: Event){
    console.log("j'y suis");
    console.log(this.isListVisible);
    const clickedButton = (event.target as HTMLElement)?.closest('#format_icon');
    if( !clickedButton){
      this.isListVisible = false;
      }
    
  }

  applyFilter(criteria: string): void{
    this.filterApplied.emit(criteria);
  }

  toggleMenu() : void{
    this.menuService.toggleMenu();
  }

}