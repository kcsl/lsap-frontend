import {ActivatedRoute, Router} from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Input, OnInit} from '@angular/core';
import {Links, LinksImpl} from '../models/links';
import 'rxjs/add/operator/switchMap';

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
  @Input('searchTerm') searchTerm;

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

  async populateLinks() {
      console.log('links version: ' + this.version);
      this.filteredLinks = [];
      this.links = [];
      await this.linksService
          .populate(LinksComponent.stripChars(this.version)) // this will get the version w/ no special chars
          .subscribe(params => { // object instance
              Object.values(params).forEach( data => {
                  const link = new LinksImpl(data);
                  this.linksService.expandLinks(link);
                  this.links.push(link);
              });
              this.filteredLinks = this.links;
          });
  }

  ngOnInit() {
      this.route.queryParamMap.subscribe(params => {
          this.driver = params.get('driver');
          this.filteredLinks = (this.driver) ?
              this.links.filter(link => link.driver === this.driver) :
              this.links;
      });
  }
}
