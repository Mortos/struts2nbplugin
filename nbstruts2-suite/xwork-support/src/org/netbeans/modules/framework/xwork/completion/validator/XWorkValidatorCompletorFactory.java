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
package org.netbeans.modules.framework.xwork.completion.validator;

import org.netbeans.modules.framework.xwork.completion.XMLEditorSupport;
import org.netbeans.modules.framework.xwork.completion.XWorkEmptyCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletor;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorCompletorFactory {

    private static final String VALIDATOR_TAG = "validator";
    private static final String FIELD_VALIDATOR_TAG = "field-validator";
    private static final String TYPE_ATTRIBITE = "type";

    public static XWorkCompletor completor(XMLEditorSupport context) {
        if (context.atAttribute(TYPE_ATTRIBITE, VALIDATOR_TAG)
                || context.atAttribute(TYPE_ATTRIBITE, FIELD_VALIDATOR_TAG)) {
            return new XWorkValidatorTypeAttributeValueCompletor(context);
        }

        return XWorkEmptyCompletor.instance();
    }
}
