package com.dstaapp.session;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
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

	@Resource(name = "jms/queue/notification")
	private Queue notificationQueue;

	@Resource(name = "java:jboss/exported/jms/RemoteConnectionFactory")
	private ConnectionFactory notificationQueueFactory;

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

		try {
			sendJMSMessageToNotificationQueue(newUser);
		} catch (JMSException ex) {
			// Logger.getLogger(CustomerSessionBean.class.getName()).log(Level.SEVERE, null,
			// ex);
		}

		return true;
	}

	private Message createJMSMessageForjmsNotificationQueue(Session session, Object messageData) throws JMSException {
		// Modified to use ObjectMessage instead
		ObjectMessage tm = session.createObjectMessage();
		tm.setObject((Serializable) messageData);
		return tm;
	}

	private void sendJMSMessageToNotificationQueue(Object messageData) throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			connection = notificationQueueFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(notificationQueue);
			messageProducer.send(createJMSMessageForjmsNotificationQueue(session, messageData));
		} finally {
			if (session != null) {
				try {
					session.close();

				} catch (JMSException e) {
					// Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close
					// session", e);
				}
			}
			if (connection != null) {
				connection.close();
			}
		}

	}
}
