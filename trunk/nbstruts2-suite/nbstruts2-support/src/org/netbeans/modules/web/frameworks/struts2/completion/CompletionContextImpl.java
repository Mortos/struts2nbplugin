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

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.web.frameworks.struts2.editor.DocumentContext;
import org.netbeans.modules.xml.text.api.XMLDefaultTokenContext;
import org.netbeans.modules.xml.text.syntax.SyntaxElement;
import org.netbeans.modules.xml.text.syntax.XMLSyntaxSupport;
import org.netbeans.modules.xml.text.syntax.dom.EmptyTag;
import org.netbeans.modules.xml.text.syntax.dom.EndTag;
import org.netbeans.modules.xml.text.syntax.dom.StartTag;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Node;

/**
 * Tracks context information for code completion. 
 * 
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public class CompletionContextImpl implements CompletionContext {

    private ArrayList<String> existingAttributes;
    
    private CompletionType completionType = CompletionType.NONE;
    private Document doc;
    private FileObject fileObject;
    private int caretOffset;
    private DocumentContext documentContext;
    private String typedChars = ""; // NOI18N
    private char lastTypedChar;
    private XMLSyntaxSupport support;

    public CompletionContextImpl(Document doc, int caretOffset) {
        this.doc = doc;
        this.fileObject = NbEditorUtilities.getFileObject(doc);
        this.caretOffset = caretOffset;
        this.support = new XMLSyntaxSupport((BaseDocument) doc);
        this.documentContext = DocumentContext.createContext(doc, caretOffset);
        this.lastTypedChar = support.lastTypedChar();
        initContext();
    }

    private void initContext() {
        if (documentContext == null) {
            return;
        }
        TokenItem token = documentContext.getCurrentToken();
        if(token == null) {
            return;
        }
        
        boolean tokenBoundary = (token.getOffset() == caretOffset) || ((token.getOffset() + token.getImage().length()) == caretOffset);

        int id = token.getTokenID().getNumericID();
        SyntaxElement element = documentContext.getCurrentElement();
        switch (id) {
            //user enters < character
            case XMLDefaultTokenContext.TEXT_ID:
                String chars = token.getImage().trim();
                if (chars != null && chars.equals("") &&
                        token.getPrevious().getImage().trim().equals("/>")) { // NOI18N
                    completionType = CompletionType.NONE;
                    break;
                }
                if (chars != null && chars.equals("") &&
                        token.getPrevious().getImage().trim().equals(">")) { // NOI18N
                    completionType = CompletionType.VALUE;
                    break;
                }
                if (chars != null && !chars.equals("<") &&
                        token.getPrevious().getImage().trim().equals(">")) { // NOI18N
                    completionType = CompletionType.NONE;
                    break;
                }
                if (chars != null && chars.startsWith("<")) { // NOI18N
                    typedChars = chars.substring(1);
                }
                completionType = CompletionType.TAG;
                break;

            //start tag of an element
            case XMLDefaultTokenContext.TAG_ID:
                if (element instanceof EndTag) {
                    completionType = CompletionType.NONE;
                    break;
                }
                if (element instanceof EmptyTag) {
                    if (token != null &&
                            token.getImage().trim().equals("/>")) { // NOI18N
                        TokenItem prevToken = token.getPrevious();
                        if (prevToken != null && prevToken.getTokenID().getNumericID() == XMLDefaultTokenContext.WS_ID && caretOffset == token.getOffset()) {
                            completionType = CompletionType.ATTRIBUTE;
                        } else {
                            completionType = CompletionType.NONE;
                        }
                        break;
                    }
                    EmptyTag tag = (EmptyTag) element;
                    if (element.getElementOffset() + 1 == this.caretOffset) {
                        completionType = CompletionType.TAG;
                        break;
                    }
                    if (caretOffset > element.getElementOffset() + 1 &&
                            caretOffset <= element.getElementOffset() + 1 + tag.getTagName().length()) {
                        completionType = CompletionType.TAG;
                        typedChars = tag.getTagName();
                        break;
                    }
                    completionType = CompletionType.ATTRIBUTE;
                    break;
                }

                if (element instanceof StartTag) {
                    if (token != null &&
                            token.getImage().trim().equals(">")) { // NOI18N
                        TokenItem prevToken = token.getPrevious();
                        if (prevToken != null && prevToken.getTokenID().getNumericID() == XMLDefaultTokenContext.WS_ID && caretOffset == token.getOffset()) {
                            completionType = CompletionType.ATTRIBUTE;
                        } else {
                            completionType = CompletionType.NONE;
                        }
                        break;
                    }
                    if (element.getElementOffset() + 1 != this.caretOffset) {
                        StartTag tag = (StartTag) element;
                        typedChars = tag.getTagName();
                    }
                }
                if (lastTypedChar == '>') {
                    completionType = CompletionType.VALUE;
                    break;
                }
                completionType = CompletionType.TAG;
                break;

            //user enters an attribute name
            case XMLDefaultTokenContext.ARGUMENT_ID:
                completionType = CompletionType.ATTRIBUTE;
                typedChars = token.getImage().substring(0, caretOffset - token.getOffset());
                ;
                break;

            //some random character
            case XMLDefaultTokenContext.CHARACTER_ID:
            //user enters = character, we should ignore all other operators
            case XMLDefaultTokenContext.OPERATOR_ID:
                completionType = CompletionType.NONE;
                break;
            //user enters either ' or "
            case XMLDefaultTokenContext.VALUE_ID:
                if (!tokenBoundary) {
                    completionType = CompletionType.ATTRIBUTE_VALUE;
                    typedChars = token.getImage().substring(1, caretOffset - token.getOffset());
                } else {
                    completionType = CompletionType.NONE;
                }
                break;

            //user enters white-space character
            case XMLDefaultTokenContext.WS_ID:
                completionType = CompletionType.NONE;

                if (token.getOffset() == caretOffset) {
                    break;
                }

                TokenItem prev = token.getPrevious();
                while (prev != null &&
                        (prev.getTokenID().getNumericID() == XMLDefaultTokenContext.WS_ID)) {
                    prev = prev.getPrevious();
                }

                if (prev.getTokenID().getNumericID() == XMLDefaultTokenContext.ARGUMENT_ID) {
                    typedChars = prev.getImage();
                    completionType = CompletionType.ATTRIBUTE;
                } else if ((prev.getTokenID().getNumericID() == XMLDefaultTokenContext.VALUE_ID) ||
                        (prev.getTokenID().getNumericID() == XMLDefaultTokenContext.TAG_ID)) {
                    completionType = CompletionType.ATTRIBUTE;
                }
                break;

            default:
                completionType = CompletionType.NONE;
                break;
        }
    }

    public CompletionType getCompletionType() {
        return completionType;
    }

    public String getTypedPrefix() {
        return typedChars;
    }

    public Document getDocument() {
        return this.doc;
    }

    public DocumentContext getDocumentContext() {
        return this.documentContext;
    }

    public int getCaretOffset() {
        return caretOffset;
    }

    public Node getTag() {
        SyntaxElement element = documentContext.getCurrentElement();
        return (element instanceof Tag) ? (Node) element : null;
    }

    public TokenItem getCurrentToken() {
        return documentContext.getCurrentToken();
    }

    public List<String> getExistingAttributes() {
        if (existingAttributes != null) {
            return existingAttributes;
        }
        existingAttributes = new ArrayList<String>();
        TokenItem item = documentContext.getCurrentToken().getPrevious();
        while (item != null) {
            if (item.getTokenID().getNumericID() ==
                    XMLDefaultTokenContext.TAG_ID) {
                break;
            }
            if (item.getTokenID().getNumericID() ==
                    XMLDefaultTokenContext.ARGUMENT_ID) {
                existingAttributes.add(item.getImage());
            }
            item = item.getPrevious();
        }
        return existingAttributes;
    }

    public FileObject getFileObject() {
        return fileObject;
    }
}
