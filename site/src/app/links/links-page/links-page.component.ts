import {Component, OnInit} from '@angular/core';
import {Links, LinksImpl} from '../../models/links';
import {ActivatedRoute} from '@angular/router';
import {LinksPageService} from '../../services/links_page.service';
import {LinksService} from '../../services/links.service';

@Component({
  selector: 'app-links-page',
  templateUrl: './links-page.component.html',
  styleUrls: ['./links-page.component.css']
})
export class LinksPageComponent implements OnInit {

  private link: Links;
  private version: string;
  private iid: string;

  constructor(
      private route: ActivatedRoute,
      private linksPageService: LinksPageService,
      private linksService: LinksService
  ) { }

  async ngOnInit() {
      await this.route.parent.paramMap.subscribe(params => {
          console.log('parent: ' + params.get('versionStripped'));
          this.version = params.get('versionStripped');
      });
      await this.route.paramMap.subscribe(params => {
          console.log('cwd: ' + params.get('id'));
          this.iid = params.get('id');
      });
      await this.linksPageService.getDataFromInstanceID(this.version, this.iid).subscribe(data => {
          if (data) {
            const link = new LinksImpl(data);
            this.linksService.expandLinks(link);
            this.link = link;
          }
      });
  }

}
