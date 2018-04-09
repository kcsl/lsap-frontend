import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NavbarService} from '../services/navbar.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  version;
  versions: String[] = [];
  versionStripped;

  constructor(private route: ActivatedRoute, private navbarService: NavbarService) { }

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
          }
          this.versionStripped = HomeComponent.stripChars(this.version);
      });

      this.route.paramMap.subscribe(params => {
          this.version = params.get('versionStripped');
      });
  }
}
