package org.twak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.DriveCollectionPage;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import com.microsoft.graph.requests.PermissionCollectionPage;

public class Graph {

	private  Authentication auth;

	public Graph( Authentication auth, String folderName, String message, String emailDomain ) {

		this.auth = auth;

		DriveCollectionPage list = auth.graphClient.drives().buildRequest().get();
		for (Drive dd : list.getCurrentPage())
			System.out.println("drive available: " + dd.name + "  " + dd.driveType);

		DriveItem driveItem = auth.graphClient.me().drive().root().buildRequest().get();
		DriveItemCollectionPage driveItem2 = auth.graphClient.me().drive().items(driveItem.id).children().buildRequest().get();

		String folderToFix = null;

		List<String> failedUsers = new ArrayList<>();

		for (DriveItem di : driveItem2.getCurrentPage())
			if (di.name.compareTo(folderName) == 0) {
				folderToFix = di.id;
				break;
			}

		if (folderToFix == null)
			throw new Error("Didn't find folder " + folderName + "!");

		driveItem2 = auth.graphClient.me().drive().items(folderToFix).children().buildRequest().get();

		do {

			file:
			for (DriveItem file : driveItem2.getCurrentPage()) {

				System.out.println("\n" + file.name);
				String allNames = file.name;

				if (file.name.contains(".zip"))
					allNames = file.name.substring(0, file.name.indexOf("."));

				String[] studentUsernames = allNames.split("_");

				List<DriveRecipient> students = new ArrayList<>();

				for (String username : studentUsernames) {
					DriveRecipient dr = new DriveRecipient();

					try {
						User u = auth.graphClient.users(username + "@" + emailDomain).buildRequest().get();

						dr.email = u.userPrincipalName;
						students.add(dr);
					} catch (Throwable th) {
						failedUsers.add(username);
						continue;
					}
				}

				PermissionCollectionPage iPermissionCollectionPage = auth.graphClient.me().drive().items(file.id).permissions().buildRequest().get();

				boolean skip = false;

				for (Permission p : iPermissionCollectionPage.getCurrentPage()) {

					for (String r : p.roles)
						System.out.println("  " + p.grantedTo.user.displayName + " is a " + r);


					for (String s : studentUsernames)
						if (p.grantedTo.user.displayName.contains(s)) {
							skip = true;
							System.out.println("permissions already set");
						}
				}

				if (skip)
					continue file;

				DriveItemInviteParameterSet invite = new DriveItemInviteParameterSet();
				invite.message = message;
				invite.roles = Collections.singletonList("read");
				invite.sendInvitation = true;
				invite.recipients = students;
				invite.requireSignIn = true;

				auth.graphClient.me().drive().items(file.id).invite(invite).buildRequest().post();
			}

			if (driveItem2.getNextPage() != null)
				driveItem2 = driveItem2.getNextPage().buildRequest().get();
			else
				driveItem2 = null;

		} while (driveItem2 != null);

		if (!failedUsers.isEmpty()) {
			System.out.println("failed to share files:");
			for (String s : failedUsers)
				System.out.println(s);
		}
	}
}