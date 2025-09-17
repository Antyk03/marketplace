import { Component, ViewContainerRef } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  // Il ViewContainerRef viene usato in altri componenti.
  constructor(public viewContainerRef: ViewContainerRef) {}

}
