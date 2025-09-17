import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaginaNonTrovataComponent } from './components/pagina-non-trovata/pagina-non-trovata.component';
import { LoginComponent } from './routes/login/login.component';
import { HomeComponent } from './routes/home/home.component';
import { AuthGuard } from './guard/auth.guard';
import { CatalogoComponent } from './components/catalogo/catalogo.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full'},
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate:[AuthGuard] },
  { path: 'catalogo', component: CatalogoComponent},
  { path: '**', component: PaginaNonTrovataComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
