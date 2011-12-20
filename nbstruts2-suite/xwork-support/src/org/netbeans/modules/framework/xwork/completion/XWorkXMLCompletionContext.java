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
package org.netbeans.modules.framework.xwork.completion;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AbstractDocument;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.modules.framework.xwork.editor.AbstractLexerEditorSupport;
import org.netbeans.modules.framework.xwork.lexer.TokenHierarchyVisitor;
import org.netbeans.modules.framework.xwork.lexer.TokenHierarchyVisitorException;

/**
 *
 * @author Aleh Maksimovich
 */
public class XWorkXMLCompletionContext extends AbstractLexerEditorSupport<XMLTokenId> {

    private static final Logger LOG = Logger.getLogger(XWorkXMLCompletionContext.class.getName());
    private boolean valid = false;
    private int offset;
    private int endOffset;
    private CharSequence text;
    private int typedLength;
    private CharSequence typedText;
    private CharSequence attributeName;
    private CharSequence tagName;

    public XWorkXMLCompletionContext(AbstractDocument document, int caretOffset) {
        super(XMLTokenId.language(), document, caretOffset);
    }

    @Override
    public void init() {
        // Initialize parent.
        super.init();
        if (!super.isValid()) {
            return; // Parent initialization has failed.
        }

        Token<XMLTokenId> token = getToken();
        if (!hasTokenId(token, XMLTokenId.VALUE)) {
            return;
        }
        offset = getOuterStartOffset() + internalOffset(token);
        endOffset = getOuterEndOffset() - internalPostfix(token);
        text = tokenText(token);
        typedLength = getCaretOffset() - offset;
        if (typedLength < 0) {
            return;
        }
        typedText = text.subSequence(0, typedLength);

        try {
            NameLocator attributeNameLocator = new NameLocator(XMLTokenId.ARGUMENT, getCaretOffset());
            visitHierarchy(attributeNameLocator);
            attributeName = attributeNameLocator.getName();
            NameLocator tagNameLocator = new NameLocator(XMLTokenId.TAG, getCaretOffset());
            visitHierarchy(tagNameLocator);
            tagName = tagNameLocator.getName();
        } catch (TokenHierarchyVisitorException ex) {
            LOG.log(Level.WARNING, null, ex);
            return;
        }

        // Initialization is complete. Context is valid.
        valid = true;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public int getInnerStartOffset() {
        return offset;
    }

    @Override
    public int getInnerEndOffset() {
        return endOffset;
    }

    @Override
    public String getInnerContent() {
        return text.toString();
    }

    @Override
    public int getLeftContentLength() {
        return typedLength;
    }

    @Override
    public String getLeftContent() {
        return typedText.toString();
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

    @Override
    public String getRightContent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInnerLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRightContentLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class NameLocator implements TokenHierarchyVisitor<AbstractDocument> {

        private final XMLTokenId type;
        private final int caretOffset;
        private String name;

        public NameLocator(XMLTokenId type, int caretOffset) {
            this.type = type;
            this.caretOffset = caretOffset;
        }

        @Override
        public void visit(TokenHierarchy<AbstractDocument> tokenHierarchy) throws TokenHierarchyVisitorException {
            // Get token sequence.
            final TokenSequence<XMLTokenId> tokenSequence = tokenHierarchy.tokenSequence(XMLTokenId.language());
            if (tokenSequence == null) {
                throw new TokenHierarchyVisitorException("Document doesn't support selected language.");
            }
            // Get current token.
            tokenSequence.move(caretOffset);
            if (!tokenSequence.moveNext()) {
                throw new TokenHierarchyVisitorException("Could not resolve current token.");
            }

            name = tokenName(type, tokenSequence);
        }

        private String tokenName(final XMLTokenId type, final TokenSequence<XMLTokenId> tokenSequence) {
            do {
                Token<XMLTokenId> testToken = tokenSequence.token();
                if (type.equals(testToken.id())) {
                    return tokenText(testToken).toString();
                }
            } while (tokenSequence.movePrevious());

            return null;
        }

        public String getName() {
            return name;
        }
    }
}
