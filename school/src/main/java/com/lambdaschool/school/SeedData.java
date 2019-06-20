package com.lambdaschool.school;

import com.lambdaschool.school.model.Role;
import com.lambdaschool.school.model.User;
import com.lambdaschool.school.model.UserRoles;
import com.lambdaschool.school.service.RoleService;
import com.lambdaschool.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Component
public class SeedData implements CommandLineRunner
{
    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;


    @Override
    public void run(String[] args) throws Exception
    {
        Role r2 = new Role("user");

        roleService.save(r2);

        // admin, data, user
        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r2));
        User u1 = new User("admin", "password", admins);
        userService.save(u1);

    }
}