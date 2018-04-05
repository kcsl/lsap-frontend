import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import {ActivatedRoute} from '@angular/router';
import { NavbarService } from '../services/navbar.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html',
  styleUrls: ['./app-navbar.component.css']
})
export class AppNavbarComponent implements OnInit {

  versions: String[] = [];
  version;
  versionStripped;
  searchTerm;
  filter: string;
  constructor(public auth: AuthService,
              private navbarService: NavbarService) { }

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  async ngOnInit() {
    await this.navbarService.getAllVersions().snapshotChanges().subscribe(v => {
        v.forEach(element => {
            this.versions.push(element.payload.val());
        });
        if (this.version == null) {
            this.version = this.versions[0];
            this.versionStripped = AppNavbarComponent.stripChars(this.version);
        }
    });
  }

  get selectedVersion() {
    return this.versionStripped;
  }

  search($event) {
    this.searchTerm = $event.target.value;
  }

  updateVersion(value) {
      this.version = value;
      this.versionStripped = AppNavbarComponent.stripChars(this.version);
  }

  logout() {
    this.auth.logout();
  }
}
