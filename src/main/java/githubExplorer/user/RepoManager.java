package githubExplorer.user;

import java.io.Serializable;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Sirve para controlar por que repos he pasado
public class RepoManager implements Serializable  {

	private static final Logger log = LogManager.getLogger(RepoManager.class.getName());

	private static final long serialVersionUID = 8829416534702666370L;
	private HashSet<String> repositories;
	
	public RepoManager() {
		repositories = new HashSet<String>();
	}

	public boolean contains(String fullName) {
		return repositories.contains(fullName);
	}
	
	public boolean add(String fullName) {
		return repositories.add(fullName);
	}
	
	public int size() {
		return repositories.size();
	}
}
