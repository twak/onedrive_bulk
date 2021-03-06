package org.twak;

import java.io.*;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.microsoft.aad.msal4j.DeviceCode;
import com.microsoft.aad.msal4j.DeviceCodeFlowParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.graph.models.extensions.User;

/**
 * From https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=3
 */
public class Authentication {

	public static final String TEMP_TXT = "temp.txt";
	public static final String AUTH = "auth";
	private static String applicationId;
	private final static String authority = "https://login.microsoftonline.com/common/";

	public static String auth, oldKey;

	public static void initialize(String applicationId) {
		Authentication.applicationId = applicationId;

		Properties prop = new Properties();
		try ( InputStream in = new FileInputStream( TEMP_TXT ) ) {
			prop.load( in );
		} catch ( Throwable e ) {
			e.printStackTrace();
		}
		if (prop != null && prop.containsKey( AUTH )) {
			oldKey = prop.getProperty( AUTH );
		}
	}

	public static String readPropertiesAndGetUserAccessToken() {

		// Load OAuth settings
		final Properties oAuthProperties = new Properties();
		try {
			oAuthProperties.load(Bulk.class.getResourceAsStream("/oAuth.properties"));
		} catch (IOException e) {
			System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
			return null;
		}

		final String appId = oAuthProperties.getProperty("app.id");
		final String[] appScopes = oAuthProperties.getProperty("app.scopes").split(",");

		// Get an access token
		Authentication.initialize(appId);
		return Authentication.getUserAccessToken(appScopes);
	}

	public static String getUserAccessToken(String[] scopes) {
		if (applicationId == null) {
			System.out.println("You must initialize Authentication before calling getUserAccessToken");
			return null;
		}

		try {
			User user = Graph.getUser( oldKey );
			System.out.println("In the name of: " + user.displayName);
		} catch ( Throwable th ) {
			System.out.println("failed to use saved key. Reapplying.");
			oldKey = null;
		}

		if (oldKey != null)
			return oldKey;

		Set<String> scopeSet = new HashSet<>(  );
		for (String s : scopes)
			scopeSet.add(s);

		ExecutorService pool = Executors.newFixedThreadPool(1);
		PublicClientApplication app;
		try {
			app = PublicClientApplication.builder(applicationId)
					.authority(authority)
					.executorService(pool)
					.build();
		} catch (MalformedURLException e) {
			return null;
		}

		Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
			System.out.println(deviceCode.message());
		};

		IAuthenticationResult result = app.acquireToken(
				DeviceCodeFlowParameters
						.builder(scopeSet, deviceCodeConsumer)
						.build()
		).exceptionally(ex -> {
			System.out.println("Unable to authenticate - " + ex.getMessage());
			return null;
		}).join();

		pool.shutdown();

		if (result != null) {

			try {
				Properties out = new Properties( );
				out.put( AUTH, result.accessToken() );
				out.store(new FileOutputStream(TEMP_TXT), null);
			} catch ( Throwable e ) {
				e.printStackTrace();
			}

			return result.accessToken();
		}

		return null;
	}
}