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

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkEmptyCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkJavaCompletionContext;
import org.netbeans.modules.framework.xwork.completion.annotation.DetectionUserTask;
import org.netbeans.modules.framework.xwork.completion.annotation.JavaSourceCompletionPoint;
import org.netbeans.modules.framework.xwork.completion.validator.XWorkValidatorTypeAttributeValueCompletor;
import org.netbeans.modules.framework.xwork.editor.AbstractTextEditorSupport;
import org.netbeans.modules.framework.xwork.sp.edtor.completion.AbstractAsyncCompletionQuery;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public class JavaSourceCompletionQuery extends AbstractAsyncCompletionQuery {

    public JavaSourceCompletionQuery(boolean fullQuery) {
        super(fullQuery);
    }

    @Override
    protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
        try {
            Source source = Source.create(document);
            DetectionUserTask detectionTask = new DetectionUserTask(document, caretOffset);
            Future<Void> result = ParserManager.parseWhenScanFinished(
                    Collections.singletonList(source), detectionTask);
            result.get();

            XWorkCompletor completor = XWorkEmptyCompletor.instance();
            if (JavaSourceCompletionPoint.CUSTOM_VALIDATOR_TYPE.equals(detectionTask.getCompletionPoint())) {
                AbstractTextEditorSupport context = new XWorkJavaCompletionContext((AbstractDocument) document, caretOffset);
                context.init();
                completor = new XWorkValidatorTypeAttributeValueCompletor(context);
            }

            completionResultSet.addAllItems(completor.items());

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            completionResultSet.finish();
        }
    }
}
