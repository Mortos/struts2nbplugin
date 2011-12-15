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
package org.netbeans.modules.framework.xwork.editor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AbstractDocument;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.modules.framework.xwork.lexer.TokenHierarchyVisitor;
import org.netbeans.modules.framework.xwork.lexer.TokenHierarchyVisitorException;
import org.netbeans.modules.framework.xwork.lexer.TokenLocator;

/**
 * {@link EditorSupport EditorSupport} abstract implementation that uses
 * {@link org.netbeans.api.lexer Lexer API} for document parsing.
 *
 * @author Aleh Maksimovich
 * @since 0.5.1
 */
public abstract class AbstractLexerEditorSupport<T extends TokenId> extends AbstractTextEditorSupport {

    private static final Logger LOG = Logger.getLogger(AbstractLexerEditorSupport.class.getName());
    private boolean valid = false;
    private final Language<T> language;
    private TokenHierarchy<AbstractDocument> tokenHierarchy;
    private Token<T> token;
    private String outerContent;

    public AbstractLexerEditorSupport(Language<T> language, AbstractDocument document, int caretOffset) {
        super(document, caretOffset);
        this.language = language;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // Initialize parent.
        super.init();
        if (!super.isValid()) {
            return; // Parent initialization has failed.
        }

        // Initialize lexer context.
        AbstractDocument document = getDocument();
        tokenHierarchy = TokenHierarchy.get(document);
        try {
            TokenLocator<T> tokenLocator = new TokenLocator<T>(language, getCaretOffset());
            visitHierarchy(tokenLocator);
            token = tokenLocator.getToken();
        } catch (TokenHierarchyVisitorException ex) {
            LOG.log(Level.WARNING, null, ex);
            return; // Token wasn't resolved.
        }
        outerContent = token.text().toString();

        // Initialization is complete. Context is valid.
        valid = true;
    }

    /**
     * Hierarchy operations must be done under read lock. Wraps token hierarchy
     * operations into read lock session.
     *
     * @param visitor operation to visit on local token hierarchy.
     *
     * @throws TokenHierarchyVisitorException visitor's exception.
     */
    public void visitHierarchy(TokenHierarchyVisitor<AbstractDocument> visitor)
            throws TokenHierarchyVisitorException {
        AbstractDocument document = getDocument();
        document.readLock();

        try {
            visitor.visit(tokenHierarchy);
        } finally {
            document.readUnlock();
        }
    }

    /**
     * Returns used {@link Language Language}.
     *
     * @return lexer language.
     */
    public Language<T> getLanguage() {
        return language;
    }

    /**
     * Token at caret offset.
     *
     * @return current token.
     */
    public Token<T> getToken() {
        return token;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return valid;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public AbstractDocument getDocument() {
        return (AbstractDocument) super.getDocument();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String getOuterContent() {
        return outerContent;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getOuterStartOffset() {
        return token.offset(tokenHierarchy);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getOuterEndOffset() {
        return getOuterStartOffset() + getOuterLength();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getOuterLength() {
        return token.length();
    }
}
