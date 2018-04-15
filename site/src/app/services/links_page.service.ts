import { Injectable } from '@angular/core';
import {Links} from '../models/links';
import {AngularFireDatabase} from 'angularfire2/database';


@Injectable()
export class LinksPageService {
    linkObj: Links;

    constructor(private db: AngularFireDatabase) {}

    getLinkObj(data) {
        this.linkObj = data;
    }

    getDataFromInstanceID(version, iid) {
        return this.db.object('/links/' + version + '/' + iid).valueChanges();
    }

}
