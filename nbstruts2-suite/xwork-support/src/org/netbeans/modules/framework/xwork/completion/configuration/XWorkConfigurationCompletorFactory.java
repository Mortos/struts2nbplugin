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
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion.configuration;

import org.netbeans.modules.framework.xwork.completion.XWorkCompletionContext;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkEmptyCompletor;

/**
 *
 * @author Aleh
 */
public class XWorkConfigurationCompletorFactory {

    private static final String VALIDATOR_TAG = "validator";
    private static final String CLASS_ATTRIBITE = "class";

    public static XWorkCompletor completor(XWorkCompletionContext context) {
        if (context.atAttribute(CLASS_ATTRIBITE, VALIDATOR_TAG)) {
            return new XWorkConfigurationClassAttributeCompletor(context);
        }

        return XWorkEmptyCompletor.instance();
    }
}
