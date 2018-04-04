import {ActivatedRoute, Router} from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Input, OnInit} from '@angular/core';
import {Links} from '../models/links';
import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'app-links-component',
  templateUrl: './links.component.html',
  styleUrls: ['./links.component.css'],
})
export class LinksComponent implements OnInit {

  file_list: string[] = [];
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
      await this.linksService.populate(this.stripChars(this.version)) // this will get the version w/ no special chars
          .snapshotChanges()
          .subscribe(params => { // object instance
              params.map(data => { // object data for instance
                  this.links.push(data.payload.val());
                  console.log(data.payload.val());
              });
              this.filteredLinks = this.links; // set filtered links to all links initially
          }).unsubscribe();
  }

  async ngOnInit() {
      if (this.version !== undefined) {
          console.log(this.version);
          await this.populateLinks();
      }
  }

  stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }
}
