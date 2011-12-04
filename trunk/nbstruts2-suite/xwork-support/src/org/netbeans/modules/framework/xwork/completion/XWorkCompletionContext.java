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
package org.netbeans.modules.framework.xwork.completion;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh Maksimovich
 */
public class XWorkCompletionContext {

    private Document document;
    private FileObject file;
    private boolean valid = false;
    private int offset;
    private int endOffset;
    private CharSequence text;
    private int typedLength;
    private CharSequence typedText;
    private CharSequence attributeName;
    private CharSequence tagName;

    public XWorkCompletionContext(Document document, int caretOffset) throws BadLocationException {

        this.document = document;
        file = NbEditorUtilities.getFileObject(document);

        final TokenHierarchy<Document> tokenHierarchy = TokenHierarchy.get(document);
        final TokenSequence<XMLTokenId> tokenSequence = tokenHierarchy.tokenSequence(XMLTokenId.language());
        tokenSequence.move(caretOffset);
        if (tokenSequence.moveNext()) {
            final Token<XMLTokenId> token = tokenSequence.token();
            if (hasTokenId(token, XMLTokenId.VALUE)) {
                valid = true;
                initValue(tokenHierarchy, token, caretOffset);
                initAttribute(tokenSequence);
                initTag(tokenSequence);
            }
        }
    }

    public Document document() {
        return document;
    }

    public FileObject file() {
        return file;
    }

    public boolean isValid() {
        return valid;
    }

    public int offset() {
        return offset;
    }

    public int endOffset() {
        return endOffset;
    }

    public CharSequence text() {
        return text;
    }

    public int typedLength() {
        return typedLength;
    }

    public CharSequence typedText() {
        return typedText;
    }

    public CharSequence attributeName() {
        return attributeName;
    }

    public CharSequence tagName() {
        return tagName;
    }

    public boolean atAttribute(String attribute, String tag) {
        return isValid()
                && attribute.equals(attributeName())
                && tag.equals(tagName());
    }

    private boolean hasTokenId(final Token<XMLTokenId> token, final XMLTokenId id) {
        return id.equals(token.id());
    }

    private int internalOffset(final Token<XMLTokenId> token) {
        if (hasTokenId(token, XMLTokenId.VALUE)
                || hasTokenId(token, XMLTokenId.TAG)) {
            return 1;
        }
        return 0;
    }

    private int internalPostfix(final Token<XMLTokenId> token) {
        if (hasTokenId(token, XMLTokenId.VALUE) && token.text().toString().endsWith("\"")) {
            return 1;
        }
        return 0;
    }

    private CharSequence tokenText(final Token<XMLTokenId> token) {
        CharSequence tokenChars = token.text();
        if (tokenChars.length() > 0) {
            final int startOffset = internalOffset(token);
            final int finishOffset = token.length() - internalPostfix(token);
            return tokenChars.subSequence(startOffset, finishOffset);
        }
        return tokenChars;
    }

    private CharSequence tokenName(final XMLTokenId type, final TokenSequence<XMLTokenId> tokenSequence) {
        do {
            Token<XMLTokenId> testToken = tokenSequence.token();
            if (type.equals(testToken.id())) {
                return tokenText(testToken);
            }
        } while (tokenSequence.movePrevious());

        return null;
    }

    private void initValue(
            final TokenHierarchy<Document> tokenHierarchy, final Token<XMLTokenId> token,
            int caretOffset) {
        offset = token.offset(tokenHierarchy) + internalOffset(token);
        endOffset = token.offset(tokenHierarchy) + token.length() - internalPostfix(token);
        text = tokenText(token);
        typedLength = caretOffset - offset;
        if (typedLength < 0) {
            valid = false;
        } else {
            typedText = text.subSequence(0, typedLength);
        }
    }

    private void initAttribute(final TokenSequence<XMLTokenId> tokenSequence) {
        if (valid) {
            attributeName = tokenName(XMLTokenId.ARGUMENT, tokenSequence);
        }
    }

    private void initTag(final TokenSequence<XMLTokenId> tokenSequence) {
        if (valid) {
            tagName = tokenName(XMLTokenId.TAG, tokenSequence);
        }
    }
}
