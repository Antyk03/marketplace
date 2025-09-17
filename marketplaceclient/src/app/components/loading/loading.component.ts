import { Component } from '@angular/core';
import { ModelloService } from '../../service/modello.service';
import { C } from '../../service/c';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css'
})
export class LoadingComponent {

  constructor(private modello: ModelloService) {}

  get loading(): boolean {
    return this.modello.getBean(C.CARICAMENTO) === true;
  }

}
