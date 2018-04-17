import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NavbarService} from '../services/navbar.service';
import {SharedService} from '../services/shared.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  version;
  versions: String[] = [];
  versionStripped;
  driver;

  constructor(
    private route: ActivatedRoute,
    private navbarService: NavbarService,
    private router: Router,
    private sharedService: SharedService,
  ) { }

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  passToLinks($event) {
      this.sharedService.getSearch($event);
  }

  ngOnInit() {
      this.navbarService.getAllVersions().snapshotChanges().subscribe(v => {
          v.forEach(element => {
              this.versions.push(element.payload.val());
          });
          if (this.version == null) {
              this.version = this.versions[0];
              this.versionStripped = HomeComponent.stripChars(this.version);
              this.router.navigate(['/v/' + this.versionStripped + '/']);
          }
      });

      this.route.paramMap.subscribe(params => {
          this.version = params.get('versionStripped');
          this.versionStripped = HomeComponent.stripChars(this.version);
      });
  }
}
