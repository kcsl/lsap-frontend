import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';

@Injectable()
export class FilterService {

  constructor(private db: AngularFireDatabase) { }

  getAll() {
    return this.db.list('/filters/drivers', ref => ref.orderByChild('name')); // order better later on
  }
}
