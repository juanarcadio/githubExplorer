package githubExplorer.worker;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;

import githubExplorer.GithubClientException;
import githubExplorer.GithubClientService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class RunMierdasTest extends TestCase {
	
	private static final Logger log = LogManager.getLogger(RunMierdasTest.class.getName());

	
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public RunMierdasTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(RunMierdasTest.class);
		return suite;
	}

	public void testNoTengoAmigos() {
		RunMierdas.reset();
		
		// Mock del cliente de github
		GithubClientService mockGithubClientService = Mockito.mock(GithubClientService.class);
		try {
			// No tengo amigos
			Mockito.when(mockGithubClientService.getParentFromFork(Mockito.anyString())).thenReturn(null);
			Mockito.when(mockGithubClientService.getReposFromUser(Mockito.anyString())).thenReturn(new ArrayList<String>());
			Mockito.when(mockGithubClientService.getUsersWhoForks(Mockito.anyString())).thenReturn(new ArrayList<String>());
		} catch (GithubClientException e) {
			assertFalse(true);
			return;
		}

		RunMierdas.setGithubClientService(mockGithubClientService);
		
		String[] args = { "true", "0", "testfile.ser", "0" };
		RunMierdas.main(args);

		assertFalse(RunMierdas.getUserManager().targetReached());
	}

	public void testPocosAmigos() {
		RunMierdas.reset();

		// Mock del cliente de github
		GithubClientService mockGithubClientService = Mockito.mock(GithubClientService.class);
		try {
			String users[] = { "juan", "alex", "mero", "drchuck" };
			ArrayList<String> pocos = new ArrayList<String>();
			for (String u : users) pocos.add(u);
			
			ArrayList<String> loowidRepos = new ArrayList<String>();
			loowidRepos.add("loowid/loowid");
			loowidRepos.add("loowid/coolproject");
			
			ArrayList<String> meroRepos = new ArrayList<String>();
			meroRepos.add("mero/loowid");
			meroRepos.add("mero/sakai");
			
			Mockito.when(mockGithubClientService.getParentFromFork(Mockito.anyString())).thenReturn(null);
			Mockito.when(mockGithubClientService.getReposFromUser(Mockito.anyString())).thenReturn(new ArrayList<String>());
			Mockito.when(mockGithubClientService.getUsersWhoForks(Mockito.anyString())).thenReturn(pocos);

			Mockito.when(mockGithubClientService.getParentFromFork("mero/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getReposFromUser("loowid")).thenReturn(loowidRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("mero")).thenReturn(meroRepos);
		} catch (GithubClientException e) {
			assertFalse(true);
		}

		RunMierdas.setGithubClientService(mockGithubClientService);
		
		String[] args = { "true", "0", "testfile.ser", "0" };
		RunMierdas.main(args);

		assertFalse(RunMierdas.getUserManager().targetReached());
	}

	public void testPocosAmigosPeroBuenos() {
		RunMierdas.reset();

		// Mock del cliente de github
		GithubClientService mockGithubClientService = Mockito.mock(GithubClientService.class);
		try {
			String users[] = { "juan", "alex", "mero", "torvalds" };
			ArrayList<String> buenos = new ArrayList<String>();
			for (String u : users) buenos.add(u);
			
			ArrayList<String> loowidRepos = new ArrayList<String>();
			loowidRepos.add("loowid/loowid");
			loowidRepos.add("loowid/coolproject");
			
			ArrayList<String> meroRepos = new ArrayList<String>();
			meroRepos.add("mero/loowid");
			meroRepos.add("mero/sakai");
			
			Mockito.when(mockGithubClientService.getParentFromFork(Mockito.anyString())).thenReturn(null);
			Mockito.when(mockGithubClientService.getReposFromUser(Mockito.anyString())).thenReturn(new ArrayList<String>());
			Mockito.when(mockGithubClientService.getUsersWhoForks(Mockito.anyString())).thenReturn(buenos);

			Mockito.when(mockGithubClientService.getParentFromFork("mero/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getReposFromUser("loowid")).thenReturn(loowidRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("mero")).thenReturn(meroRepos);

		} catch (GithubClientException e) {
			assertFalse(true);
		}

		RunMierdas.setGithubClientService(mockGithubClientService);
		String[] args = { "true", "0", "file.ser", "0" };
		RunMierdas.main(args);

		assertTrue(RunMierdas.getUserManager().targetReached());
	}
	
	public void testMasCompleto() {
		RunMierdas.reset();

		// Mock del cliente de github
		GithubClientService mockGithubClientService = Mockito.mock(GithubClientService.class);
		try {
			String users[] = { "juan", "alex", "mero", "drchuck", "afish", "torvalds" };
			ArrayList<String> buenos = new ArrayList<String>();
			for (String u : users) buenos.add(u);
			
			ArrayList<String> loowidRepos = new ArrayList<String>();
			loowidRepos.add("loowid/loowid");
			loowidRepos.add("loowid/coolproject");
			
			ArrayList<String> juanRepos = new ArrayList<String>();
			juanRepos.add("juan/loowid");
			juanRepos.add("juan/umusync");
			juanRepos.add("juan/coolproject");
			
			ArrayList<String>alexRepos = new ArrayList<String>();
			alexRepos.add("alex/loowid");
			alexRepos.add("alex/coolproject");
			alexRepos.add("alex/sakai");
			
			ArrayList<String> meroRepos = new ArrayList<String>();
			meroRepos.add("mero/loowid");
			meroRepos.add("mero/sakai");
			meroRepos.add("mero/umusync");
			
			ArrayList<String> drchuckRepos = new ArrayList<String>();
			drchuckRepos.add("drchuck/linux");
			drchuckRepos.add("drchuck/clog");
			
			ArrayList<String> afishRepos = new ArrayList<String>();
			afishRepos.add("afish/clog");
			afishRepos.add("afish/sakai");
			
			ArrayList<String> torvaldsRepos = new ArrayList<String>();
			torvaldsRepos.add("torvalds/linux");
			
			
			Mockito.when(mockGithubClientService.getParentFromFork(Mockito.anyString())).thenReturn(null);
			Mockito.when(mockGithubClientService.getReposFromUser(Mockito.anyString())).thenReturn(new ArrayList<String>());
			Mockito.when(mockGithubClientService.getUsersWhoForks(Mockito.anyString())).thenReturn(new ArrayList<String>());
			
			Mockito.when(mockGithubClientService.getParentFromFork("loowid/coolproject")).thenReturn("alex");
			Mockito.when(mockGithubClientService.getParentFromFork("juan/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("juan/coolproject")).thenReturn("alex");
			Mockito.when(mockGithubClientService.getParentFromFork("alex/sakai")).thenReturn("mero");
			Mockito.when(mockGithubClientService.getParentFromFork("alex/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("mero/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("mero/umusync")).thenReturn("juan");
			Mockito.when(mockGithubClientService.getParentFromFork("drchuck/clog")).thenReturn("afish");
			Mockito.when(mockGithubClientService.getParentFromFork("drchuck/linux")).thenReturn("torvalds");
			Mockito.when(mockGithubClientService.getParentFromFork("afish/sakai")).thenReturn("mero");
			
			Mockito.when(mockGithubClientService.getReposFromUser("loowid")).thenReturn(loowidRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("juan")).thenReturn(juanRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("alex")).thenReturn(alexRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("mero")).thenReturn(meroRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("drchuck")).thenReturn(drchuckRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("afish")).thenReturn(afishRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("torvalds")).thenReturn(torvaldsRepos);
			
			ArrayList<String> loowidUsers = new ArrayList<String>();
			loowidUsers.add("alex");
			loowidUsers.add("juan");
			loowidUsers.add("mero");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("loowid/loowid")).thenReturn(loowidUsers);
			
			ArrayList<String> coolUsers = new ArrayList<String>();
			coolUsers.add("juan");
			coolUsers.add("loowid");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("alex/coolproject")).thenReturn(coolUsers);

			ArrayList<String> umusyncUsers = new ArrayList<String>();
			umusyncUsers.add("mero");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("juan/umusync")).thenReturn(umusyncUsers);
			
			ArrayList<String> sakaiusers = new ArrayList<String>();
			sakaiusers.add("afish");
			sakaiusers.add("alex");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("mero/sakai")).thenReturn(sakaiusers);
			
			ArrayList<String> clogUsers = new ArrayList<String>();
			clogUsers.add("drchuck");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("afish/clog")).thenReturn(clogUsers);
			
			ArrayList<String> linuxUsers = new ArrayList<String>();
			linuxUsers.add("drchuck");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("torvalds/linux")).thenReturn(linuxUsers);

		} catch (GithubClientException e) {
			assertFalse(true);
			return;
		}

		RunMierdas.setGithubClientService(mockGithubClientService);
		String[] args = { "true", "0", "testfile.ser", "0" };
		RunMierdas.main(args);

		assertTrue(RunMierdas.getUserManager().targetReached());
	}
	
	public void testGithubTroublesome() {
		RunMierdas.reset();

		// Mock del cliente de github
		GithubClientService mockGithubClientService = Mockito.mock(GithubClientService.class);
		
		// Proxy del cliente que falla 1 de cada 10
		GithubClientService mockGithubClientServiceProxy = new GithubClientService() {
			
			private int peticiones = 0;
			
			@Override
			public ArrayList<String> getUsersWhoForks(String userRepo) throws GithubClientException {
				if (++peticiones%10==0) {
					log.debug(peticiones+"... getUsersWhoForks("+userRepo+") TIMEOUT");
					throw new GithubClientException("Has superado el rato de peticiones por hora");
				}				log.debug(peticiones+") getUsersWhoForks("+userRepo+")");
				return mockGithubClientService.getUsersWhoForks(userRepo);
			}
			
			@Override
			public ArrayList<String> getReposFromUser(String user) throws GithubClientException {
				if (++peticiones%10==0) {
					log.debug(peticiones+"... getReposFromUser("+user+") TIMEOUT");
					throw new GithubClientException("Has superado el rato de peticiones por hora");
				}				log.debug(peticiones+") getReposFromUser("+user+")");
				return mockGithubClientService.getReposFromUser(user);
			}
			
			@Override
			public String getParentFromFork(String userRepo) throws GithubClientException {
				if (++peticiones%10==0) {
					log.debug(peticiones+"... getParentFromFork("+userRepo+") TIMEOUT");
					throw new GithubClientException("Has superado el rato de peticiones por hora");
				}
				log.debug(peticiones+") getParentFromFork("+userRepo+")");
				return mockGithubClientService.getParentFromFork(userRepo);
			}
		};
		
		try {
			String users[] = { "juan", "alex", "mero", "drchuck", "afish", "torvalds" };
			ArrayList<String> buenos = new ArrayList<String>();
			for (String u : users) buenos.add(u);
			
			ArrayList<String> loowidRepos = new ArrayList<String>();
			loowidRepos.add("loowid/loowid");
			loowidRepos.add("loowid/coolproject");
			
			ArrayList<String> juanRepos = new ArrayList<String>();
			juanRepos.add("juan/loowid");
			juanRepos.add("juan/umusync");
			juanRepos.add("juan/coolproject");
			
			ArrayList<String>alexRepos = new ArrayList<String>();
			alexRepos.add("alex/loowid");
			alexRepos.add("alex/coolproject");
			alexRepos.add("alex/sakai");
			
			ArrayList<String> meroRepos = new ArrayList<String>();
			meroRepos.add("mero/loowid");
			meroRepos.add("mero/sakai");
			meroRepos.add("mero/umusync");
			
			ArrayList<String> drchuckRepos = new ArrayList<String>();
			drchuckRepos.add("drchuck/linux");
			drchuckRepos.add("drchuck/clog");
			
			ArrayList<String> afishRepos = new ArrayList<String>();
			afishRepos.add("afish/clog");
			afishRepos.add("afish/sakai");
			
			ArrayList<String> torvaldsRepos = new ArrayList<String>();
			torvaldsRepos.add("torvalds/linux");
			
			
			Mockito.when(mockGithubClientService.getParentFromFork(Mockito.anyString())).thenReturn(null);
			Mockito.when(mockGithubClientService.getReposFromUser(Mockito.anyString())).thenReturn(new ArrayList<String>());
			Mockito.when(mockGithubClientService.getUsersWhoForks(Mockito.anyString())).thenReturn(new ArrayList<String>());
			
			Mockito.when(mockGithubClientService.getParentFromFork("loowid/coolproject")).thenReturn("alex");
			Mockito.when(mockGithubClientService.getParentFromFork("juan/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("juan/coolproject")).thenReturn("alex");
			Mockito.when(mockGithubClientService.getParentFromFork("alex/sakai")).thenReturn("mero");
			Mockito.when(mockGithubClientService.getParentFromFork("alex/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("mero/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("mero/umusync")).thenReturn("juan");
			Mockito.when(mockGithubClientService.getParentFromFork("drchuck/clog")).thenReturn("afish");
			Mockito.when(mockGithubClientService.getParentFromFork("drchuck/linux")).thenReturn("torvalds");
			Mockito.when(mockGithubClientService.getParentFromFork("afish/sakai")).thenReturn("mero");
			
			Mockito.when(mockGithubClientService.getReposFromUser("loowid")).thenReturn(loowidRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("juan")).thenReturn(juanRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("alex")).thenReturn(alexRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("mero")).thenReturn(meroRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("drchuck")).thenReturn(drchuckRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("afish")).thenReturn(afishRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("torvalds")).thenReturn(torvaldsRepos);
			
			ArrayList<String> loowidUsers = new ArrayList<String>();
			loowidUsers.add("alex");
			loowidUsers.add("juan");
			loowidUsers.add("mero");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("loowid/loowid")).thenReturn(loowidUsers);
			
			ArrayList<String> coolUsers = new ArrayList<String>();
			coolUsers.add("juan");
			coolUsers.add("loowid");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("alex/coolproject")).thenReturn(coolUsers);

			ArrayList<String> umusyncUsers = new ArrayList<String>();
			umusyncUsers.add("mero");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("juan/umusync")).thenReturn(umusyncUsers);
			
			ArrayList<String> sakaiusers = new ArrayList<String>();
			sakaiusers.add("afish");
			sakaiusers.add("alex");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("mero/sakai")).thenReturn(sakaiusers);
			
			ArrayList<String> clogUsers = new ArrayList<String>();
			clogUsers.add("drchuck");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("afish/clog")).thenReturn(clogUsers);
			
			ArrayList<String> linuxUsers = new ArrayList<String>();
			linuxUsers.add("drchuck");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("torvalds/linux")).thenReturn(linuxUsers);

		} catch (GithubClientException e) {
			assertFalse(true);
			return;
		}

		RunMierdas.setGithubClientService(mockGithubClientServiceProxy);
		String[] args = { "true", "0", "testfile.ser", "-1" };
		RunMierdas.main(args);

		assertTrue(RunMierdas.getUserManager().targetReached());
	}
	
	public void testGithubStopSaveLoadContinue() {
		RunMierdas.reset();

		// Mock del cliente de github
		GithubClientService mockGithubClientService = Mockito.mock(GithubClientService.class);
		
		// Proxy del cliente que falla 1 de cada 10 peticiones
		GithubClientService mockGithubClientServiceProxy = new GithubClientService() {
			
			private int peticiones = 0;
			
			@Override
			public ArrayList<String> getUsersWhoForks(String userRepo) throws GithubClientException {
				if (++peticiones%10==0) {
					log.debug(peticiones+"... getUsersWhoForks("+userRepo+") TIMEOUT");
					throw new GithubClientException("Has superado el rato de peticiones por hora");
				}				
				log.debug(peticiones+") getUsersWhoForks("+userRepo+")");
				return mockGithubClientService.getUsersWhoForks(userRepo);
			}
			
			@Override
			public ArrayList<String> getReposFromUser(String user) throws GithubClientException {
				if (++peticiones%10==0) {
					log.debug(peticiones+"... getReposFromUser("+user+") TIMEOUT");
					throw new GithubClientException("Has superado el rato de peticiones por hora");
				}				
				log.debug(peticiones+") getReposFromUser("+user+")");
				return mockGithubClientService.getReposFromUser(user);
			}
			
			@Override
			public String getParentFromFork(String userRepo) throws GithubClientException {
				if (++peticiones%10==0) {
					log.debug(peticiones+"... getParentFromFork("+userRepo+") TIMEOUT");
					throw new GithubClientException("Has superado el rato de peticiones por hora");
				}
				log.debug(peticiones+") getParentFromFork("+userRepo+")");
				return mockGithubClientService.getParentFromFork(userRepo);
			}
		};
		
		try {
			String users[] = { "juan", "alex", "mero", "drchuck", "afish", "torvalds" };
			ArrayList<String> buenos = new ArrayList<String>();
			for (String u : users) buenos.add(u);
			
			ArrayList<String> loowidRepos = new ArrayList<String>();
			loowidRepos.add("loowid/loowid");
			loowidRepos.add("loowid/coolproject");
			
			ArrayList<String> juanRepos = new ArrayList<String>();
			juanRepos.add("juan/loowid");
			juanRepos.add("juan/umusync");
			juanRepos.add("juan/coolproject");
			
			ArrayList<String>alexRepos = new ArrayList<String>();
			alexRepos.add("alex/loowid");
			alexRepos.add("alex/coolproject");
			alexRepos.add("alex/sakai");
			
			ArrayList<String> meroRepos = new ArrayList<String>();
			meroRepos.add("mero/loowid");
			meroRepos.add("mero/sakai");
			meroRepos.add("mero/umusync");
			
			ArrayList<String> drchuckRepos = new ArrayList<String>();
			drchuckRepos.add("drchuck/linux");
			drchuckRepos.add("drchuck/clog");
			
			ArrayList<String> afishRepos = new ArrayList<String>();
			afishRepos.add("afish/clog");
			afishRepos.add("afish/sakai");
			
			ArrayList<String> torvaldsRepos = new ArrayList<String>();
			torvaldsRepos.add("torvalds/linux");
			
			
			Mockito.when(mockGithubClientService.getParentFromFork(Mockito.anyString())).thenReturn(null);
			Mockito.when(mockGithubClientService.getReposFromUser(Mockito.anyString())).thenReturn(new ArrayList<String>());
			Mockito.when(mockGithubClientService.getUsersWhoForks(Mockito.anyString())).thenReturn(new ArrayList<String>());
			
			Mockito.when(mockGithubClientService.getParentFromFork("loowid/coolproject")).thenReturn("alex");
			Mockito.when(mockGithubClientService.getParentFromFork("juan/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("juan/coolproject")).thenReturn("alex");
			Mockito.when(mockGithubClientService.getParentFromFork("alex/sakai")).thenReturn("mero");
			Mockito.when(mockGithubClientService.getParentFromFork("alex/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("mero/loowid")).thenReturn("loowid");
			Mockito.when(mockGithubClientService.getParentFromFork("mero/umusync")).thenReturn("juan");
			Mockito.when(mockGithubClientService.getParentFromFork("drchuck/clog")).thenReturn("afish");
			Mockito.when(mockGithubClientService.getParentFromFork("drchuck/linux")).thenReturn("torvalds");
			Mockito.when(mockGithubClientService.getParentFromFork("afish/sakai")).thenReturn("mero");
			
			Mockito.when(mockGithubClientService.getReposFromUser("loowid")).thenReturn(loowidRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("juan")).thenReturn(juanRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("alex")).thenReturn(alexRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("mero")).thenReturn(meroRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("drchuck")).thenReturn(drchuckRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("afish")).thenReturn(afishRepos);
			Mockito.when(mockGithubClientService.getReposFromUser("torvalds")).thenReturn(torvaldsRepos);
			
			ArrayList<String> loowidUsers = new ArrayList<String>();
			loowidUsers.add("alex");
			loowidUsers.add("juan");
			loowidUsers.add("mero");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("loowid/loowid")).thenReturn(loowidUsers);
			
			ArrayList<String> coolUsers = new ArrayList<String>();
			coolUsers.add("juan");
			coolUsers.add("loowid");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("alex/coolproject")).thenReturn(coolUsers);

			ArrayList<String> umusyncUsers = new ArrayList<String>();
			umusyncUsers.add("mero");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("juan/umusync")).thenReturn(umusyncUsers);
			
			ArrayList<String> sakaiusers = new ArrayList<String>();
			sakaiusers.add("afish");
			sakaiusers.add("alex");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("mero/sakai")).thenReturn(sakaiusers);
			
			ArrayList<String> clogUsers = new ArrayList<String>();
			clogUsers.add("drchuck");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("afish/clog")).thenReturn(clogUsers);
			
			ArrayList<String> linuxUsers = new ArrayList<String>();
			linuxUsers.add("drchuck");
			
			Mockito.when(mockGithubClientService.getUsersWhoForks("torvalds/linux")).thenReturn(linuxUsers);

		} catch (GithubClientException e) {
			assertFalse(true);
			return;
		}

		RunMierdas.setGithubClientService(mockGithubClientServiceProxy);
		String[] argsFirst = { "true", "0", "testfile.ser", "0" };
		String[] argsNext = { "false", "0", "testfile.ser", "0" };

		RunMierdas.main(argsFirst);
		do {
			// borra memoria
			RunMierdas.setUserManager(null);
			log.debug("Cleaning memory...");
			// rejecuta el programa
			RunMierdas.main(argsNext);
			// hasta que lo encuentres
		} while (!RunMierdas.getUserManager().targetReached());
		
		assertTrue(RunMierdas.getUserManager().targetReached());
	}
	
	public void testRealApi() {
		RunMierdas.reset();
		RunMierdas.setDst("Gonzalo2310");
		String[] myConfig = { "false", "600", "data/testGonzalo.ser", "-1" };
		RunMierdas.main(myConfig);
		assertTrue(RunMierdas.getUserManager().targetReached());
	}
}
