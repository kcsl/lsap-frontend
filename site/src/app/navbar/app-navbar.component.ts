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

  public isAdmin;
  versions: String[] = [];
  version;
  searchTerm;
  filter: string;
  constructor(public auth: AuthService,
              private navbarService: NavbarService,
              private router: ActivatedRoute) { }

  async ngOnInit() {
    // this.version = this.selectedVersion();
    this.isAdmin = this.auth.appUser;
    await this.navbarService.getAll().snapshotChanges().subscribe(v => {
        v.forEach(element => {
            this.versions.push(element.payload.val());
        });
        console.log('running nav');
        console.log(this.version);
        if (this.version == null) {
            this.version = this.versions[0];
            console.log(this.version);
        }
    });
  }

  get selectedVersion() {
    return this.version;
  }

  search($event) {
    console.log($event.target.value);
    this.searchTerm = $event.target.value;
  }

  updateVersion(value) {
      console.log(value);
      this.version = value;
  }

  logout() {
    this.auth.logout();
  }
}
