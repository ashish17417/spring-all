import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadOriginalsComponentComponent } from './upload-originals-component.component';

describe('UploadOriginalsComponentComponent', () => {
  let component: UploadOriginalsComponentComponent;
  let fixture: ComponentFixture<UploadOriginalsComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadOriginalsComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadOriginalsComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
