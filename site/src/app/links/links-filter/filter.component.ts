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
  constructor(
    private filterService: FilterService) { }

  ngOnInit() {
    this.filters = this.filterService.getAll().snapshotChanges().map(filter => {
      return filter.map(f => ({ key: f.payload.key, ...f.payload.val() }));
    });
  }

}
