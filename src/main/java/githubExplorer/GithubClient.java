package githubExplorer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GithubClient implements GithubClientService {

	private static final Logger log = LogManager.getLogger(GithubClient.class.getName());

	public ArrayList<String> getReposFromUser(String user) throws GithubClientException {

		try {

			int page = 1;
			ArrayList<String> rtn = new ArrayList<String>();
			boolean pageComplete = false;
			do {
				// https://api.github.com/users/:user/repos?type=all&page=1&per_page=100

				StringBuffer request = new StringBuffer("https://api.github.com/users/");
				request.append(user);
				//request.append("/repos?type=all");
				request.append("/repos?type=owner");
				request.append("&page=" + page++);
				request.append("&per_page=" + PER_PAGE);

				URL url = new URL(request.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

				if (conn.getResponseCode() != 200) {
					log.error("getReposFromUser: Failed : HTTP error code : " + conn.getResponseCode());
					// HTTP 451 Unavailable For Legal Reasons (simulate empty query)
					if (conn.getResponseCode()==451) return rtn;
					throw new GithubClientException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				StringBuffer json = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output;
				while ((output = br.readLine()) != null) {
					json.append(output);
				}
				conn.disconnect();

				JSONParser jsonParser = new JSONParser();
				Object obj = jsonParser.parse(json.toString());

				JSONArray repoList = (JSONArray) obj;

				ArrayList<String> parcial = new ArrayList<String>();
				repoList.forEach(x -> parcial.add((String) ((JSONObject) x).get("full_name")));
				rtn.addAll(parcial);
				pageComplete = parcial.size() == PER_PAGE;

				log.debug(url.toString() + " Obtuvo " + parcial.size() + " resultados.");
			} while (pageComplete);
			return rtn;

		} catch (MalformedURLException e) {
			log.error("getReposFromUser", e);
			throw new GithubClientException(e);
		} catch (IOException e) {
			log.error("getReposFromUser", e);
			throw new GithubClientException(e);
		} catch (ParseException e) {
			log.error("getReposFromUser", e);
			throw new GithubClientException(e);
		}
	}

	public ArrayList<String> getUsersWhoForks(String userRepo, int page) throws GithubClientException {

		try {

			ArrayList<String> rtn = new ArrayList<String>();

			// https://api.github.com/repos/:user/:repo/forks?sort=stargazers&page=1&per_page=100
			StringBuffer request = new StringBuffer("https://api.github.com/repos/");
			request.append(userRepo);
			request.append("/forks?sort=stargazers");
			request.append("&page=" + page);
			request.append("&per_page=" + PER_PAGE);

			URL url = new URL(request.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

			if (conn.getResponseCode() != 200) {
				log.error("getUsersWhoForks: Failed : HTTP error code : " + conn.getResponseCode());
				// HTTP 451 Unavailable For Legal Reasons (simulate empty query)
				if (conn.getResponseCode()==451) return rtn;
				throw new GithubClientException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			StringBuffer json = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				json.append(output);
			}
			conn.disconnect();

			JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser.parse(json.toString());

			JSONArray repoList = (JSONArray) obj;

			repoList.forEach(x -> rtn.add((String) ((JSONObject) ((JSONObject) x).get("owner")).get("login")));

			log.debug(url.toString() + " Obtuvo " + rtn.size() + " resultados.");
			return rtn;

		} catch (MalformedURLException e) {
			log.error("getUsersWhoForks", e);
			throw new GithubClientException(e);
		} catch (IOException e) {
			log.error("getUsersWhoForks", e);
			throw new GithubClientException(e);
		} catch (ParseException e) {
			log.error("getUsersWhoForks", e);
			throw new GithubClientException(e);
		}
	}

	public String getParentFromFork(String userRepo) throws GithubClientException {

		try {

			// https://api.github.com/repos/:user/:repo
			StringBuffer request = new StringBuffer("https://api.github.com/repos/");
			request.append(userRepo);

			URL url = new URL(request.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

			if (conn.getResponseCode() != 200) {
				log.error("getParentFromFork: Failed : HTTP error code : " + conn.getResponseCode());
				// HTTP 451 Unavailable For Legal Reasons (simulate empty query)
				if (conn.getResponseCode()==451) return null;
				throw new GithubClientException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			StringBuffer json = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				json.append(output);
			}
			conn.disconnect();

			JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser.parse(json.toString());

			Object parent = ((JSONObject) obj).get("parent");
			// Se el user es owner del repo no tiene parent
			if (parent == null)
				return null;
			return (String) ((JSONObject) ((JSONObject) parent).get("owner")).get("login");

		} catch (MalformedURLException e) {
			log.error("getParentFromFork", e);
			throw new GithubClientException(e);
		} catch (IOException e) {
			log.error("getParentFromFork", e);
			throw new GithubClientException(e);
		} catch (ParseException e) {
			log.error("getParentFromFork", e);
			throw new GithubClientException(e);
		}
	}

}
