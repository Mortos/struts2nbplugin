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
package org.netbeans.modules.framework.xwork.sp.query;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkXMLCompletionContext;
import org.netbeans.modules.framework.xwork.completion.validator.XWorkValidatorCompletorFactory;
import org.netbeans.modules.framework.xwork.sp.edtor.completion.AbstractAsyncCompletionQuery;
import org.netbeans.spi.editor.completion.CompletionResultSet;

/**
 *
 * @author Aleh
 */
public class ValidatorAsyncCompletionQuery extends AbstractAsyncCompletionQuery {

    public ValidatorAsyncCompletionQuery(boolean fullQuery) {
        super(fullQuery);
    }

    @Override
    protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
        try {
            XWorkXMLCompletionContext context = new XWorkXMLCompletionContext((AbstractDocument) document, caretOffset);
            context.init();
            XWorkCompletor completor = XWorkValidatorCompletorFactory.completor(context);
            completionResultSet.addAllItems(completor.items());
        } finally {
            completionResultSet.finish();
        }
    }
}
