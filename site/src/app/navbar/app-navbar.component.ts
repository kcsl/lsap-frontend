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
  filter: string;
  constructor(public auth: AuthService,
              private navbarService: NavbarService,
              private router: ActivatedRoute) { }

  ngOnInit() {
    this.isAdmin = this.auth.appUser;
    this.navbarService.getAll().snapshotChanges().subscribe(v => {
        v.forEach(element => {
            this.versions.push(element.payload.val());
        });
    });
  }

    get selectedVersion() {
      return this.versions;
    }

    set selectedVersion(value) {
      this.version = value;
    }

  logout() {
    this.auth.logout();
  }
}
