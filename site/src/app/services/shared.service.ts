import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class SharedService {
    private term: Subject<string> = new Subject<string>();
    searchTerm = this.term.asObservable();

    getSearch($event) {
        this.term.next($event);
    }
}
