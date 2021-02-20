package com.sid.authservice.sec.service;


import com.sid.authservice.sec.entites.AppRole;
import com.sid.authservice.sec.entites.AppUser;

import java.util.List;

public interface AccountService {
    // public AppUser addNewUser(String username, String password, String ComfirmedPassword);
    public AppUser addNewUser(AppUser appUser);

    public AppRole addNewRole(AppRole appRole);

    public void addRoleToUser(String username, String roleName);

    public void removeRoleFromUser(String username, String roleName);

    public AppUser loadUserByUsername(String username);

    List<AppUser> allUser();

    List<AppRole> allRole();
}
