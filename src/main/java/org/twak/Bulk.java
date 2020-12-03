package org.twak;

import com.microsoft.graph.models.extensions.User;

import java.io.IOException;
import java.util.Properties;

/**
 * Bulk permission setting thing for OneDrive
 *
 * You should have a file src/resources/oAuth.properties with the following contents.
 * app.id={from dashboard}
 * app.scopes=User.Read,Files.ReadWrite,User.ReadBasic.All
 *
 */
public class Bulk {
	public static void main(String[] args) {
		System.out.println("Java Graph Tutorial");
		System.out.println();

		// Load OAuth settings
		final Properties oAuthProperties = new Properties();
		try {
			oAuthProperties.load(Bulk.class.getResourceAsStream("/oAuth.properties"));
		} catch (IOException e) {
			System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
			return;
		}

		final String appId = oAuthProperties.getProperty("app.id");
		final String[] appScopes = oAuthProperties.getProperty("app.scopes").split(",");

		// Get an access token
		Authentication.initialize(appId);
		final String accessToken = Authentication.getUserAccessToken(appScopes);

		User user = Graph.getUser(accessToken);
		System.out.println("Working in the name of: " + user.displayName);

		Graph.doDrive( accessToken, "toemail", "Ignore this, tom is testing a script to share feedback files with students." );
	}
}