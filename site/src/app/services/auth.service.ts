import { ActivatedRoute } from '@angular/router';
import { AngularFireAuth } from 'angularfire2/auth';
import { Injectable } from '@angular/core';
import * as firebase from 'firebase';

@Injectable()
export class AuthService {

  constructor(public afAuth: AngularFireAuth, private route: ActivatedRoute) { }

  login() {
    const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/';
    localStorage.setItem('returnUrl', returnUrl);

    this.afAuth.auth.signInWithRedirect(new firebase.auth.GoogleAuthProvider());
  }

  logout() {
    this.afAuth.auth.signOut();
  }

  get appUser() {
    return this.afAuth.authState.map(user => {
      if (user) {
        return (user.email === 'matthew.wallt@gmail.com');
      } else {
        return null;
      }
    });
  }
}
