package dk.statsbiblioteket.doms.updatetracker;

import com.sun.jersey.api.client.UniformInterfaceException;

import java.lang.*;
import java.lang.String;
/* $Id: BackendInvalidCredsException.java 769 2010-09-24 14:41:24Z jrgatsb $
 * $Revision: 769 $
 * $Date: 2010-09-24 16:41:24 +0200 (Fri, 24 Sep 2010) $
 * $Author: jrgatsb $
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

public class BackendInvalidCredsException extends Exception {
    public BackendInvalidCredsException(String s) {
        super(s);
    }

    public BackendInvalidCredsException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
