import { Component, ViewContainerRef } from '@angular/core';
import { AppRoutingModule } from "./app-routing.module";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {

  // Il ViewContainerRef viene usato in altri componenti.
  constructor(public viewContainerRef: ViewContainerRef) {}

}
