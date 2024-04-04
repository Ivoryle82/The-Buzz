package edu.lehigh.cse216.exh226.backend;

/**
 * OAuthData class:
 * !! THIS CLASS IS ONLY MEANT FOR WORKING WITH THE JSON OBJECTS THAT GOOGLE'S
 * OAUTH RETURNS !!
 */
public class OAuthData {
    public String token_endpoint; // the Discovery Document's token_endpoint
    public String id_token; // The OAuth id_token in the HTTP POST response
}
