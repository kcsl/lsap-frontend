import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit {

  @Input('versions') versions: String[] = [];
  @Input('version') version;
  @Input('versionStripped') versionStripped;
  searchTerm;
  filter: string;
  constructor() { }

  ngOnInit() {}

  search($event) {
    this.searchTerm = $event.target.value;
  }
}
