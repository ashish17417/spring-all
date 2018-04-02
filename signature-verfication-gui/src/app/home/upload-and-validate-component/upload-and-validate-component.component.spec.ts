import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadAndValidateComponentComponent } from './upload-and-validate-component.component';

describe('UploadAndValidateComponentComponent', () => {
  let component: UploadAndValidateComponentComponent;
  let fixture: ComponentFixture<UploadAndValidateComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadAndValidateComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadAndValidateComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
