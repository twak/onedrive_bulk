package org.twak;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;

import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.AttendeeType;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.EventCollectionPage;
import com.microsoft.graph.requests.EventCollectionRequestBuilder;


/**
 * From https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=3
 */
public class Authentication {

	public static String auth, oldKey;
	TokenCredentialAuthProvider authProvider;
	GraphServiceClient graphClient;

	public Authentication(String appId) {
		String[] appScopes = "User.Read,Files.ReadWrite,User.ReadBasic.All".split(",");


		// Create the auth provider
		final DeviceCodeCredential credential = new DeviceCodeCredentialBuilder()
				.clientId(appId)
				.challengeConsumer(challenge -> System.out.println(challenge.getMessage()))
				.build();

		authProvider = new TokenCredentialAuthProvider( Arrays.asList(appScopes), credential);

		// Create default logger to only log errors
		DefaultLogger logger = new DefaultLogger();
		logger.setLoggingLevel(LoggerLevel.ERROR);

		// Build a Graph client
		graphClient = GraphServiceClient.builder()
				.authenticationProvider(authProvider)
				.logger(logger)
				.buildClient();

		System.out.println("I believe authentication has been achieved " + getUserAccessToken());
	}

	public String getUserAccessToken()
	{
		try {
			URL meUrl = new URL("https://graph.microsoft.com/v1.0/me");
			return authProvider.getAuthorizationTokenAsync(meUrl).get();
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}