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

import javax.swing.text.AbstractDocument;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.lexer.Token;
import org.netbeans.modules.framework.xwork.editor.AbstractLexerEditorSupport;

/**
 *
 * @author Aleh
 */
public class XWorkJavaCompletionContext extends AbstractLexerEditorSupport<JavaTokenId> {

    private boolean valid = false;
    private int offset;
    private int endOffset;
    private CharSequence text;
    private int typedLength;
    private CharSequence typedText;

    public XWorkJavaCompletionContext(AbstractDocument document, int caretOffset) {
        super(JavaTokenId.language(), document, caretOffset);
    }

    @Override
    public void init() {
        // Initialize parent.
        super.init();
        if (!super.isValid()) {
            return; // Parent initialization has failed.
        }

        Token<JavaTokenId> token = getToken();
        offset = getOuterStartOffset() + internalOffset(token);
        endOffset = getOuterEndOffset() - internalPostfix(token);
        text = tokenText(token);
        typedLength = getCaretOffset() - offset;
        if (typedLength < 0) {
            return;
        }
        typedText = text.subSequence(0, typedLength);

        // Initialization is complete. Context is valid.
        valid = true;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public int getInnerEndOffset() {
        return endOffset;
    }

    @Override
    public int getInnerStartOffset() {
        return offset;
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

    private CharSequence tokenText(final Token<JavaTokenId> token) {
        CharSequence tokenChars = token.text();
        if (tokenChars.length() > 0) {
            final int startOffset = internalOffset(token);
            final int finishOffset = token.length() - internalPostfix(token);
            return tokenChars.subSequence(startOffset, finishOffset);
        }
        return tokenChars;
    }

    private int internalOffset(final Token<JavaTokenId> token) {
        return 1;
    }

    private int internalPostfix(final Token<JavaTokenId> token) {
        return 1;
    }
}
