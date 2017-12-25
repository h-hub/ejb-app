package com.dstaapp.session;

import javax.ejb.Local;

import com.dstaapp.model.User; 

@Local 
public interface MangeUserSessionBeanLocal {
	public boolean addUser(User user); 
}
