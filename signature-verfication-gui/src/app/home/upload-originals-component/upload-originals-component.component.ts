import { UploadDocumentsToverfiedServiceService } from '../../service/upload-documents-toverfied-service.service';
import { HttpResponse } from '@angular/common/http';
import { HttpEventType } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-upload-originals-component',
  templateUrl: './upload-originals-component.component.html',
  styleUrls: ['../../../../node_modules/bootstrap/dist/css/bootstrap.min.css'],
  providers: [UploadDocumentsToverfiedServiceService]
})
export class UploadOriginalsComponentComponent implements OnInit {

  fileUploads: Observable<string[]>;
  selectedFiles: FileList;
  currentFileUpload: File;
  @Input() fileUpload: string;
  progress: {percentage: number} = {percentage: 0};
  public message;
  constructor(private uploadDocumentsToverfiedServiceService: UploadDocumentsToverfiedServiceService) { }

  ngOnInit() {
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }
  
  upload() {
        this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadDocumentsToverfiedServiceService.pushOriginalFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
            alert("Original Signature is uploaded successfully to backend");
            this.refresh();
      }
    });

    this.selectedFiles = undefined;
  }
  
  refresh(): void {
    window.location.reload();
}
}
