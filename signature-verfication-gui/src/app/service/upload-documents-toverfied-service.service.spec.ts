import { TestBed, inject } from '@angular/core/testing';

import { UploadDocumentsToverfiedServiceService } from './upload-documents-toverfied-service.service';

describe('UploadDocumentsToverfiedServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UploadDocumentsToverfiedServiceService]
    });
  });

  it('should be created', inject([UploadDocumentsToverfiedServiceService], (service: UploadDocumentsToverfiedServiceService) => {
    expect(service).toBeTruthy();
  }));
});
