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

import java.io.IOException;
import javax.lang.model.element.Element;
import javax.swing.text.Document;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.netbeans.modules.web.frameworks.struts2.utils.JavaUtils;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.util.Exceptions;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class JavaElementDocQuery extends AsyncCompletionQuery {
private ElementHandle<?> elemHandle;
        
        public JavaElementDocQuery(ElementHandle<?> elemHandle) {
            this.elemHandle = elemHandle;
        }
        
        @Override
        protected void query(final CompletionResultSet resultSet, Document doc, int caretOffset) {
            try {
                JavaSource js = JavaUtils.getJavaSource(doc);
                if (js == null) {
                    return;
                }

                js.runUserActionTask(new Task<CompilationController>() {

                    public void run(CompilationController cc) throws Exception {
                        cc.toPhase(Phase.RESOLVED);
                        Element element = elemHandle.resolve(cc);
                        if (element == null) {
                            return;
                        }
                        StrutsXMLConfigCompletionDoc doc = StrutsXMLConfigCompletionDoc.createJavaDoc(cc, element);
                        resultSet.setDocumentation(doc);
                    }
                }, false);
                resultSet.finish();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
}
