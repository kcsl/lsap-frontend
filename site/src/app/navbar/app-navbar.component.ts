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
  @Input('versionStripped') versionStripped;

  @Output() versionChange = new EventEmitter();
  searchTerm;

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
      console.log(this.version);
      this.versionChange.emit(this.version);
  }

  navigateToNewVersion(version: string) {
    this.version = version;
    this.versionStripped = AppNavbarComponent.stripChars(version);
    this.router.navigate(['/v/' + this.versionStripped + '/']);
  }
}
