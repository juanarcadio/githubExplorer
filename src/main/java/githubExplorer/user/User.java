package githubExplorer.user;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User implements Serializable {

	private static final long serialVersionUID = -6881314304899969774L;
	
	private static final Logger log = LogManager.getLogger(User.class.getName());
	
	private String login;
	private boolean visited;
	private int level;
	private ArrayList<UserRepo> repositories;
	private boolean inverso;
	
	// Obtener el resultado
	private User parent;
	private String relation;
	
	public User(String login, User parent, String relation, boolean inverso) {
		this.login = login;
		this.visited = false;
		this.repositories = null;
		this.parent = parent;
		this.relation = relation;
		this.level = (parent==null?0:parent.level+1);
		this.inverso = inverso;
	}
	
	public User(String login, boolean inverso) {
		this(login, null, null, inverso);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<UserRepo> getRepositories() {
		return repositories;
	}

	public void setRepositories(ArrayList<UserRepo> repositories) {
		this.repositories = repositories;
		this.setVisited(true);
		log.debug("Repositories complete for user: "+login);
	}
	
	public String getRootPath() {
		if (level==0) return "";
		if (inverso) return level+"--. "+parent.login+" works in "+relation+"\n"+parent.getRootPath();
		else return parent.getRootPath()+level+". "+parent.login+" works in "+relation+"\n";
	}
	
	public boolean isInverso() {
		return inverso;
	}
}
