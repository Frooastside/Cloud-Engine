package de.frooastside.engine;

import de.frooastside.authentication.UserAuthentication;
import de.frooastside.engine.language.I18n;

public class Session {
	
	private String UUID;
	private String displayName;
	private String accessToken;
	
	public Session(String username, String password) {
		login(username, password);
	}
	
	private void login(String username, String password) {
		UserAuthentication userAuthentication = Engine.getEngine().getUserService().createUserAuthentication();
		userAuthentication.setUsername(username);
		userAuthentication.setPassword(password);
		userAuthentication.authenticate();
		if(userAuthentication.isSuccessful()) {
			if(userAuthentication.isActivated()) {
				UUID = userAuthentication.getUUID();
				displayName = userAuthentication.getDisplayname();
				accessToken = userAuthentication.getAccessToken();
			}else {
				System.err.println(I18n.get("error.session.notactivated"));
			}
		}else {
			System.err.println(I18n.get("error.session.authentication") + userAuthentication.getErrorMessage());
		}
	}

	public String getUUID() {
		return UUID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getAccessToken() {
		return accessToken;
	}

}
