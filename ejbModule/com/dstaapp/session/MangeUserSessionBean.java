package com.dstaapp.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.dstaapp.model.Role;
import com.dstaapp.model.User;

/**
 * Session Bean implementation class MangeUserSessionBean
 */
@Stateless
@LocalBean
public class MangeUserSessionBean implements MangeUserSessionBeanLocal {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public MangeUserSessionBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addUser(User user) {
		entityManager.persist(user);
		return false;
	}

	/**
	 * Returns a list of Customer objects in the database
	 * 
	 * @return List<Customer>
	 */

	public List<User> retrieve() {
		Query query = entityManager.createNamedQuery("Customer.findAll");
		return query.getResultList();
	}

	/**
	 * * Update the customer record
	 * 
	 * @param customer
	 *            object to be updated
	 * @return Customer
	 */
	public User update(User user) {
		return entityManager.merge(user);
	}

	public boolean create(String username, String email, String password, String role) {

		User newUser = new User(username, email, password);

		Query query = entityManager.createQuery("SELECT e FROM Role e");
		// return (Collection<Professor>) query.getResultList();

		@SuppressWarnings("unchecked")
		List<Role> userRole = query.getResultList();

		Set<Role> roles = new HashSet<Role>(userRole);

		newUser.setRoles(roles);
		entityManager.persist(newUser);
		return true;
	}

}
