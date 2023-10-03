import { Component, ElementRef } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Constants } from 'src/app/common/constants';
import { LoginRequest } from 'src/app/model/model';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  isSubmit = false;
  loginRequest: LoginRequest = new LoginRequest();
  homeUrl = '/';
  loginForm = this.formBuilder.group({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });
  constructor(
    private formBuilder: FormBuilder,
    private el: ElementRef,
    private toastrs: ToastrService ,private authService:AuthService
  ) {}
  ngOnInit() {}
  focusInValidForm() {
    let invalidElements = this.el.nativeElement.querySelectorAll(
      'textarea.form-control.ng-invalid, input.form-control.ng-invalid, datepicker.form-control.ng-invalid input'
    );
    if (invalidElements.length > 0) {
      invalidElements[0].focus();
    }
  }
  onSubmit(): void {
    // Process checkout data here
    this.isSubmit=true
    if (this.loginForm.status == 'INVALID') {
      this.focusInValidForm()
      return;
    } else if (this.loginForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      this.authService.login(this.loginRequest).subscribe(
       loginResponse =>{
        
        if(Constants.OK == loginResponse.errorCode){
          // this.router.navigate([this.homeUrl]);
          window.location.replace(this.homeUrl);

        }else{
          this.toastrs.error(loginResponse.errorMessage);
        }
      });
    }
  }
}
