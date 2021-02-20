import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UsersService} from '../services/users.service';
import {AuthenticationService} from '../services/authentication.service';


@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  constructor(public usersService: UsersService, private authService: AuthenticationService, private  router: Router) {
  }

  public selectedUsername: any;
  public users: any;
  public roles: any;
  public roleName: any;
  public username: any;

  roleUserForm = new RoleUserForm();

  ngOnInit(): void {
    this.usersService.getUsers().subscribe((data: any) => {
      this.users = data;
    }, (err: any) => {
      console.log(err);
    });

    this.usersService.getRoles().subscribe((data: any) => {
      this.roles = data;
    }, (err: any) => {
      console.log(err);
    });
  }


  // tslint:disable-next-line:typedef
  onGetUsername(username: any) {
    this.roleUserForm.username = username;
    console.log(username);
  }


  // tslint:disable-next-line:typedef
  onAddRoleToSelectedUser(roleName: any) {
    console.log(roleName);
    this.roleUserForm.roleName = roleName;
    console.log(this.roleUserForm);
    this.usersService.AddRoleToSelectedUser(this.roleUserForm).subscribe(data => {
      alert('Role ajouté à l\'utilisateur avec succès!!!!');
    }, err => {
      console.log(err);
    });

  }

  // tslint:disable-next-line:typedef
  isAdmin() {
    return this.authService.isAdmin();
  }

  // tslint:disable-next-line:typedef
  isUser() {
    return this.authService.isUser();
  }
}

class RoleUserForm {
  username: string | undefined;
  roleName: string | undefined;

  constructor() {
  }

}


