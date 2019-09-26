package githubExplorer;

import java.util.ArrayList;

public interface GithubClientService {
	
	public static final int PER_PAGE = 100;

	public ArrayList<String> getReposFromUser(String user) throws GithubClientException;
	public ArrayList<String> getUsersWhoForks(String userRepo, int page) throws GithubClientException;
	public String getParentFromFork(String userRepo) throws GithubClientException;

}
