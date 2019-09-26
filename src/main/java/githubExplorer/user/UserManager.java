package githubExplorer.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class UserManager implements Serializable {

	private static final long serialVersionUID = 2744589509727275007L;
	
	private HashMap<String, User> usersByLogin;
	private HashMap<Integer, ArrayList<User>> usersByLevel;
	private int level, node;
	private User nexus;
	
	public class NextNotFoundException extends Exception {
		private static final long serialVersionUID = 5349583612947069827L;
	}
	
	public UserManager(String src, String dst) {
		level = 0; node = 0;
		
		// Usuarios raiz del arbol
		User root1 = new User(src, false);
		User root2 = new User(dst, true);

		usersByLogin = new HashMap<String, User>();
		usersByLevel = new HashMap<Integer, ArrayList<User>>();
		addUser(root1);
		addUser(root2);
	}
	
	public User addUser(String login, User parent, String relation) {
		User nuevo = new User(login, parent, relation, parent.isInverso());
		addUser(nuevo);
		return nuevo;
	}
	
	public boolean addUser(User insert) {
		if (usersByLogin.containsKey(insert.getLogin())) {
			if (insert.isInverso() != usersByLogin.get(insert.getLogin()).isInverso()) {
				// Hemos llegado al mismo user recorriendo el arbol en dos sentidos distintos
				nexus = insert;
				System.out.println("NEXO: "+nexus.getLogin());
			}
			return false;

		}
		usersByLogin.put(insert.getLogin(), insert);
		ArrayList<User> levelList = usersByLevel.get(insert.getLevel());
		if (levelList==null) {
			levelList = new ArrayList<User>();
			usersByLevel.put(insert.getLevel(), levelList);
		}
		return levelList.add(insert);
	}
	
	public User getUser(String login) {
		return usersByLogin.get(login);
	}

	public boolean targetReached() {
		return nexus!=null;
	}
	
	public String getPath() {
		StringBuffer sb = new StringBuffer();
		if (nexus.isInverso()) {
			sb.append(usersByLogin.get(nexus.getLogin()).getRootPath());
			sb.append(nexus.getRootPath());
		} else {
			sb.append(nexus.getRootPath());
			sb.append(usersByLogin.get(nexus.getLogin()).getRootPath());
		}
		return sb.toString();
	}

	public User getNext() throws NextNotFoundException {
		ArrayList<User> currentList = usersByLevel.get(this.level);
		User lastVisited = currentList.get(node);
		// Si no se completo la exploración del nodo sigue por donde iba
		if (!lastVisited.isVisited() || lastVisited.getRepositories()==null) return lastVisited;
		for (UserRepo repo : lastVisited.getRepositories()) if (!repo.isVisited()) return lastVisited;
		// Si esta completo saltamos al siguiente nodo
		if (currentList.size() > ++node) return currentList.get(node);
		currentList = usersByLevel.get(++level);
		// Hemos recorrido todos los nodos
		if (currentList == null) throw new NextNotFoundException();
		
		return currentList.get(node=0);
	}
	
	public Collection<User> getAllUsers() {
		return usersByLogin.values();
	}

}
