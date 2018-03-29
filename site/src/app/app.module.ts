import {LinksService } from './services/links.service';
import { FormsModule } from '@angular/forms';
import { FilterService } from './services/filter.service';
import { AdminAuthGuard } from './guards/admin-auth.guard';
import { UserService } from './services/user.service';
import { AuthGuard } from './guards/auth-guard';
import { AuthService } from './services/auth.service';
import {NavbarService} from './services/navbar.service';
import { RouterModule } from '@angular/router';
import { environment } from './../environments/environment';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AngularFireModule } from 'angularfire2';
import { AngularFireDatabaseModule } from 'angularfire2/database';
import { AngularFireAuthModule } from 'angularfire2/auth';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CustomFormsModule } from 'ng2-validation';

import { AppComponent } from './app.component';
import { AppNavbarComponent } from './navbar/app-navbar.component';
import { LinksComponent } from './links/links.component';
import { LoginComponent } from './login/login.component';
import { FilterComponent } from './links/links-filter/filter.component';
import { LinkCardComponent } from './link-card/link-card.component';
import { HomeComponent } from './home/home.component';


@NgModule({
  declarations: [
    AppComponent,
    AppNavbarComponent,
    LinksComponent,
    LoginComponent,
    FilterComponent,
    LinkCardComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireDatabaseModule,
    AngularFireAuthModule,
    FormsModule,
    CustomFormsModule,
    NgbModule.forRoot(),
    RouterModule.forRoot([
      {
        path: '',
        component: HomeComponent
      },
      {
        path: 'v/:version',
        component: LinksComponent
      },
      {
        path: 'login',
        component: LoginComponent
      }
    ])
  ],
  providers: [
    AuthService,
    AuthGuard,
    UserService,
    AdminAuthGuard,
    FilterService,
    LinksService,
    NavbarService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
