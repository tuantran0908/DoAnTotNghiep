import { Component } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { LoaderService } from 'src/app/services/loader/loader.service';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent {
  isLoading: BehaviorSubject<boolean> = this.loaderService.isLoading;
  constructor(private loaderService: LoaderService){}
  ngOnInit() {
  }
  
}
