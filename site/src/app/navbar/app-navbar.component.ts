import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit {

  @Input('versions') versions: String[] = [];
  private _version: string;
  private _searchTerm: string;
  @Input('versionStripped') versionStripped;

  @Output() versionChange = new EventEmitter();
  @Output() searchTermChange = new EventEmitter();

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {}

  search($event) {
    this.searchTerm = $event.target.value;
  }

  get version() {
    return this._version;
  }

  @Input('version')
  set version(val: string) {
      this._version = val;
      this.versionChange.emit(this.version);
  }

  get searchTerm() {
    return this._searchTerm;
  }

  @Input('searchTerm')
  set searchTerm(val: string) {
      this._searchTerm = val;
      this.searchTermChange.emit(this.searchTerm);
  }

  navigateToNewVersion(version: string) {
    this.version = version;
    this.versionStripped = AppNavbarComponent.stripChars(version);
    this.router.navigate(['/v/' + this.versionStripped + '/']);
  }
}
