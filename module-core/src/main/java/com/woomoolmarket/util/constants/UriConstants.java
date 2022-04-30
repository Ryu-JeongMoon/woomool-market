package com.woomoolmarket.util.constants;

public class UriConstants {

  public static final String[] SHOULD_NOT_FILTER_URL_PATTERNS = new String[]{
    "/css/**", "/js/**", "/vendor/**", "/webjars/**", "/img/**",
    "/logout", "/swagger-ui/**", "/swagger-resources/**", "/v2/**"
  };

  public static class Full {

    public static final String BASE_URL = "https://localhost:8443";
    public static final String MY_PAGE = "https://localhost:8443/members/my-page";

    public static final String TOKEN_REQUEST_URI = "https://oauth2.googleapis.com/token";
    public static final String TOKEN_CALLBACK_URI = "https://localhost:8443/oauth2/google";
    public static final String VALIDATION_URI = "https://oauth2.googleapis.com/tokeninfo";
  }

  public static class Mapping {

    public static final String ROOT = "/";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";

    public static final String MEMBERS = "/members";
    public static final String MEMBERS_DETAIL = "/members/{id}";
    public static final String MEMBERS_EDIT = "/members/edit-page";
    public static final String MEMBERS_MY_PAGE = "/members/my-page";

    public static final String POSTS = "/posts";
    public static final String POSTS_DETAIL = "/posts/{id}";
    public static final String POSTS_EDIT = "/posts/edit-page/{id}";
    public static final String POSTS_WRITE_PAGE = "/posts/write-page";

    public static final String ISSUE_GOOGLE_TOKEN = "/oauth2/google";
    public static final String ISSUE_OAUTH2_TOKEN = "/oauth2/{registrationId}";
    public static final String RENEW_GOOGLE_TOKEN = "/oauth2/google/renewal";
    public static final String RENEW_OAUTH2_TOKEN = "/oauth2/{registrationId}/renewal";
    public static final String RENEW_OAUTH2_TOKEN_AND_REDIRECT = "/oauth2/{registrationId}/renewal/redirect";
    public static final String VALIDATE_OAUTH2_TOKEN = "/oauth2/validation";

    public static final String RENEW_LOCAL_TOKEN_AND_REDIRECT = "/renewal/redirect";
  }

  public static class Keyword {

    public static final String REDIRECT = "redirect:";
  }
}
