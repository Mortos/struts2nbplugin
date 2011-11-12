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

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.beans.BeanInfo;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.completion.Completion;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Rohan Ranade
 */
public class FolderItem extends StrutsXMLConfigCompletionItem {

    private FileObject folder;

    public FolderItem(int substitutionOffset, FileObject folder) {
        super(substitutionOffset);
        this.folder = folder;
    }

    @Override
    public void processKeyEvent(KeyEvent evt) {
        if (evt.getID() == KeyEvent.KEY_TYPED) {
            if (evt.getKeyChar() == '/') { // NOI18N

                Completion.get().hideDocumentation();
                JTextComponent component = (JTextComponent) evt.getSource();
                int caretOffset = component.getSelectionEnd();
                substituteText(component, substitutionOffset, caretOffset - substitutionOffset, Character.toString(evt.getKeyChar()));
                Completion.get().showCompletion();
                evt.consume();
            }
        }
    }

    public int getSortPriority() {
        return 300;
    }

    public CharSequence getSortText() {
        return folder.getName();
    }

    public CharSequence getInsertPrefix() {
        return folder.getName();
    }

    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    @Override
    protected ImageIcon getIcon() {
        return new ImageIcon(getTreeFolderIcon());
    }

    @Override
    protected String getLeftHtmlText() {
        return folder.getName();
    }
    private static final String ICON_KEY_UIMANAGER = "Tree.closedIcon"; // NOI18N

    private static final String ICON_KEY_UIMANAGER_NB = "Nb.Explorer.Folder.icon"; // NOI18N


    /**
     * Returns default folder icon as {@link java.awt.Image}. Never returns
     * <code>null</code>.Adapted from J2SELogicalViewProvider
     */
    private static Image getTreeFolderIcon() {
        Image base = null;
        Icon baseIcon = UIManager.getIcon(ICON_KEY_UIMANAGER); // #70263

        if (baseIcon != null) {
            base = ImageUtilities.icon2Image(baseIcon);
        } else {
            base = (Image) UIManager.get(ICON_KEY_UIMANAGER_NB); // #70263

            if (base == null) { // fallback to our owns                

                final Node n = DataFolder.findFolder(Repository.getDefault().getDefaultFileSystem().getRoot()).getNodeDelegate();
                base = n.getIcon(BeanInfo.ICON_COLOR_16x16);
            }
        }
        assert base != null;
        return base;
    }
}
