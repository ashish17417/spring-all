import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifySignatureComponentComponent } from './verify-signature-component.component';

describe('VerifySignatureComponentComponent', () => {
  let component: VerifySignatureComponentComponent;
  let fixture: ComponentFixture<VerifySignatureComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VerifySignatureComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VerifySignatureComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
