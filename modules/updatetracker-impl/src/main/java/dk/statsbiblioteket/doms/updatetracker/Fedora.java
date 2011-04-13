package dk.statsbiblioteket.doms.updatetracker;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import dk.statsbiblioteket.doms.webservices.authentication.Base64;
import dk.statsbiblioteket.doms.webservices.authentication.Credentials;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: Oct 14, 2010
 * Time: 3:39:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Fedora {



    private WebResource restApi;
    private Credentials credentials;
    protected java.lang.String location;


    public Fedora(Credentials creds, java.lang.String ecmLocation) {
        credentials = creds;
        location = ecmLocation;

        Client client = Client.create();
        restApi = client.resource(location);
    }

    protected java.lang.String credsAsBase64(){
        java.lang.String preBase64 = credentials.getUsername() + ":"
                           + credentials.getPassword();
        java.lang.String base64 = Base64.encodeBytes(preBase64.getBytes());
        return "Basic " + base64;
    }


    public List<java.lang.String> query(java.lang.String query)
            throws BackendInvalidCredsException, BackendMethodFailedException {
        //TODO sanitize label
        try {
            java.lang.String objects = restApi
                    .path("/risearch")
                    .queryParam("type", "tuples")
                    .queryParam("lang", "iTQL")
                    .queryParam("format", "CSV")
                    .queryParam("flush","true")
                    .queryParam("stream","on")
                    .queryParam("query", query)
                    .header("Authorization", credsAsBase64())
                    .post(java.lang.String.class);
            java.lang.String[] lines = objects.split("\n");
            List<java.lang.String> foundobjects = new ArrayList<java.lang.String>();
            for (java.lang.String line : lines) {
                if (line.startsWith("\"")){
                    continue;
                }

                line = line.replaceAll("info:fedora/","");

                foundobjects.add(line);
            }
            return foundobjects;
        }  catch (UniformInterfaceException e) {
            if (e.getResponse().getStatus()
                == ClientResponse.Status.UNAUTHORIZED.getStatusCode()) {
                throw new BackendInvalidCredsException(
                        "Invalid Credentials Supplied",
                        e);
            }else  {
                throw new BackendMethodFailedException("Server error", e);
            }
        }
    }
}
