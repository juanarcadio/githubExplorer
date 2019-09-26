package githubExplorer.worker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import githubExplorer.GithubClient;
import githubExplorer.GithubClientException;
import githubExplorer.GithubClientService;
import githubExplorer.user.RepoManager;
import githubExplorer.user.User;
import githubExplorer.user.UserManager;
import githubExplorer.user.UserManager.NextNotFoundException;
import githubExplorer.user.UserRepo;

public class RunMierdas {

	private static final Logger log = LogManager.getLogger(RunMierdas.class.getName());

	private static String src = "loowid";
	private static String dst = "torvalds";
	
	private static UserManager um;
	private static RepoManager repos;
	private static GithubClientService githubClient;

	public static GithubClientService getGithubClientService() {
		
		try {
			Thread.sleep(7*1000);
		} catch (InterruptedException e1) {
			log.error("nothing important "+e1);
		}
		
		if (githubClient == null) githubClient = new GithubClient();
		return githubClient;
	}

	public static void setGithubClientService(GithubClientService githubClient) {
		RunMierdas.githubClient = githubClient;
	}

	public static UserManager getUserManager() {
		return um;
	}
	
	public static void setUserManager(UserManager um) {
		RunMierdas.um = um;
	}
	
	public static void reset() {
		githubClient = null;
		repos = null;
		um = null;
		src = "loowid";
		dst = "torvalds";
	}
	
	public static void setDst(String dst) {
		RunMierdas.dst = dst;
	}

	public static void main(String[] args) {
		
		if (args==null || args.length!=4) {
			log.error(
				"args[0] : empty: true empieza de cero, false continua  \n"+
				"args[1] : seconds sleeping when github limited         \n"+
				"args[2] : filename para carga y almacenamiento         \n"+
				"args[3] : time of rerrunes -1: infinito; 0:disabled    \n");
			return;		
		}
		
		// INIT or LOAD FROM DISK 
		if (um==null) {
			if (Boolean.parseBoolean(args[0])) {
				um = new UserManager(src, dst);
				repos = new RepoManager();
				log.info("New empty user & repo manager");
			} else {
				load(args[2]);
				log.info("User & repo manager loaded");
			}
		}
		
		// EXPLORATION IN GITHUB
		try {
			try {
				while (!um.targetReached())
					explore(um.getNext());

				// Muestra el camino del resultado
				String path = um.getPath();
				System.out.println(path);
				
				// SAVE
				if (args[2]!=null) save(args[2]);
				
			// FIN DEL RECORRIDO
			} catch (NextNotFoundException e) {
				System.out.println("Path not found between " + src + " and " + dst);
				// SAVE
				if (args[2]!=null) save(args[2]);
			}
		} catch (GithubClientException e) {
			// SAVE
			if (args[2]!=null) save(args[2]);
			
			log.error("No se puede acceder al repositorio de github en estos momentos...");

			int rerrunes = Integer.parseInt(args[3]);
			
			// NO MAS INTENTOS
			if (rerrunes==0 || rerrunes==1) {
				return;
			}
			
			// PAUSE
			try {
				Thread.sleep(Integer.parseInt(args[1])*1000);
			} catch (InterruptedException ie) {
				log.error("Interrumpida mi siesta "+ie);
				return;
			}
			
			// SE VUELVE A LANZAR
			String[] nuevosArgs = { "false", args[1], args[2], String.valueOf(((rerrunes==-1)?rerrunes:rerrunes-1)) };
			main(nuevosArgs);
		}
	}

	public static void save(String fileName) {

		// Serialization
		try {
			// Saving of object in a file
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// Method for serialization of object
			out.writeObject(um);
			out.writeObject(repos);

			out.close();
			file.close();

			log.info("Users and repos has been serialized");
			log.info("Usuarios encontrados: "+um.getAllUsers().size());
			log.info("Repos recorridos: "+repos.size());
		}

		catch (IOException e) {
			log.error("IOException is caught", e);
		}
	}

	public static void load(String fileName) {

		// Deserialization
		try {
			// Reading the object from a file
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			um = (UserManager) in.readObject();
			repos = (RepoManager) in.readObject();
			// initRepos();

			in.close();
			file.close();

			log.info("Users and repos has been deserialized ");

		}

		catch (IOException e) {
			if (e instanceof FileNotFoundException) {
				log.info("empty database as expected");
				um = new UserManager(src, dst);
				repos = new RepoManager();
			} else {
				log.error("IOException is caught:"+ e);
			}
		}

		catch (ClassNotFoundException cnfe) {
			log.error("ClassNotFoundException is caught", cnfe);
		}

	}
	
	// Marcar los visitados y no marcados, usado para migracion de version
	private static void initRepos() {	
		repos = new RepoManager();
		um.getAllUsers().stream().filter(n -> n.getRepositories()!=null).forEach(u -> u.getRepositories().stream().filter(r -> r.isVisited()).forEach(ur -> repos.add(ur.getFullName())));
	}

	static void explore(User currentUser) throws GithubClientException {
		System.out.println("· explore User: "+currentUser.getLogin()+" ("+currentUser.getLevel()+")");
		if (!currentUser.isVisited()) {
			log.debug("Exploring repos from "+currentUser.getLogin());
			ArrayList<String> repos = getGithubClientService().getReposFromUser(currentUser.getLogin());
			ArrayList<UserRepo> userRepo = new ArrayList<UserRepo>();
			repos.forEach(repo -> userRepo.add(new UserRepo(repo, currentUser)));
			currentUser.setRepositories(userRepo);
		}

		for (UserRepo repo : currentUser.getRepositories()) {
			if (!um.targetReached() && !repo.isVisited()) {
				System.out.println("-- explore UserRepo: "+repo.getFullName());

				// Global check, pueden aparecer repetidos los mismos repos con distintas afiliaciones
				if (!repos.contains(repo.getFullName())) {
					log.debug("Exploring forks from "+repo.getFullName());
					boolean pageFullOfData = true;
					do {
						ArrayList<String> forkers = getGithubClientService().getUsersWhoForks(repo.getFullName(), repo.getPage());
						for (String user : forkers) {
							User uwf = um.addUser(user, currentUser, "(" + repo.getFullName() + ") forks by " + user);
							repo.addFork(uwf);
						}
						repo.incPage();
						pageFullOfData = forkers.size() == GithubClientService.PER_PAGE;
					} while (pageFullOfData);
					log.debug("Exploring parent from "+repo.getFullName());
					String parent = getGithubClientService().getParentFromFork(repo.getFullName());
					if (parent != null) {
						User userParent = um.addUser(parent, currentUser, "(" + repo.getFullName() + ") forked from " + parent);
						repo.setParent(userParent);
					}
					repos.add(repo.getFullName());
				}
				repo.setVisited(true);
				log.debug("Repo ("+repo.getFullName()+") completed");
			}

		}
	}
}
