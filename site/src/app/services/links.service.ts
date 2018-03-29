import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';

@Injectable()
export class LinksService {

  constructor(private db: AngularFireDatabase) { }

    getAll(version) {
        return this.db.list('/links/' + version, ref => ref.orderByChild('title'));
    }

    get(version, linkId) {
      return this.db.object(`links/${version}/${linkId}`).snapshotChanges();
    }

}
