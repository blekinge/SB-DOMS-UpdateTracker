package dk.statsbiblioteket.doms.updatetracker.webservice;

import javax.jws.WebParam;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.*;
import java.lang.String;
import java.util.List;
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

public class UpdateTrackerWebserviceImpl implements UpdateTrackerWebservice{

    /**
     * TODO javadoc
     *
     * @param collectionPid
     * @param entryCMPid
     * @param beginTime
     * @return returns java.util.List<dk.statsbiblioteket.doms.updatetracker.webservice.PidDatePidPid>
     * @throws MethodFailedException
     *
     * @throws InvalidCredentialsException
     *
     */
    public List<PidDatePidPid> listObjectsChangedSince(@WebParam(name = "collectionPid", targetNamespace = "") String collectionPid, @WebParam(name = "entryCMPid", targetNamespace = "") String entryCMPid, @WebParam(name = "beginTime", targetNamespace = "") XMLGregorianCalendar beginTime) throws InvalidCredentialsException, MethodFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
