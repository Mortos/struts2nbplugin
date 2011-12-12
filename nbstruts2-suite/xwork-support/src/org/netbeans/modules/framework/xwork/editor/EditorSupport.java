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
import org.openide.filesystems.FileObject;

/**
 * Generic document editing information. The interface declares methods helpful
 * with providing completion and hyper linking for any text document. The
 * interface operates with editor document and backing file, provided typed text
 * information together with major region offsets and lengths.
 *
 * @author Aleh Maksimovich
 * @since 0.5.1
 */
public interface EditorSupport {

    /**
     * Specified whether the support class was correctly initialized. Instance
     * of EditorSupport could be used only is this method returns <i>true</i>.
     *
     * @return <i>true</i> when support instance is correctly initialized.
     */
    boolean isValid();

    /**
     * Returns source document for this support object.
     *
     * @return source document.
     */
    Document getDocument();

    /**
     * Returns file object corresponding to source document.
     *
     * @return source document file object.
     */
    FileObject getFileObject();

    /**
     * Returns block outer content. Returned text will include all special
     * characters that forego and follow block's text. Basically this method
     * returns the portion of document between
     * {@link #getOuterStartOffset() outerStartOffset} and
     * {@link #getOuterEndOffset() outerEndOffset}.
     *
     * @return block's outer content.
     */
    String getOuterContent();

    /**
     * Returns block inner content. Returned text will not include any special
     * characters that forego and follow block's text. Basically this method
     * returns the portion of document between
     * {@link #getInnerStartOffset() innerStartOffset} and
     * {@link #getInnerEndOffset() innerEndOffset}.
     *
     * @return block's inner content.
     */
    String getInnerContent();

    /**
     * Returns part of inner content lying to the left of current caret
     * position. Basically this method returns the portion of document between
     * {@link #getInnerStartOffset() innerStartOffset} and
     * {@link #getCaretOffset() caretOffset}.
     *
     * @return block's left inner content.
     */
    String getLeftContent();

    /**
     * Returns part of inner content lying to the right of current caret
     * position. Basically this method returns the portion of document between
     * {@link EditorSupport#getCaretOffset() caretOffset} and
     * {@link EditorSupport#getInnerEndOffset() innerEndOffset}.
     *
     * @return block's right inner content.
     */
    String getRightContent();

    /**
     * Returns the offset of the first symbol in the block, counting in any
     * special markup characters.
     *
     * @return outer content start offset.
     */
    int getOuterStartOffset();

    /**
     * Returns the offset of the first symbol in the block inner content, this
     * doesn't include any special markup characters.
     *
     * @return inner content start offset.
     */
    int getInnerStartOffset();

    /**
     * Returns current caret position.
     *
     * @return current caret offset.
     */
    int getCaretOffset();

    /**
     * Returns the offset of the last symbol in the block inner content, this
     * doesn't include any special markup characters.
     *
     * @return inner content end offset.
     */
    int getInnerEndOffset();

    /**
     * Returns the offset of the last symbol in the block, counting in any
     * special markup characters.
     *
     * @return outer content end offset.
     */
    int getOuterEndOffset();

    /**
     * Returns the length of the {@link #getOuterContent() outer content}.
     *
     * @return outer content length.
     */
    int getOuterLength();

    /**
     * Returns the length of the {@link #getInnerContent() inner content}.
     *
     * @return inner content length.
     */
    int getInnerLength();

    /**
     * Returns the length of the {@link #getLeftContent() left content}.
     *
     * @return left content length.
     */
    int getLeftContentLength();

    /**
     * Returns the length of the {@link #getRightContent() right content}.
     *
     * @return right content length.
     */
    int getRightContentLength();
}
