import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';

@Injectable()
export class LinksService {

  constructor(private db: AngularFireDatabase) { }

    populate(version) {
      return this.db.list('/links/' + version);
    }

    getAll(version) {
        return this.db.list('/links/version/instance_id', ref => ref.orderByChild('title'));
    }

    get(version, linkId) {
      return this.db.object(`links/${version}/${linkId}`).snapshotChanges();
    }

    /**
     * pushes a new version to the database
     * @param version - version to be uploaded
     * @returns {ThenableReference}
     */
    uploadVersion(version) {
      return this.db.list('/versions/').push(version);
    }

}
