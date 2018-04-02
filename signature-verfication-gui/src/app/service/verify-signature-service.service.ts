import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http'
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

@Injectable()
export class VerifySignatureServiceService {

  constructor(private http: Http) { }
  
  callCore(name): Observable<any> {
    const url = 'http://localhost:8080/api/signature?name=' + name;
    return this.http.get(url).map(
      res => {
        const data = res.json;
        return data;
      }
    )
  }

  
  getData() {
    return this.http.get('http://localhost:8080/api/signature')
      .map((res: any) => res);
  }
}
