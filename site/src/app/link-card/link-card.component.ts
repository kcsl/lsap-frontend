import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-link-card',
  templateUrl: './link-card.component.html',
  styleUrls: ['./link-card.component.css']
})
export class LinkCardComponent {
  @Input('linkObject') linkObject;
  constructor() { }

  getStyle() {
    return this.linkObject.status === 'paired' ? '#5FAD56' :
            this.linkObject.status === 'deadlock' ? '#706C61' :
            this.linkObject.status === 'partial' ? '#F2C14E' :
            '#A72608';
  }
}
