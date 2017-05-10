package chat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * A chat user. Contains the user's name and information about his messages.
 */
public class User {

	private static final boolean removeOldMessages = true;

	/**
	 * The user's name.
	 */
	private String name;

	/**
	 * The sequence number of the last message sent.
	 */
	private int sequenceNumber;

	/**
	 * Messages to be delivered.
	 */
	private Queue<Message> messages = new ArrayDeque<Message>();

	/**
	 * The user�s token.
	 */
	private String token;

	/**
	 * The expiration date of the token.
	 */
	private Date expirationDate;

	/**
	 * Creates a new user with the given name.
	 *
	 * @param name
	 *            The user's name.
	 */
	public User(String name) {
		this.name = name;
	}

	/**
	 * Sends a message to the user. The method returns a message object with the
	 * correct sequence number.
	 *
	 * @param msg
	 *            The message to sent to the user.
	 * @return The sent message with the correct sequence number.
	 */
	public Message sendMessage(Message msg) {
		authenticateUser(msg.token);
		msg.sequence = sequenceNumber++;
		messages.add(msg);
		System.out.println(String.format("%s -> %s [%d]: %s", msg.from, msg.to, msg.sequence, msg.text));
		return msg;
	}

	/**
	 * Gets all received message with a sequence number lower than the
	 * parameter. Returned messages are deleted.
	 *
	 * @param sequenceNumber
	 *            The last sequence number received by the client or 0 to fetch
	 *            all available messages.
	 * @return Returns all message with a sequence number lower than the
	 *         parameter.
	 */
	public List<Message> receiveMessages(int sequenceNumber) {
		ArrayList<Message> recvMsgs = new ArrayList<>();

		for (Message message : messages) {
			if (sequenceNumber == 0 || message.sequence > sequenceNumber) {
				recvMsgs.add(message);
			}
		}

		// Remove all message with a sequence <= the parameter. This removes all
		// messages from storage that
		// the client confirmed as received.
		if (User.removeOldMessages) {
			while (!this.messages.isEmpty()) {
				Message msg = this.messages.peek();
				if (msg.sequence <= sequenceNumber) {
					this.messages.poll();
				} else {
					break;
				}
			}
		}

		return recvMsgs;
	}

	/**
	 * Method to authenticate a user with his token.
	 * @return Returns true if the authentication was successfull and false if not
	 */
	private boolean authenticateUser(String token) {
		SimpleDateFormat sdf = new SimpleDateFormat(Service.ISO8601);
		if(this.token == token) {
		if(sdf.format(new Date()).compareTo(expirationDate.toString()) < 0) {
			return true;
		}

			// TODO: Anfrage mit token und name an /auth
		}
		else {
			//TODO: Anfrage mit token und name an /auth
		}

		return false;
	}
}
