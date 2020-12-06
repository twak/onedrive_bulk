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

		String accessToken = Authentication.readPropertiesAndGetUserAccessToken();

		Graph.doDrive( accessToken, "toemail", "Ignore this, tom is testing a script to share feedback files with students.", "leeds.ac.uk" );
	}
}