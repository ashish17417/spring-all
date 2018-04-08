import {UploadDocumentsToverfiedServiceService} from '../../service/upload-documents-toverfied-service.service';
import {Component, OnInit, Input} from '@angular/core';
import {HttpModule} from '@angular/http';
import {Router} from '@angular/router';
import {VerifySignatureServiceService} from '../../service/verify-signature-service.service'
import {HttpResponse} from '@angular/common/http';
import {HttpEventType} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Original} from './Originals';
import {MatTableDataSource} from '@angular/material';
import {VerificationResult} from './VerificationResult'

@Component({
  selector: 'app-verify-signature-component',
  templateUrl: './verify-signature-component.component.html',
  styleUrls: ['../../../../node_modules/bootstrap/dist/css/bootstrap.min.css'],
  providers: [VerifySignatureServiceService, UploadDocumentsToverfiedServiceService]
})


export class VerifySignatureComponentComponent implements OnInit {

  public input;
  public originalSignatureValue;
  public result;
  showFile = false;
  fileUploads: Observable<string[]>;
  selectedFiles: FileList;
  currentFileUpload: File;
  @Input() fileUpload: string;
  progress: {percentage: number} = {percentage: 0};
  public originals: Original[];
  public verificationResult: VerificationResult[];
  public selectedOriginal;
  displayedColumns = ['originalSignature', 'testSubjectDocument', 'matched', 'matchCount'];
  public data: VerificationResult[] = [];
  public datastr;
  public test = '<h2>ASHISH INGLE</h2>'
  constructor(private verifySignatureServiceService: VerifySignatureServiceService,
    private uploadDocumentsToverfiedServiceService: UploadDocumentsToverfiedServiceService) {}
  public selectedValueByte64;
  ngOnInit() {
    this.verifySignatureServiceService.getData().subscribe(
      res => {
        console.log(res._body);
        console.log(JSON.parse(res._body));
        this.originals = JSON.parse(res._body);
      }
    )
  }


  originalSignature(event) {
    this.originalSignatureValue = event.target.value;

  }

  selected(event: any, originals: Original) {
    if (event.source.selected) {
      this.selectedOriginal = originals.value;
      this.selectedValueByte64 = originals.viewValue;
      console.log("selected val " + this.selectedOriginal);
    }
  }
  onKeyup(event) {
    this.input = event.target.value;
  }

  callTest() {
    console.log('cllCoreByRest');
    this.verifySignatureServiceService.getData().subscribe(
      res => {
        this.result = res;
      }
    )
    return this.result;
  }

  showFiles(enable: boolean) {
    this.showFile = enable

    if (enable) {
      // this.fileUploads = this.uploadDocumentsToverfiedServiceService.getFiles();
    }
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadDocumentsToverfiedServiceService.pushFileToStorage(this.currentFileUpload, this.selectedOriginal).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
       this.datastr = event.body;
        console.log(this.datastr);
        this.verificationResult = JSON.parse(this.datastr);
      }
    });

    this.selectedFiles = undefined;
  }


}
