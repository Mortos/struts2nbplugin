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
package org.netbeans.modules.web.frameworks.struts2.editor;

import java.util.HashMap;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.xml.text.syntax.SyntaxElement;
import org.netbeans.modules.xml.text.syntax.XMLSyntaxSupport;
import org.netbeans.modules.xml.text.syntax.dom.StartTag;

/**
 * This code is based on the Spring support implementation in NetBeans 6.1
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public final class DocumentContext {

    private Document document;
    private XMLSyntaxSupport syntaxSupport;
    private int caretOffset = -1;
    private SyntaxElement element;
    private TokenItem token;
    private StartTag docRoot;
    private boolean valid = false;
    private HashMap<String, String> declaredNamespaces =
            new HashMap<String, String>();

    private DocumentContext(Document document, int caretOffset) {
        this.caretOffset = caretOffset;
        this.document = document;
        this.syntaxSupport = new XMLSyntaxSupport((BaseDocument) document);
        initialize();
    }

    public static DocumentContext createContext(Document doc, int caretOffset) {
        DocumentContext dc = new DocumentContext(doc, caretOffset);
        if (!dc.isValid()) {
            dc = null;
        }
        return dc;
    }

    private void initialize() {
        valid = true;
        declaredNamespaces.clear();
        try {
            element = syntaxSupport.getElementChain(caretOffset);
            token = syntaxSupport.getTokenChain(caretOffset, Math.min(document.getLength(), caretOffset + 1));
            this.docRoot = ContextUtilities.getRoot(element);
        } catch (BadLocationException ex) {
            // No context support available in this case
            valid = false;
        }
    }

    private boolean isValid() {
        return this.valid;
    }

    public int getCurrentTokenId() {
        if (isValid()) {
            return token.getTokenID().getNumericID();
        } else {
            return -1;
        }
    }

    public TokenItem getCurrentToken() {
        if (isValid()) {
            return token;
        } else {
            return null;
        }
    }

    public String getCurrentTokenImage() {
        if (isValid()) {
            return token.getImage();
        } else {
            return null;
        }
    }

    public SyntaxElement getCurrentElement() {
        return this.element;
    }

    public Document getDocument() {
        return this.document;
    }

    public int getCaretOffset() {
        return this.caretOffset;
    }
}
