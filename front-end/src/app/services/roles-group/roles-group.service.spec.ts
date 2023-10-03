import { TestBed } from '@angular/core/testing';

import { RolesGroupService } from './roles-group.service';

describe('RolesGroupService', () => {
  let service: RolesGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RolesGroupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
