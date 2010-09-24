package dk.statsbiblioteket.doms.updatetracker.webservice;

import javax.jws.WebParam;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;
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
     *
     * @throws MethodFailedException
     * @throws InvalidCredentialsException
     *
     */
    public List<PidDatePidPid> listObjectsChangedSince(
            @WebParam(name = "collectionPid", targetNamespace = "")
            String collectionPid,
            @WebParam(name = "entryCMPid", targetNamespace = "")
            String entryCMPid,
            @WebParam(name = "beginTime", targetNamespace = "")
            XMLGregorianCalendar beginTime)
            throws InvalidCredentialsException, MethodFailedException {

        List<PidDatePidPid> result = new ArrayList<PidDatePidPid>();

        XMLGregorianCalendar lastChangedTime;
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(
                "Europe/Copenhagen"));
        calendar.set(GregorianCalendar.YEAR, 1999);
        calendar.set(GregorianCalendar.MONTH, 12);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 31);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 23);
        calendar.set(GregorianCalendar.MINUTE, 59);
        calendar.set(GregorianCalendar.SECOND, 59);
        calendar.set(GregorianCalendar.MILLISECOND, 999);

        try {
            lastChangedTime
                    = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    calendar);
        } catch (DatatypeConfigurationException e) {
            throw new MethodFailedException(
                    "Could not make new XMLGregorianCalendar", "");
        }

        // TODO mockup with more functionality, not hardcoded
        PidDatePidPid objectThatChanged = new PidDatePidPid();
        objectThatChanged.setPid("unknownPID");
        objectThatChanged.setLastChangedTime(lastChangedTime);
        objectThatChanged.setCollectionPid("unknownPID");
        objectThatChanged.setEntryCMPid("unknownPID");

        result.add(objectThatChanged);

        return result;
    }
}
