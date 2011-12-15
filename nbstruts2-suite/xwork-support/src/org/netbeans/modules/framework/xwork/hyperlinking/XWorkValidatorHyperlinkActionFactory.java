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
package org.netbeans.modules.framework.xwork.hyperlinking;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.framework.xwork.completion.XWorkXMLCompletionContext;
import org.netbeans.modules.framework.xwork.completion.validator.XWorkValidatorTypeAttributeValueCompletor;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorHyperlinkActionFactory {

    private static final String VALIDATOR_TAG = "validator";
    private static final String FIELD_VALIDATOR_TAG = "field-validator";
    private static final String TYPE_ATTRIBITE = "type";

    public static boolean isRegisteredHyperlinkPoint(XWorkXMLCompletionContext context) {
        if (context.atAttribute(TYPE_ATTRIBITE, VALIDATOR_TAG)
                || context.atAttribute(TYPE_ATTRIBITE, FIELD_VALIDATOR_TAG)) {
            return true;
        }

        return false;
    }

    public static void hyperlinkAction(XWorkXMLCompletionContext context) {
        if (context.atAttribute(TYPE_ATTRIBITE, VALIDATOR_TAG)
                || context.atAttribute(TYPE_ATTRIBITE, FIELD_VALIDATOR_TAG)) {
            XWorkValidatorTypeAttributeValueCompletor completor = new XWorkValidatorTypeAttributeValueCompletor(context);
            FileObject sourceFile = completor.getChoiceOrigin(context.getInnerContent().toString());
            if (sourceFile != null) {
                openFileInEditor(sourceFile);
            }
        }
    }

    private static void openFileInEditor(FileObject file) {
        try {
            DataObject dataObject = DataObject.find(file);
            EditorCookie editorCookie = dataObject.getCookie(EditorCookie.class);
            if (editorCookie != null) {
                editorCookie.open();
            }
        } catch (DataObjectNotFoundException ex) {
            Logger.getLogger("global").log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
