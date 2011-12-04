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
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh
 */
public class XWorkJavaCompletionContext implements XWorkCompletionContext {

    private Document document;
    private FileObject file;
    private boolean valid = false;
    private int offset;
    private int endOffset;
    private CharSequence text;
    private int typedLength;
    private CharSequence typedText;

    public XWorkJavaCompletionContext(Document document, int caretOffset) throws BadLocationException {
        this.document = document;
        file = NbEditorUtilities.getFileObject(document);

        final TokenHierarchy<Document> tokenHierarchy = TokenHierarchy.get(document);
        final TokenSequence<JavaTokenId> tokenSequence = tokenHierarchy.tokenSequence(JavaTokenId.language());
        tokenSequence.move(caretOffset);
        if (tokenSequence.moveNext()) {
            final Token<JavaTokenId> token = tokenSequence.token();
            valid = true;
            initValue(tokenHierarchy, token, caretOffset);
        }
    }

    @Override
    public Document document() {
        return document;
    }

    @Override
    public int endOffset() {
        return endOffset;
    }

    @Override
    public FileObject file() {
        return file;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public int offset() {
        return offset;
    }

    @Override
    public CharSequence text() {
        return text;
    }

    @Override
    public int typedLength() {
        return typedLength;
    }

    @Override
    public CharSequence typedText() {
        return typedText;
    }

    private void initValue(
            final TokenHierarchy<Document> tokenHierarchy, final Token<JavaTokenId> token,
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
