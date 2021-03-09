package gu;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * This class represents a single user.
 * A User contains a username, profilepic, and a status
 * 
 * @author
 *
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private ImageIcon profilepic;
	private int status;

	/**
	 * Constructs a User-object
	 * 
	 * @param username the username
	 * @param profilepic the profilepicture
	 */
	public User(String username, ImageIcon profilepic) {
		this.username=username;
		this.profilepic=profilepic;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the profilepicture
	 */
	public ImageIcon getProfilepic() {
		return profilepic;
	}
	/**
	 * @param status the status of the users connection
	 */
	public void setStatus(int status) {
		this.status=status;
	}
	/**
	 * @return the status of the users connection
	 */
	public int getStatus() {
		return status;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return username.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(User u) {
		return username.equals(u.getUsername());
	}

}
