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
package org.netbeans.modules.web.frameworks.struts2.completion;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.web.frameworks.struts2.completion.CompletionContext.CompletionType;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public class StrutsXMLConfigCompletionProvider implements CompletionProvider {

    public CompletionTask createTask(int queryType, JTextComponent component) {
        if ((queryType & COMPLETION_QUERY_TYPE) == COMPLETION_QUERY_TYPE) {
            return new AsyncCompletionTask(new StrutsXMLConfigCompletionQuery(queryType,
                    component.getSelectionStart()), component);
        }

        return null;
    }

    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0; // XXX: Return something more specific
    }
    
      /**
     * XXX: To take care of filter() and canFilter() methods to shortcircuit calls to query
     * every time user types a key with completion open
     */
    private static class StrutsXMLConfigCompletionQuery extends AsyncCompletionQuery {

        private int queryType;
        private int caretOffset;
        private JTextComponent component;

        public StrutsXMLConfigCompletionQuery(int queryType, int caretOffset) {
            this.queryType = queryType;
            this.caretOffset = caretOffset;
        }

        @Override
        protected void preQueryUpdate(JTextComponent component) {
            //XXX: look for invalidation conditions
            this.component = component;
        }

        @Override
        protected void prepareQuery(JTextComponent component) {
            this.component = component;
        }
        
        @Override
        protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
            this.caretOffset = caretOffset;

            CompletionContextImpl context = new CompletionContextImpl(doc, caretOffset);
            if (context.getCompletionType() == CompletionType.NONE) {
                resultSet.finish();
                return;
            }

            switch (context.getCompletionType()) {
                case ATTRIBUTE_VALUE:
                    StrutsXMLCompletionManager.getDefault().completeAttributeValues(resultSet, context);
                    break;
                case ATTRIBUTE:
                    //StrutsXMLCompletionManager.getDefault().completeAttributes(resultSet, context);
                    break;
                case TAG:
                    //StrutsXMLCompletionManager.getDefault().completeElements(resultSet, context);
                    break;
            }

            resultSet.finish();
        }
    }
}
