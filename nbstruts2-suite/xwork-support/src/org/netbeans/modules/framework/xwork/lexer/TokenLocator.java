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
package org.netbeans.modules.framework.xwork.lexer;

import javax.swing.text.AbstractDocument;
import org.netbeans.api.lexer.*;

/**
 * Token hierarchy visitor responsible for returning token at given offset.
 *
 * @author Aleh Maksimovich
 * @since 0.5.1
 */
/**
 * Inner class that initializes token hierarchy related fields.
 */
public class TokenLocator<T extends TokenId> implements TokenHierarchyVisitor<AbstractDocument> {

    private final Language<T> language;
    private final int caretOffset;
    private Token<T> token;

    public TokenLocator(Language<T> language, int caretOffset) {
        this.language = language;
        this.caretOffset = caretOffset;
    }

    @Override
    public void visit(TokenHierarchy<AbstractDocument> tokenHierarchy) throws TokenHierarchyVisitorException {
        // Get token sequence.
        final TokenSequence<T> tokenSequence = tokenHierarchy.tokenSequence(language);
        if (tokenSequence == null) {
            throw new TokenHierarchyVisitorException("Document doesn't support selected language.");
        }
        // Get current token.
        tokenSequence.move(caretOffset);
        if (!tokenSequence.moveNext()) {
            throw new TokenHierarchyVisitorException("Could not resolve current token.");
        }
        // Init parent fields.
        token = tokenSequence.offsetToken();
    }

    public Token<T> getToken() {
        return token;
    }
}
