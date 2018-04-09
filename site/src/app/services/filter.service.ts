import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';
import {first} from 'rxjs/operators';

@Injectable()
export class FilterService {

  constructor(private db: AngularFireDatabase) { }

  getAll(version) {
    return this.db.list('/links/' + version + '/').valueChanges().pipe(first()).toPromise();
  }
}
