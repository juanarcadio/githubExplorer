package githubExplorer.user;

import java.io.Serializable;
import java.util.ArrayList;

public class UserRepo implements Serializable {

	private static final long serialVersionUID = 787245710809407590L;
	
	private String fullName;
	private User parent;
	private ArrayList<User> forks;
	private boolean visited;
	private int page;
	
	public UserRepo(String fullName, User pointer) {
		this.fullName = fullName;
		visited = false;
		forks = new ArrayList<User>();
		parent = null;
		page = 1;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public User getParent() {
		return parent;
	}
	public void setParent(User parent) {
		this.parent = parent;
	}
	public ArrayList<User> getForks() {
		return forks;
	}
	public void setForks(ArrayList<User> forks) {
		this.forks = forks;
	}
	public void addFork(User fork) {
		this.forks.add(fork);
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getPage() {
		return page;
	}

	public void incPage() {
		this.page ++;
	}
}
