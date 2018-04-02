import { TestBed, inject } from '@angular/core/testing';

import { VerifySignatureServiceService } from './verify-signature-service.service';

describe('VerifySignatureServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VerifySignatureServiceService]
    });
  });

  it('should be created', inject([VerifySignatureServiceService], (service: VerifySignatureServiceService) => {
    expect(service).toBeTruthy();
  }));
});
