import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoadingComponent } from './components/loading/loading.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { PaginaNonTrovataComponent } from './components/pagina-non-trovata/pagina-non-trovata.component';
import { SnackbarComponent } from './components/snackbar/snackbar.component';
import { LoginComponent } from './routes/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HttpClientInMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryRepository } from './service/dao/in-memory-repository';
import { LoadingInterceptor } from './service/interceptors/loading.interceptor';
import { AuthTokenInterceptor } from './service/interceptors/auth-token.interceptor';
import { ErrorInterceptor } from './service/interceptors/error.interceptor';
import { HomeComponent } from './routes/home/home.component';
import { environment } from '../environments/environment';
import { CatalogoComponent } from './routes/catalogo/catalogo.component';
import { FormAggiungiProdottoComponent } from './routes/form-aggiungi-prodotto/form-aggiungi-prodotto.component';
import { FormModificaProdottoComponent } from './routes/form-modifica-prodotto/form-modifica-prodotto.component';
import { CarrelloComponent } from './routes/carrello/carrello.component';

@NgModule({
  declarations: [
    AppComponent,
    LoadingComponent,
    FooterComponent,
    HeaderComponent,
    PaginaNonTrovataComponent,
    SnackbarComponent,
    LoginComponent,
    HomeComponent,
    CatalogoComponent,
    FormAggiungiProdottoComponent,
    FormModificaProdottoComponent,
    CarrelloComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    environment.backendStrategy === 'MOCK' ? HttpClientInMemoryWebApiModule.forRoot(InMemoryRepository, {apiBase: '/api'}) : [],
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthTokenInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
