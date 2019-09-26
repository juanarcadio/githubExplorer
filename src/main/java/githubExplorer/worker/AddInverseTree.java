package githubExplorer.worker;

import githubExplorer.GithubClientException;
import githubExplorer.user.User;
import githubExplorer.user.UserRepo;

public class AddInverseTree {
	private static boolean torvaldsOK() {
		// Falta saber que repos tiene
		User torvalds = RunMierdas.getUserManager().getUser("torvalds");
		if (!torvalds.isVisited()) return false;
		// Falta por revisar algun repo
		for (UserRepo repo : torvalds.getRepositories()) if (!repo.isVisited())  return false;
		return true;
	}
	
	public static void main(String[] args) {
		
		RunMierdas.load("data/oro.ser");
		testDB();

		while (!torvaldsOK()) {
			System.out.println("GOES!");

			try {
				RunMierdas.explore(RunMierdas.getUserManager().getUser("torvalds"));
				
				// Muestra el camino del resultado
				if (RunMierdas.getUserManager().targetReached()) {
					String path = RunMierdas.getUserManager().getPath();
					System.out.println(path);
				}
				
				// PAUSE
				testDB();
				try {
					Thread.sleep(300*1000);
				} catch (InterruptedException ie) {
					System.out.println("OUT!");
				}
				
			} catch (GithubClientException e) {
				// SAVE
				RunMierdas.save("data/oro.ser");
				
				System.out.println("No se puede acceder al repositorio de github en estos momentos...");
				
				// PAUSE
				try {
					Thread.sleep(300*1000);
				} catch (InterruptedException ie) {
					System.out.println("OUT!");
				}
			}
		} 
	}

	private static void testDB() {
		RunMierdas.getUserManager().getAllUsers().stream().filter(n -> n.isInverso()).forEach(x -> System.out.println(x.getLogin()));
	}
}
