/*
 * Doms build framework version 1.0.15
 *
 * $Id: dependsforloop.js 279 2010-02-18 14:19:12Z blekinge $
 * $Revision: 279 $
 * $Date: 2010-02-18 15:19:12 +0100 (Thu, 18 Feb 2010) $
 * $Author: blekinge $
 *
 * The DOMS project.
 * Copyright (C) 2007-2010  The State and University Library
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/* Part of doms build framework version 1.0.5 */
importClass(java.io.File);
importPackage(Packages.org.apache.tools.ant);
importPackage(Packages.org.apache.tools.ant.types);

importPackage(Packages.org.apache.tools.ant.taskdefs);
var target = attributes.get("target");

var dirSetname = attributes.get("dependencies");
var dirSet = project.getReference(dirSetname);

var echo = project.createTask("echo");
/*echo.setMessage("Hello, World!");
 echo.execute();
 */
// Obtain a reference to a files
// et in the enclosing project
/*echo.setMessage("fileSet size = "+dirSet.size());

 echo.execute();
 echo.setMessage(dirSet.toString());
 echo.execute();
 */
scanner = dirSet.getDirectoryScanner(project);
dirs = scanner.getIncludedDirectories();
basedir = scanner.getBasedir();
echo.setMessage("module dependencies list size = " + dirs.length);
echo.execute();

for (var i = 0; i < dirs.length; i++) {
    subdir = new File(basedir, dirs[i])
    echo.setMessage("recursing into " + subdir);
    echo.execute();
    list = subdir.list(); //TODO possible race condition
    if (list != null && contains(list,"build.xml")){
        var anttask = project.createTask("ant");
        anttask.setDir(subdir);
        anttask.setTarget(target);
        anttask.setInheritAll(false);
        anttask.perform();
    }
}

function contains(array, name){

    for (var i=0; i< array.length; i++){
        if (array[i] == name){
            return true;
        }
    }
    return false;
}


