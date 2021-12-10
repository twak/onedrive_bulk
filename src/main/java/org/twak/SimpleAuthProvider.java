package org.twak;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * SimpleAuthProvider
 */
public class SimpleAuthProvider implements IAuthenticationProvider {

	private String accessToken = null;

	public SimpleAuthProvider(String accessToken) {
		this.accessToken = accessToken;
	}

	public void authenticateRequest(IHttpRequest request) {
		// Add the access token in the Authorization header
		request.addHeader("Authorization", "Bearer " + accessToken);
	}

	@NotNull
	@Override
	public CompletableFuture<String> getAuthorizationTokenAsync(@NotNull URL url) {
		CompletableFuture<String> completableFuture = new CompletableFuture<String>() {

		};

		return completableFuture;
	}
}