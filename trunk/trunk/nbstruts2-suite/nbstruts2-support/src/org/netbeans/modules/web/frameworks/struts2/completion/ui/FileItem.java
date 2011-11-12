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

package org.netbeans.modules.web.frameworks.struts2.completion.ui;

import javax.swing.ImageIcon;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Rohan Ranade
 */
public class FileItem extends StrutsXMLConfigCompletionItem {

    private FileObject file;

    public FileItem(int substitutionOffset, FileObject file) {
        super(substitutionOffset);
        this.file = file;
    }

    public int getSortPriority() {
        return 100;
    }

    public CharSequence getSortText() {
        return file.getNameExt();
    }

    public CharSequence getInsertPrefix() {
        return file.getNameExt();
    }

    @Override
    protected ImageIcon getIcon() {
        return new ImageIcon(ImageUtilities.loadImage(
                "org/netbeans/modules/web/frameworks/struts2/resources/StrutsConfigIcon.png")); // NOI18N

    }

    @Override
    protected String getLeftHtmlText() {
        return file.getNameExt();
    }
}
