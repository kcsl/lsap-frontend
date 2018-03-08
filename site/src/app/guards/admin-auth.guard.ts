import { AuthService } from '../services/auth.service';
import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AdminAuthGuard implements CanActivate {
  constructor(private auth: AuthService) { }
  canActivate() {
    return this.auth.afAuth.authState.map(user => {
      if (user.email === 'matthew.wallt@gmail.com') {
        return true;
      } else {
        return false;
      }
    });
  }
}
