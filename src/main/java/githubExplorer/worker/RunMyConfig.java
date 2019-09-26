package githubExplorer.worker;

public class RunMyConfig {

	public static void main(String[] args) {
		String[] myConfig = { "false", "600", "data/onlyforks.ser", "-1" };
		RunMierdas.main(myConfig);
	}
}
