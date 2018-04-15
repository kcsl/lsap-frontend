import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Links} from '../../models/links';
import {LinksPageService} from '../../services/links_page.service';

@Component({
  selector: 'app-link-card',
  templateUrl: './link-card.component.html',
  styleUrls: ['./link-card.component.css']
})
export class LinkCardComponent implements OnInit {
  private _linkObject: Links;
  private _version: string;

  constructor(private linksPageService: LinksPageService) { }

  /*getter & setter for linkObject*/
  get linkObject() {
    return this._linkObject;
  }

  @Input('linkObject')
  set linkObject(val: Links) {
    this._linkObject = val;
    this.linksPageService.getLinkObj(this._linkObject);
  }
  /*end getter and setter for linkObject*/

    /*getter & setter for version*/
    get version() {
        return this._version;
    }

    @Input('version')
    set version(val: string) {
        this._version = val;
    }
    /*end getter and setter for version*/

  getStyle() {
    return this.linkObject.status === 'paired' ? '#5FAD56' :
            this.linkObject.status === 'deadlock' ? '#706C61' :
            this.linkObject.status === 'partial' ? '#F2C14E' :
            '#A72608';
  }

  ngOnInit() {}

}
