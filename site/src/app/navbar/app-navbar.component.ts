import {Component, Input, OnChanges, OnInit} from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit, OnChanges {

  @Input('versions') versions: String[] = [];
  @Input('version') version;
  @Input('versionStripped') versionStripped;
  searchTerm;
  constructor() { }

  ngOnInit() {}

  ngOnChanges() {
    console.log(this.versionStripped);
  }

  search($event) {
    this.searchTerm = $event.target.value;
  }
}
