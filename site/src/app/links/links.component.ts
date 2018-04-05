import {ActivatedRoute, Router} from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Input, OnInit} from '@angular/core';
import {Links, LinksImpl} from '../models/links';
import { Graph } from '../models/graph';
import 'rxjs/add/operator/switchMap';
import {AngularFireStorage} from 'angularfire2/storage';

@Component({
  selector: 'app-links-component',
  templateUrl: './links.component.html',
  styleUrls: ['./links.component.css'],
})
export class LinksComponent implements OnInit {

  instance_list: any[] = [];
  links: Links[] = [];
  filteredLinks: Links[];
  graph: Graph;

  @Input('version') version;
  @Input('searchTerm') searchTerm;

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private linksService: LinksService
  ) { }

  async populateLinks() {
      await this.linksService
          .populate(LinksComponent.stripChars(this.version)) // this will get the version w/ no special chars
          .subscribe(params => { // object instance
              console.log(params);
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
