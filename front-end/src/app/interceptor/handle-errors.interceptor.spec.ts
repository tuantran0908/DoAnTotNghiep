import { TestBed } from '@angular/core/testing';

import { HandleErrorsInterceptor } from './handle-errors.interceptor';

describe('HandleErrorsInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      HandleErrorsInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: HandleErrorsInterceptor = TestBed.inject(HandleErrorsInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
