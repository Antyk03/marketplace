import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaginaNonTrovataComponent } from './components/pagina-non-trovata/pagina-non-trovata.component';
import { LoginComponent } from './routes/login/login.component';
import { HomeComponent } from './routes/home/home.component';
import { AuthGuard } from './guard/auth.guard';
import { CatalogoComponent } from './routes/catalogo/catalogo.component';
import { FormAggiungiProdottoComponent } from './routes/form-aggiungi-prodotto/form-aggiungi-prodotto.component';
import { FormModificaProdottoComponent } from './routes/form-modifica-prodotto/form-modifica-prodotto.component';
import { prodottiResolver} from './resolver/prodotti.resolver';
import { CarrelloComponent } from './routes/carrello/carrello.component';
import { AdminComponent } from './routes/admin/admin.component';
import { RegistrazioneComponent } from './routes/registrazione/registrazione.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full'},
  { path: 'register', component: RegistrazioneComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate:[AuthGuard] },
  { path: 'catalogo', component: CatalogoComponent},
  { path: 'aggiungi-prodotto', component: FormAggiungiProdottoComponent, canActivate:[AuthGuard] },
  { path: 'home/prodotti/:idProdotto', component: FormModificaProdottoComponent, canActivate:[AuthGuard], resolve: {prod: prodottiResolver}},
  { path: 'carrello', component: CarrelloComponent, canActivate:[AuthGuard] },
  { path: 'admin', component: AdminComponent, canActivate:[AuthGuard] },
  { path: '**', component: PaginaNonTrovataComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
