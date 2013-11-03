/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is XWork Support. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Aleh
 */
public enum XWorkDocuments {

    CONFIGURATION("text/x-xwork-validator-config+xml") {
        {
            docTypes.add("-//OpenSymphony Group//XWork Validator Config 1.0//EN");
        }
    },
    VALIDATION("text/x-xwork-validator+xml") {
        {
            docTypes.add("-//OpenSymphony Group//XWork Validator 1.0//EN");
            docTypes.add("-//OpenSymphony Group//XWork Validator 1.0.2//EN");
            docTypes.add("-//OpenSymphony Group//XWork Validator 1.0.3//EN");
        }
    };
    private String mimeType;
    protected List<String> docTypes = new LinkedList<String>();

    private XWorkDocuments(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isMimeType(String testMimeType) {
        return mimeType.equals(testMimeType);
    }

    public boolean isDocType(String testDocType) {
        return docTypes.contains(testDocType);
    }
}
