package dk.statsbiblioteket.doms.updatetracker.webservice;

import dk.statsbiblioteket.doms.webservices.Credentials;
import dk.statsbiblioteket.doms.webservices.Base64;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.lang.*;
import java.lang.String;

import com.sun.jersey.api.client.*;

/* $Id$
 * $Revision$
 * $Date$
 * $Author$
 *
 * The DOMS project.
 * Copyright (C) 2007-2009  The State and University Library
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


/**
 * TODO javadoc
 */
public class ECM {

    private WebResource restApi;
    private Credentials credentials;
    protected java.lang.String location;

    /**
     * Constructor
     *
     * @param creds
     * @param ecmLocation
     * @throws MalformedURLException
     */
    public ECM(Credentials creds, String ecmLocation)
            throws MalformedURLException {
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

    /**
     * For the given PID of a collection, returns a list of pids of the objects
     * in this collection.
     *
     * @param collectionPid
     * @param viewAngle
     * @param entryContentModel
     * @throws BackendInvalidCredsException
     * @throws BackendMethodFailedException
     * @return
     */
    public List<String> getAllEntryObjectsInCollection(String collectionPid,
                                                       String viewAngle,
                                                       String entryContentModel)
            throws BackendInvalidCredsException,
            BackendMethodFailedException {
        try {
            PidList pidlist = restApi
                    .path("/getAllEntryObjectsForCollection/")
                    .path(collectionPid)
                    .path("/forAngle/")
                    .path(viewAngle)
                    .queryParam("entryContentModel", entryContentModel)
                    .header("Authorization", credsAsBase64())
                    .get(PidList.class);

            return pidlist;
        } catch (UniformInterfaceException e) {
            if (e.getResponse().getClientResponseStatus()
                    .equals(ClientResponse.Status.UNAUTHORIZED)) {
                throw new BackendInvalidCredsException(
                        "Invalid Credentials Supplied", e);
            } else {
                throw new BackendMethodFailedException("Server error", e);
            }
        }
    }

}
