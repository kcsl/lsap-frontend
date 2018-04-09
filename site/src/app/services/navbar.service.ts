import { Injectable } from '@angular/core';
import { AngularFireDatabase } from 'angularfire2/database';

@Injectable()
export class NavbarService {

    constructor(private db: AngularFireDatabase) { }

    getAllVersions() {
        return this.db.list('/versions');
    }
}
