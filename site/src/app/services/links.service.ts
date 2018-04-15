import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';
import {AngularFireStorage} from 'angularfire2/storage';
import {Links} from '../models/links';

@Injectable()
export class LinksService {

  constructor(private db: AngularFireDatabase,
              private storage: AngularFireStorage
  ) { }

    populate(version) {
      return this.db.object('/links/' + version).valueChanges();
    }

    getAssetURL(link: string) {
      return this.storage.ref(link).getDownloadURL();
    }

    async expandLinks(link: Links) {
      if (link.mpg) { // null check
          await this.getAssetURL(link.mpg).toPromise().then(ref => {
              link.mpg = ref;
          });
      } else {
        // placeholder image for now
          await this.getAssetURL('placeholder.png').toPromise().then(ref => {
            link.mpg = ref;
          });
      }
    }
}
