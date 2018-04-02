import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest, HttpEvent} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
@Injectable()
export class UploadDocumentsToverfiedServiceService {

  constructor(private http: HttpClient) {}

  pushFileToStorage(file: File, originalSignature: string): Observable<HttpEvent<{}>> {
    let formdata: FormData = new FormData();

    formdata.append('file', file);
    formdata.append('prototype', originalSignature);

    const req = new HttpRequest('POST', 'http://localhost:8080/api/signature/post', formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get('http://localhost:8080/api/signature')
  }

}
