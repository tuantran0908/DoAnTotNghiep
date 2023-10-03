import { Location } from '@angular/common';
import { Component, ElementRef, Injector } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FwAction } from 'src/app/common/constants';
import { PagesRequest } from 'src/app/model/model';

export class BaseComponent<T> {
  public el: ElementRef;
  public toastrs: ToastrService;
  public activatedRoute: ActivatedRoute;
  public _location: Location;
  public formBuilder: FormBuilder;
  public action?: string;
  public selectedFiles: File[] = [];
  isSubmit: boolean = false;
  constructor(private injectorObj: Injector) {
    this.el = this.injectorObj.get(ElementRef);
    this.toastrs = this.injectorObj.get(ToastrService);
    this.activatedRoute = this.injectorObj.get(ActivatedRoute);
    this._location = this.injectorObj.get(Location);
    this.formBuilder = this.injectorObj.get(FormBuilder);
    this.page.size = 10;
  }
  maxSize: number = 10;
  autoHide: boolean = false;
  responsive: boolean = true;
  labels: any = {
    previousLabel: 'Trước',
    nextLabel: 'Sau',
  };
  asyncData?: T[];
  page: PagesRequest = new PagesRequest();
  total: number = 0;

  onInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.action = this.activatedRoute.snapshot.data['action'];
    switch (this.action) {
      case FwAction.DETAIL: {
        this.getDetail(id);
        break;
      }
      case FwAction.CREATE: {
        this.getInit();
        break;
      }
      case FwAction.UPDATE: {
        this.getDetail(id);
        break;
      }
      case FwAction.SEARCH: {
        this.search(1);
        break;
      }
      default: {
        //this.search(1)
        break;
      }
    }
  }
  isSearchView(): boolean {
    return FwAction.SEARCH == this.action;
  }

  isReadOnly() {
    return FwAction.DETAIL == this.action;
  }
  isCreate() {
    return FwAction.CREATE == this.action;
  }
  isUpdate() {
    return FwAction.UPDATE == this.action;
  }
  isDetail() {
    return FwAction.DETAIL == this.action;
  }
  isHistory() {
    return FwAction.HISTORY == this.action;
  }
  getCurrentPageNumber(index: number) {
    if (this.page.size) {
      return (this.page.curentPage - 1) * this.page.size + index;
    }
    return 0;
  }

  focusInValidForm() {
    let invalidElements = this.el.nativeElement.querySelectorAll(
      'textarea.form-control.ng-invalid, input.form-control.ng-invalid, datepicker.form-control.ng-invalid input'
    );
    if (invalidElements.length > 0) {
      invalidElements[0].focus();
    }
  }
  create() {}
  update() {}
  getInit() {}
  submitDetail() {
    if (this.isCreate()) {
      this.create();
    } else if (this.isUpdate()) {
      this.update();
    }
  }
  getDetail(id: any) {}
  search(page: any) {}

  onFileSelected(event: any) {
    this.selectedFiles = event.target.files;
  }

  fileToImage(file: any) {
    //  const byteArray = new Int8Array(new ArrayBuffer(this.productDetail.image));
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (e: any) => resolve(e.target.result);
      reader.readAsDataURL(file);
      reader.onerror = (error) => reject(error);
    });
  }
  byteToImage(bytes: any, type: string) {
    const byteCharacters = window.atob(bytes);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    //  const byteArray = new Int8Array(new ArrayBuffer(this.productDetail.image));
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (e: any) => resolve(e.target.result);
      reader.readAsDataURL(new Blob([byteArray], { type: type }));
      reader.onerror = (error) => reject(error);
    });
  }
  convertUnits(number: number): string {
    const units: string[] = ['', 'K', 'M', 'B', 'T'];
    for (let i = 0; i < units.length; i++) {
      if (Math.abs(number) < 1000) {
        return `${number}${units[i]}`;
      }
      number /= 1000;
    }
    return `${number.toFixed(2)}${units[units.length - 1]}`;
  }
}
