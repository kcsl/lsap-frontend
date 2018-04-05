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

  @Input('version') version;
  @Input('searchTerm') searchTerm;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private linksService: LinksService
  ) { }

  async populateLinks() {
      await this.linksService
          .populate(this.version) // this will get the version w/ no special chars
          .subscribe(params => { // object instance
              Object.values(params).forEach( data => {
                  const link = new LinksImpl(data);
                  this.linksService.expandLinks(link);
                  this.links.push(link);
              });
              this.filteredLinks = this.links;
          });
  }

  async ngOnInit() {
      if (this.version !== undefined) {
          await this.populateLinks();
      }
  }
}
