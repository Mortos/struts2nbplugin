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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.web.frameworks.struts2.completion.ui;

import javax.swing.text.Document;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class AttribValueItem extends StrutsXMLConfigCompletionItem {

    private String displayText;
    private String docText;

    public AttribValueItem(int substitutionOffset, String displayText, String docText) {
        super(substitutionOffset);
        this.displayText = displayText;
        this.docText = docText;
    }

    public int getSortPriority() {
        return 50;
    }

    public CharSequence getSortText() {
        return displayText;
    }

    public CharSequence getInsertPrefix() {
        return displayText;
    }

    @Override
    protected String getLeftHtmlText() {
        return displayText;
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                if (docText != null) {
                    CompletionDocumentation documentation = StrutsXMLConfigCompletionDoc.createAttribValueDoc(docText);
                    resultSet.setDocumentation(documentation);
                }
                resultSet.finish();
            }
        });
    }
}
