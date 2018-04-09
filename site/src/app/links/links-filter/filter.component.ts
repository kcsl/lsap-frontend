import { FilterService } from '../../services/filter.service';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {
  filters;
  @Input('filter') filter;
  @Input('version') version;

  constructor(
    private filterService: FilterService) { }

  async ngOnInit() {
      await this.filterService.getAll(this.version).then(instances => {
          this.filters = instances.map(instance => {
            return instance.driver;
          });
      });
  }
}
