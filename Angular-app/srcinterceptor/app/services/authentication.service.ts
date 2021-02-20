import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  public host2 = 'http://localhost:8080';
  jwt: string | null | undefined;
  username: undefined;
  roles: Array<any> = [];

  constructor(private http: HttpClient) {
  }

  // tslint:disable-next-line:typedef
  public login(data: any) {
    return this.http.post(this.host2 + '/login', data, {observe: 'response'});
  }

  // tslint:disable-next-line:typedef
  saveToken(jwt: string | null) {
    this.jwt = jwt;
    if (jwt != null) {
      localStorage.setItem('token', jwt);
    }
    this.parseJWT();

  }

  // tslint:disable-next-line:typedef
  parseJWT() {
    const jwtHelper = new JwtHelperService();
    // @ts-ignore
    const objJWT = jwtHelper.decodeToken(this.jwt);
    this.username = objJWT.obj;
    this.roles = objJWT.roles;
  }

  // tslint:disable-next-line:typedef
  isAdmin() {
    return this.roles.indexOf('ADMIN') >= 0;
  }

  // tslint:disable-next-line:typedef
  isUser() {
    return this.roles.indexOf('USER') >= 0;
  }

  // tslint:disable-next-line:typedef
  isAuthenticated() {
    return this.roles && this.isAdmin() || this.isUser();
  }

  // tslint:disable-next-line:typedef
  loadToken() {
    this.jwt = localStorage.getItem('token');
    return this.jwt;
  }

  // tslint:disable-next-line:typedef
  logout() {
    localStorage.removeItem('token');

    this.initParams();
  }

  // tslint:disable-next-line:typedef
  initParams() {
    this.jwt = undefined;
    this.username = undefined;
    this.roles = [];
  }


  /*getUserAuthentificated(){

    console.log(this.username);
    return this.username;
  }*/
}
