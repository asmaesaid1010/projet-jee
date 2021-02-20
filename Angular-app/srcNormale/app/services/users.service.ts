import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {


  public host = 'http://localhost:8080';
  // public accesToken: string | null | undefined;
  roles: Array<string> = [];
  headers = new HttpHeaders();

  constructor(private http: HttpClient, private authService: AuthenticationService) {
    this.headers.set('Authorization', 'Bearer ' + this.authService.loadToken());
    console.log(this.headers);
  }


  // tslint:disable-next-line:typedef
  public getUsers() {
    if (this.authService.jwt == null) {
      this.authService.loadToken();
    }
// const headers = new HttpHeaders({Authorization: 'Bearer ' + this.accesToken});


    return this.http.get(this.host + '/users', {headers: this.headers});
  }

  // tslint:disable-next-line:typedef
  public getRoles() {
    if (this.authService.jwt == null) {
      this.authService.loadToken();
    }
    return this.http.get(this.host + '/roles', {headers: this.headers});
  }

  // tslint:disable-next-line:typedef
  public AddRoleToSelectedUser(RoleUserForm: any) {
    return this.http.post(this.host + '/addRoleToUser', RoleUserForm, {headers: this.headers});
  }
}
