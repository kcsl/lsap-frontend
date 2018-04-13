import {ActivatedRoute, Router} from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Inject, Input, OnInit} from '@angular/core';
import {Links, LinksImpl} from '../models/links';
import 'rxjs/add/operator/switchMap';
import {HomeComponent} from '../home/home.component';
import {SharedService} from '../services/shared.service';

@Component({
  selector: 'app-links-component',
  templateUrl: './links.component.html',
  styleUrls: ['./links.component.css'],
})
export class LinksComponent implements OnInit {

  links: Links[] = [];
  filteredLinks: Links[];
  driver;

  private _version;
  private _searchTerm;

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private linksService: LinksService,
    private sharedService: SharedService
  ) {}

  get version() {
      return this._version;
  }

  @Input('version')
  set version(val: string) {
      this._version = val;
      this.populateLinks();
  }

  @Input('searchTerm')
  set searchTerm(val) {
      this._searchTerm = val;
      // change filteredLinks based on new search term
      this.filter();
  }

  get searchTerm() {
      return this._searchTerm;
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

  filter() {
      this.filteredLinks = this.links.filter((link) => {
          // check to see if it matches any of our search parameters
          return link.title.toUpperCase().includes(this._searchTerm.toUpperCase())
              || link.type.toUpperCase().includes(this._searchTerm.toUpperCase())
              || link.driver.toUpperCase().includes(this._searchTerm.toUpperCase())
              || link.status.toUpperCase().includes(this._searchTerm.toUpperCase())
              || link.filename.toUpperCase().includes(this._searchTerm.toUpperCase())
              || link.instance_id.toUpperCase().includes(this._searchTerm.toUpperCase());
      });
  }

  ngOnInit() {
      // get current drivers for filtering via drivers to work
      this.route.queryParamMap.subscribe(params => {
          this.driver = params.get('driver');
          this.filteredLinks = (this.driver) ?
              this.links.filter(link => link.driver === this.driver) :
              this.links;
      });
      // get versionStripped in order to be able to navigate into LinksPage
      this.route.parent.paramMap.subscribe(params => {
          this.version = params.get('versionStripped');
      });
      // get searchTerm from the nav-bar search bar
      this.sharedService.searchTerm.subscribe(term => this.searchTerm = term);
  }
}
