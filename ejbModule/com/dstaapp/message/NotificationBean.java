package com.dstaapp.message;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.dstaapp.model.User;

/**
 * Message-Driven Bean implementation class for: NotificationBean
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "NotificationQueue"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") }, mappedName = "NotificationQueue")
public class NotificationBean implements MessageListener {

	/**
	 * Default constructor.
	 */
	public NotificationBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		try {
			Object msgObj = ((ObjectMessage) message).getObject();
			if (msgObj != null) {
				User user = (User) msgObj;
				System.out.println("Customer with the following details has been updated:");
				StringBuilder sb = new StringBuilder();
				sb.append("Customer ID=");
				sb.append(user.getId());
				sb.append(", ");
				sb.append("Name=");
				sb.append(user.getUsername());
				sb.append(", ");
				sb.append("Email=");
				sb.append(user.getEmail());
				System.out.println(sb.toString());
			}
		} catch (JMSException ex) {
			//Logger.getLogger(NotificationBean.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
