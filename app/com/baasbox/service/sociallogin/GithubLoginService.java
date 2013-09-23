package com.baasbox.service.sociallogin;

import org.codehaus.jackson.JsonNode;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import play.libs.Json;
import play.mvc.Http.Request;
import play.mvc.Http.Session;

public class GithubLoginService extends SocialLoginService {

	public GithubLoginService(String appcode) {
		super("github",appcode);
	}

	@Override
	public Class<? extends Api> provider() {
		return GithubApi.class;
	}

	@Override
	protected OAuthRequest buildOauthRequestForUserInfo(Token accessToken) {
		return new OAuthRequest(Verb.GET, userInfoUrl());
	}
	@Override
	public Boolean needToken() {
		return false;
	}

	@Override
	public String userInfoUrl() {
		return "https://api.github.com/user";
	}

	@Override
	public String getVerifierFromRequest(Request r) {
		return r.getQueryString("code");
	}

	@Override
	public Token getAccessTokenFromRequest(Request r,Session s) {
		return null;
	}

	@Override
	public UserInfo extractUserInfo(Response r) {
		JsonNode user = Json.parse(r.getBody());
		UserInfo ui = new UserInfo();
		ui.setId(user.get("id").getTextValue());
		ui.setUsername(user.get("login").getTextValue());
		ui.addData("avatar", user.get("avatar_url").getTextValue());
		ui.addData("personal_url", user.get("html_url").getTextValue());
		ui.addData("name", user.get("name").getTextValue());
		ui.addData("location", user.get("location").getTextValue());
		return ui;
		
	}

}
