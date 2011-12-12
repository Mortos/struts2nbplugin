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

import javax.swing.text.Document;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 * Base class for {@link EditorSupport EditorSupport} implementations. Provides
 * document type agnostic implementation of interface methods.
 *
 * @author Aleh Maksimovich
 * @since 0.5.1
 */
public abstract class AbstractTextEditorSupport implements EditorSupport {

    private boolean valid = false;
    private final Document document;
    private FileObject fileObject;
    private final int caretOffset;

    /**
     * Constructor.
     *
     * @param document target document.
     * @param caretOffset caret offset within target document.
     *
     * @throws IllegalArgumentException when document is null
     * @throws IndexOutOfBoundsException when caret offset doesn't correspond to
     * a location within the document
     */
    public AbstractTextEditorSupport(Document document, int caretOffset) {
        // Validate method arguments
        if (document == null) {
            throw new IllegalArgumentException("Document should not be null.");
        }
        if (caretOffset < 0 || caretOffset > document.getLength()) {
            throw new IndexOutOfBoundsException("Caret offset outside of the document.");
        }

        // Assign arguments
        this.document = document;
        this.caretOffset = caretOffset;
    }

    /**
     * Initializes current object. Current instance is invalid until this method
     * executes successfully.
     */
    public void init() {
        // Resolve file object
        fileObject = NbEditorUtilities.getFileObject(document);
        if (fileObject == null) {
            return; // Failed to resolve file object.
        }

        // Initialization is complete. Context is valid.
        valid = true;
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
    public Document getDocument() {
        return document;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public FileObject getFileObject() {
        return fileObject;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getCaretOffset() {
        return caretOffset;
    }
}
