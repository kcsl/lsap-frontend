import { FilterService } from '../../services/filter.service';
import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {
  filters;
  private _version;
  driver;

  static stripChars(input) {
      return input.replace(/[^0-9a-z]/gi, '').toString();
  }

  constructor(private filterService: FilterService,
              private route: ActivatedRoute) { }

  get version() {
      if (this._version !== null) {
          return FilterComponent.stripChars(this._version);
      }
  }

  @Input('version')
  set version(val) {
      this._version = val;
      this.getInstanceDrivers();
  }

  ngOnInit() {
      this.route.queryParamMap.subscribe(params => {
          this.driver = params.get('driver');
      });
  }

  async getInstanceDrivers() {
      await this.filterService.getAll(this.version).then(instances => {
          this.filters = instances.map(instance => {
              return (instance as any).driver;
          });
      });
  }

}
