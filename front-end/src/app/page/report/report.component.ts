import { Component, Injector, ViewChild } from '@angular/core';
import {
  ApexChart,
  ApexNonAxisChartSeries,
  ApexResponsive,
  ChartComponent,
} from 'ng-apexcharts';
import { ToastrService } from 'ngx-toastr';
import { FwError } from 'src/app/common/constants';
import {
  Bill,
  GiaoTrinh,
  PagesRequest,
  ReportSearchRequest,
  Review,
} from 'src/app/model/model';
import { ReportService } from 'src/app/services/report/report.service';
export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
};
@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss'],
})
export class ReportComponent {
  @ViewChild('chart') chart?: ChartComponent;
  public chartOptions: Partial<ChartOptions> = {};
  totalQuantity: any;
  giaoTrinhs: GiaoTrinh[] = [];
  reviews: Review[] = [];
  bills: Bill[] = [];
  pageGiaoTrinh: PagesRequest = new PagesRequest();
  pageReview: PagesRequest = new PagesRequest();
  pageBill: PagesRequest = new PagesRequest();
  reportSearchRequest: ReportSearchRequest = new ReportSearchRequest();
  timeList: any = [
    { label: '7 ngày gần nhất', value: this.getNextWeek(7) },
    { label: '14 ngày gần nhất', value: this.getNextWeek(14) },
    { label: '21 ngày gần nhất', value: this.getNextWeek(21) },
    { label: '1 tháng gần nhất', value: this.getNextMonth(1) },
    { label: '2 tháng gần nhất', value: this.getNextMonth(2) },
    { label: '3 tháng gần nhất', value: this.getNextMonth(3) },
  ];
  searchType: string = 'giao-trinh';
  constructor(
    private reportService: ReportService,
    private toastrs: ToastrService
  ) {}
  ngOnInit() {
    this.getTotalQuantity();
    this.getTotalCategory();
    this.search();
  }
  setSearchType(searchType: string) {
    if (this.searchType != searchType) {
      this.searchType = searchType;
      this.search();
    }
  }
  getNextWeek(value: number) {
    let result = new Date();
    result.setDate(new Date().getDate() - value);
    return result;
  }
  getNextMonth(value: number) {
    let result = new Date();
    result.setDate(new Date().getMonth() - value);
    return result;
  }
  search() {
    if (this.searchType == 'giao-trinh')
      this.searchGiaoTrinh(this.pageGiaoTrinh.curentPage);
    if (this.searchType == 'nhan-xet')
      this.searchReviews(this.pageReview.curentPage);
    if (this.searchType == 'don-hang')
      this.searchBills(this.pageBill.curentPage);
  }
  getTotalQuantity() {
    this.reportService.getTotalQuantity().subscribe((res) => {
      this.totalQuantity = res.data;
    });
  }
  getTotalCategory() {
    this.reportService.getTotalCategory().subscribe((res) => {
      this.chartOptions = {
        series: Object.values(res.data),
        chart: {
          width: 600,
          type: 'pie',
        },
        labels: Object.keys(res.data),
        responsive: [
          {
            breakpoint: 1000,
            options: {
              chart: {
                width: 600,
              },
              legend: {
                position: 'bottom',
              },
            },
          },
        ],
      };
    });
  }
  searchGiaoTrinh(page: any) {
    this.pageGiaoTrinh.size = 10;
    this.pageGiaoTrinh.curentPage = page;
    this.reportService
      .searchGiaoTrinh(this.pageGiaoTrinh, this.reportSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) this.giaoTrinhs = res.data.content;
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  searchReviews(page: any) {
    this.pageReview.size = 10;
    this.pageReview.curentPage = page;
    this.reportService
      .searchReviews(this.pageReview, this.reportSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) this.reviews = res.data.content;
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  searchBills(page: any) {
    this.pageBill.size = 10;
    this.pageBill.curentPage = page;
    this.reportService
      .searchBills(this.pageBill, this.reportSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content)
            this.bills = res.data.content.map((item) => {
              item.payment == '1' &&
                (item.payment = 'Thanh toán khi nhận hàng');
              item.payment == '0' && (item.payment = 'Thanh toán online');
              return item;
            });
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
}
