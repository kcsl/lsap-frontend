import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';
import * as fireabase from 'firebase';


@Injectable()
export class UserService {

  constructor(public db: AngularFireDatabase) { }

  save(user: fireabase.User) {
    this.db.object(`/users/${user.uid}`).update({
      name: user.displayName,
      email: user.email
    });
  }
}
