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
import org.openide.util.NbBundle;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
 public class ResultItem extends StrutsXMLConfigCompletionItem {

        private String displayName;
        private String resultClass;
        private String docText;

        public ResultItem(int substitutionOffset, String displayName, String resultClass) {
            super(substitutionOffset);
            this.displayName = displayName;
            this.resultClass = resultClass;
        }

        public int getSortPriority() {
            return 100;
        }

        public CharSequence getSortText() {
            return displayName;
        }

        public CharSequence getInsertPrefix() {
            return displayName;
        }

        @Override
        protected String getLeftHtmlText() {
            return displayName;
        }

        @Override
        public CompletionTask createDocumentationTask() {
            if(docText == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("<b>"); // NOI18N
                sb.append(NbBundle.getMessage(ResultItem.class, "MESG_Interceptor_Doc"));
                sb.append("</b>"); // NOI18N
                sb.append(resultClass);
                docText = sb.toString();
            }
            
            return new AsyncCompletionTask(new AsyncCompletionQuery() {
                @Override
                protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                    CompletionDocumentation docItem = StrutsXMLConfigCompletionDoc.createAttribValueDoc(docText);
                    resultSet.setDocumentation(docItem);
                    resultSet.finish();
                }
            });
        }
    }