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

import java.net.URL;
import javax.lang.model.element.Element;
import javax.swing.Action;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ui.ElementJavadoc;
import org.netbeans.spi.editor.completion.CompletionDocumentation;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
public abstract class StrutsXMLConfigCompletionDoc implements CompletionDocumentation {

    public static StrutsXMLConfigCompletionDoc createAttribValueDoc(String text) {
        return new AttribValueDoc(text);
    }

    public static StrutsXMLConfigCompletionDoc createJavaDoc(CompilationController cc, Element element) {
        return new JavaElementDoc(ElementJavadoc.create(cc, element));
    }

    public static StrutsXMLConfigCompletionDoc createInterceptorRefDoc(String displayName, String intClass) {
        return new InterceptorRefDoc(displayName, intClass);
    }

    public URL getURL() {
        return null;
    }

    public CompletionDocumentation resolveLink(String link) {
        return null;
    }

    public Action getGotoSourceAction() {
        return null;
    }

    private static class AttribValueDoc extends StrutsXMLConfigCompletionDoc {

        private String text;

        public AttribValueDoc(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    private static class JavaElementDoc extends StrutsXMLConfigCompletionDoc {

        private ElementJavadoc elementJavadoc;

        public JavaElementDoc(ElementJavadoc elementJavadoc) {
            this.elementJavadoc = elementJavadoc;
        }

        @Override
        public JavaElementDoc resolveLink(String link) {
            ElementJavadoc doc = elementJavadoc.resolveLink(link);
            return doc != null ? new JavaElementDoc(doc) : null;
        }

        @Override
        public URL getURL() {
            return elementJavadoc.getURL();
        }

        public String getText() {
            return elementJavadoc.getText();
        }

        @Override
        public Action getGotoSourceAction() {
            return elementJavadoc.getGotoSourceAction();
        }
    }

    private static class InterceptorRefDoc extends StrutsXMLConfigCompletionDoc {

        private String text;
        private String intClass;

        public InterceptorRefDoc(String text, String intClass) {
            this.text = text;
            this.intClass = intClass;
        }

        public String getText() {
            return text;
        }
    }
}
