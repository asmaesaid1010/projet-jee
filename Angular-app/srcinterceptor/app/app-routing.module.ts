import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {UsersComponent} from './users/users.component';
import {AuthGuard} from './guards/auth.guard';

const routes: Routes = [


  {
    path: 'login',
    component: LoginComponent
  }, {
    path: 'users',
    component: UsersComponent, canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
