package perscholas.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import perscholas.database.dao.UserDAO;
import perscholas.database.entity.User;
import perscholas.database.entity.UserRole;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserDAO userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // if the incoming username or email is out found in the database
        // this user will be null
        // TODO if you want to login using username column you can change this to findByUsername or even findByUsernameOrEmail
        User user = userDao.findByEmail(username);

        if (user == null) {
            // this means that the username was not found in the database so we are done
            // and we can get out of here
            throw new UsernameNotFoundException("Username '" + username + "' not found in database");
        }

        // we do not need to do anything with these flags for the project
        // check the account status
        boolean accountIsEnabled = true;
        //accountIsEnabled = user.isActive();

        // spring security configs
        boolean accountNonExpired = true;

        //if ( user.getExpirationDate().before(new Date()))
        boolean credentialsNonExpired = true;

        boolean accountNonLocked = true;

        // setup user roles
        // List<Permission> permissions = userDao.getPermissionsByEmail(username);
        // Collection<? extends GrantedAuthority> springRoles = buildGrantAuthorities(permissions);
        List<UserRole> userRoles = userDao.getUserRoles(user.getId());
        Collection<? extends GrantedAuthority> springRoles = buildGrantAuthorities(userRoles);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), accountIsEnabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked, springRoles);
    }

//	private Collection<? extends GrantedAuthority> buildGrantAuthorities(List<Permission> permissions) {
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		for (Permission permission : permissions) {
//			authorities.add(new SimpleGrantedAuthority(permission.getName()));
//		}
//
//		return authorities;
//	}

    private Collection<? extends GrantedAuthority> buildGrantAuthorities(List<UserRole> userRoles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (UserRole role : userRoles) {
            authorities.add(new SimpleGrantedAuthority(role.getUserRole().toString()));
        }

        // always add the user role
        //authorities.add(new SimpleGrantedAuthority(UserRoleEnum.USER.toString()));

        return authorities;
    }

}
