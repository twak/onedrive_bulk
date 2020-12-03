package org.twak;

import com.microsoft.graph.models.extensions.User;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.util.Properties;

/**
 * Graph Tutorial
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

		Graph.doDrive( accessToken, "toemail", "" );

		Scanner input = new Scanner(System.in);

		int choice = -1;

		while (choice != 0) {
			System.out.println("Please choose one of the following options:");
			System.out.println("0. Exit");
			System.out.println("1. Display access token");
			System.out.println("2. List calendar events");

			try {
				choice = input.nextInt();
			} catch (InputMismatchException ex) {
				// Skip over non-integer input
				input.nextLine();
			}

			// Process user choice
			switch(choice) {
			case 0:
				// Exit the program
				System.out.println("Goodbye...");
				break;
			case 1:
				System.out.println("Access token: " + accessToken);

				break;
			case 2:
				// List the calendar
				break;
			default:
				System.out.println("Invalid choice");
			}
		}

		input.close();
	}
}