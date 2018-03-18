import { InstanceObject } from '../interfaces/instance';
import { ActivatedRoute } from '@angular/router';
import { LinksService } from '../services/links.service';
import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-links-component',
  templateUrl: './links.component.html',
  styleUrls: ['./links.component.css']
})
export class LinksComponent implements OnInit {
  links: InstanceObject[] = [];
  filteredLinks: InstanceObject[] = [];
  driver: string;

  @Input('version') version;

  constructor(
    private router: ActivatedRoute,
    private linksService: LinksService
  ) { }

  ngOnInit() {
    this.linksService.getAll(this.version).snapshotChanges().subscribe(links => {
      links.forEach(element => {
        this.links.push(element.payload.val());
      });

      this.router.queryParamMap.subscribe(params => {

        this.driver = params.get('driver');

        this.filteredLinks = (this.driver) ?
          this.links.filter(p => p.driver === this.driver) :
          this.links;
      });
    });
  }
}
