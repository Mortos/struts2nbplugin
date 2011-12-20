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
package org.netbeans.modules.framework.xwork.sp.edtor.completion;

import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.framework.xwork.XWorkMimeType;
import org.netbeans.modules.framework.xwork.sp.query.JavaSourceCompletionQuery;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * Completion provider for Java source files. Provides XWork related completion.
 *
 * @author Aleh Maksimovich (aleh.maksimovich@gmail.com)
 * @since 0.5.2
 */
@MimeRegistration(mimeType = XWorkMimeType.JAVA_SOURCE_MIME, service = CompletionProvider.class)
public class JavaSourceCompletionProvider implements CompletionProvider {

    /**
     * {@inheritDoc}
     *
     * @param queryType {@inheritDoc}
     * @param textComponent {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CompletionTask createTask(int queryType, JTextComponent textComponent) {

        switch (queryType) {
            case CompletionProvider.COMPLETION_QUERY_TYPE:
                return new AsyncCompletionTask(
                        new JavaSourceCompletionQuery(false), textComponent);
            case CompletionProvider.COMPLETION_ALL_QUERY_TYPE:
                return new AsyncCompletionTask(
                        new JavaSourceCompletionQuery(true), textComponent);
            default:
                return null;
        }
    }

    /**
     * No automatic completion currently available in Java files. Returns 0.
     *
     * @param textComponent {@inheritDoc}
     * @param typedText {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getAutoQueryTypes(JTextComponent textComponent, String typedText) {
        return 0;
    }
}
