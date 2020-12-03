package org.twak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.requests.extensions.*;

/**
 * Graph
 */
public class Graph {

	private static IGraphServiceClient graphClient = null;
	private static SimpleAuthProvider authProvider = null;

	private static void ensureGraphClient(String accessToken) {
		if (graphClient == null) {
			// Create the auth provider
			authProvider = new SimpleAuthProvider(accessToken);

			// Create default logger to only log errors
			DefaultLogger logger = new DefaultLogger();
			logger.setLoggingLevel(LoggerLevel.ERROR);

			// Build a Graph client
			graphClient = GraphServiceClient.builder()
					.authenticationProvider(authProvider)
					.logger(logger)
					.buildClient();
		}
	}

	public static void doDrive(String accessToken, String folderName, String message) {
		ensureGraphClient(accessToken);

		// GET /me to get authenticated user
		User me = graphClient
				.me()
				.buildRequest()
				.get();


		IDriveCollectionPage list = graphClient.drives().buildRequest().get();
		for (Drive dd : list.getCurrentPage() ) {
			System.out.println("drive available: "+ dd.name + "  " + dd.driveType);
		}

//		graphClient.drive().buildRequest().get();
//		Drive d = graphClient.me().drive().buildRequest().get();
//		System.out.println("using drive "+d.name);

		DriveItem driveItem = graphClient.me().drive().root().buildRequest().get();
		IDriveItemCollectionPage driveItem2 = graphClient.me().drive().items(driveItem.id).children().buildRequest().get();

		String folderToFix = null;

		for ( DriveItem di : driveItem2.getCurrentPage() ) {

				if ( di.name.compareTo( folderName ) == 0 )
					folderToFix = di.id;

//				System.out.println( ">>>" + di.name );
			}

		if (folderToFix == null)
			throw new Error("Didn't find folder toFix!");

		driveItem2 = graphClient.me().drive().items(folderToFix).children().buildRequest().get();

		for ( DriveItem file : driveItem2.getCurrentPage() ) {

			String[] studentUsernames = file.name.substring( 0, file.name.indexOf( "." ) ).split( "_" );



			List<DriveRecipient> students = new ArrayList<>(  );

			for (String username : studentUsernames) {

				{
					DriveRecipient dr = new DriveRecipient();

					User u = graphClient.users( username+"@leeds.ac.uk" ).buildRequest().get();

					dr.email = u.userPrincipalName;
//					dr.alias = dr.email;
//					dr.objectId = u.id;
					students.add( dr );
				}
			}

			IPermissionCollectionPage iPermissionCollectionPage = graphClient.me().drive().items( file.id ).permissions().buildRequest().get();

			for (Permission p : iPermissionCollectionPage.getCurrentPage() ) {
				for (String r : p.roles)
					System.out.println(">>> " + p.grantedTo.user.displayName +" is a " + r);
			}

			graphClient.me().drive().items( file.id ).invite( true, Collections.singletonList( "read" ),
					true, message, students ).buildRequest().post();

		}

		if (driveItem2.getNextPage() != null)
			throw new Error("fixme!");




		//		ListItemCollectionPage lcp = l.items;

//		for ( ListItem li : lcp.getCurrentPage() ) {
//			System.out.println(">>> "+ li.name );
//		}
//
//		lcp.getNextPage().buildRequest().get();

//		while ()

//		return me;
	}

	public static User getUser(String accessToken) {
		ensureGraphClient(accessToken);

		// GET /me to get authenticated user
		User me = graphClient
				.me()
				.buildRequest()
				.get();

		return me;
	}
}