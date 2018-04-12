import {ActivatedRoute, Router} from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Input, OnInit} from '@angular/core';
import {Links, LinksImpl} from '../models/links';
import 'rxjs/add/operator/switchMap';
import {HomeComponent} from '../home/home.component';

@Component({
  selector: 'app-links-component',
  templateUrl: './links.component.html',
  styleUrls: ['./links.component.css'],
})
export class LinksComponent implements OnInit {

  links: Links[] = [];
  filteredLinks: Links[];
  driver;

  private _searchTerm;
  private _version;

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private linksService: LinksService
  ) { }

  get version() {
      return this._version;
  }

  @Input('version')
  set version(val: string) {
      this._version = val;
      this.populateLinks();
  }

    get searchTerm() {
        return this._searchTerm;
    }

    @Input('searchTerm')
    set searchTerm(val: string) {
        this._searchTerm = val;
        console.log(this._searchTerm);
        this.filter();
    }

  async populateLinks() {
      // reset the data
      this.links = [];
      this.filteredLinks = this.links;

      await this.linksService
          .populate(LinksComponent.stripChars(this.version)) // this will get the version w/ no special chars
          .subscribe(params => { // object instance
              // null check
              if (params) {
                  Object.values(params).forEach(data => {
                      const link = new LinksImpl(data);
                      this.linksService.expandLinks(link);
                      this.links.push(link);
                  });
                  this.filteredLinks = this.links;
              }
          });
  }

  filter() {}

  ngOnInit() {
      this.route.queryParamMap.subscribe(params => {
          this.driver = params.get('driver');
          this.filteredLinks = (this.driver) ?
              this.links.filter(link => link.driver === this.driver) :
              this.links;
      });
      this.route.paramMap.subscribe(params => {
         this.searchTerm = params.get('searchTerm');
      });
      this.route.parent.paramMap.subscribe(params => {
          this.version = params.get('versionStripped');
      });
  }
}
