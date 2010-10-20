package dk.statsbiblioteket.doms.updatetracker.webservice;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import dk.statsbiblioteket.doms.webservices.Base64;
import dk.statsbiblioteket.doms.webservices.Credentials;

import java.lang.String;
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


    public Fedora(Credentials creds, String ecmLocation) {
        credentials = creds;
        location = ecmLocation;

        Client client = Client.create();
        restApi = client.resource(location);
    }

    protected String credsAsBase64(){
        String preBase64 = credentials.getUsername() + ":"
                           + credentials.getPassword();
        String base64 = Base64.encodeBytes(preBase64.getBytes());
        return "Basic " + base64;
    }


    public List<String> query(String query)
            throws BackendInvalidCredsException, BackendMethodFailedException {
        //TODO sanitize label
        try {
            String objects = restApi
                    .path("/risearch")
                    .queryParam("type", "tuples")
                    .queryParam("lang", "iTQL")
                    .queryParam("format", "CSV")
                    .queryParam("flush","true")
                    .queryParam("stream","on")
                    .queryParam("query", query)
                    .header("Authorization", credsAsBase64())
                    .post(String.class);
            String[] lines = objects.split("\n");
            List<String> foundobjects = new ArrayList<String>();
            for (String line : lines) {
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
