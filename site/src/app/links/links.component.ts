import {ActivatedRoute, Router} from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Input, OnInit} from '@angular/core';
import {Links} from '../interfaces/links';
import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'app-links-component',
  templateUrl: './links.component.html',
  styleUrls: ['./links.component.css'],
})
export class LinksComponent implements OnInit {

  driver: string;

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
      // await this.linksService.getVersion(this.version);
      await this.linksService.getAll(this.version).snapshotChanges()
          .map(links => {

            links.forEach(function (data) {
              console.log(data);

              });

              // this.links = links;
              // this.filteredLinks = links;
              // return this.route.queryParamMap;
          });
          // .subscribe(params => {
          // });
  }

  async ngOnInit() {
      console.log('running links');
      console.log(this.version, this.searchTerm);
    await this.populateLinks();
  }

  search($event) {
    console.log($event.target.value);
    const searchTerm = $event.target.value;
    this.filteredLinks = this.links.filter((data) => {
        return data.title.toUpperCase().includes(searchTerm.toUpperCase())
            || data.driver.toUpperCase().includes(searchTerm.toUpperCase());
    });
  }
}
