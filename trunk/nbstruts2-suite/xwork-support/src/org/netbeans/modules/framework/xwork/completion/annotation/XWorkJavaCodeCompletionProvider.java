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
package org.netbeans.modules.framework.xwork.completion.annotation;

import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author Aleh
 */
@MimeRegistration(mimeType = "text/x-java", service = CompletionProvider.class)
public class XWorkJavaCodeCompletionProvider implements CompletionProvider {

    @Override
    public CompletionTask createTask(int queryType, JTextComponent textComponent) {

        if ((queryType & CompletionProvider.COMPLETION_QUERY_TYPE) != 0) {
            return new AsyncCompletionTask(new XWorkJavaCodeCompletionQuery(), textComponent);
        }
        return null;
    }

    @Override
    public int getAutoQueryTypes(JTextComponent textComponent, String typedText) {
        // TODO: at the moment automatic completion is disabled.
        return 0;
    }
}
