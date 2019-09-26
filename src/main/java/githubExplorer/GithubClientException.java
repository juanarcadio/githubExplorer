package githubExplorer;

public class GithubClientException extends Exception {

	private static final long serialVersionUID = 7498168141381473538L;

	public GithubClientException(Exception e) {
		super(e);
	}

	public GithubClientException(String string) {
		super(string);
	}
	
}
