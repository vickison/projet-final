import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';



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
 
  isListVisible: boolean = false ; 
  isTablet: boolean = false;
  isMobile: boolean = false;
  
  value = ' ';

  language: Language[] = [
    {value:'French-0', viewValue:'French'},
    {value:'English-1', viewValue:'English'},
    {value:'Creole-2', viewValue:'Creole'},
   ];
   selectedLanguage = this.language[2].value;

   constructor(private breakpointObserver: BreakpointObserver) { }

  ngOnInit(): void {
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

}

