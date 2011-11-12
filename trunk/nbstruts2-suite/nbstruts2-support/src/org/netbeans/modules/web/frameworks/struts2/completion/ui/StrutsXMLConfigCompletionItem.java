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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.editor.BaseDocument;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
public abstract class StrutsXMLConfigCompletionItem implements CompletionItem {

    public static final String COLOR_END = "</font>"; //NOI18N
    public static final String STRIKE = "<s>"; //NOI18N
    public static final String STRIKE_END = "</s>"; //NOI18N
    public static final String BOLD = "<b>"; //NOI18N
    public static final String BOLD_END = "</b>"; //NOI18N

    public static StrutsXMLConfigCompletionItem createAttribValueItem(int substitutionOffset, String displayText, String docText) {
        return new AttribValueItem(substitutionOffset, displayText, docText);
    }

    public static StrutsXMLConfigCompletionItem createPackageItem(int substitutionOffset, String packageName,
            boolean deprecated) {
        return new PackageItem(substitutionOffset, packageName, deprecated);
    }

    public static StrutsXMLConfigCompletionItem createTypeItem(int substitutionOffset, TypeElement elem, ElementHandle<TypeElement> elemHandle,
            boolean deprecated, boolean smartItem) {
        return new TypeItem(substitutionOffset, elem, elemHandle, deprecated, smartItem);
    }

    public static StrutsXMLConfigCompletionItem createMethodItem(int substitutionOffset, ExecutableElement element,
            boolean isInherited, boolean isDeprecated) {
        return new MethodItem(substitutionOffset, element, isInherited, isDeprecated);
    }

    public static StrutsXMLConfigCompletionItem createInterceptorRefItem(int substitutionOffset, String displayName,
            String typeClass) {
        return new InterceptorRefItem(substitutionOffset, displayName, typeClass);
    }

    public static StrutsXMLConfigCompletionItem createFolderItem(int substitutionOffset, FileObject folder) {
        return new FolderItem(substitutionOffset, folder);
    }

    public static StrutsXMLConfigCompletionItem createSpringXMLFileItem(int substitutionOffset, FileObject file) {
        return new FileItem(substitutionOffset, file);
    }

    public static StrutsXMLConfigCompletionItem createDefaultInterceptorRefItem(int substitutionOffset, String displayName,
            String typeClass) {
        return new DefaultInterceptorRefItem(substitutionOffset, displayName, typeClass);
    }

    public static StrutsXMLConfigCompletionItem createDefaultActionRefItem(int substitutionOffset, String displayName,
            String typeClass) {
        return new DefaultActionRefItem(substitutionOffset, displayName, typeClass);
    }

    public static StrutsXMLConfigCompletionItem createResultTypeItem(int substitutionOffset, String displayName,
            String typeClass) {
        return new ResultTypeItem(substitutionOffset, displayName, typeClass);
    }

    public static StrutsXMLConfigCompletionItem createResultItem(int substitutionOffset, String displayName,
            String typeClass) {
        return new ResultItem(substitutionOffset, displayName, typeClass);
    }

    public static StrutsXMLConfigCompletionItem createStrutsPackageItem(int substitutionOffset, String displayName, FileObject file) {
        return new StrutsPackageItem(substitutionOffset, displayName, file);
    }
    protected int substitutionOffset;

    protected StrutsXMLConfigCompletionItem(int substitutionOffset) {
        this.substitutionOffset = substitutionOffset;
    }

    public void defaultAction(JTextComponent component) {
        if (component != null) {
            Completion.get().hideDocumentation();
            Completion.get().hideCompletion();
            int caretOffset = component.getSelectionEnd();
            substituteText(component, substitutionOffset, caretOffset - substitutionOffset, null);
        }
    }

    protected void substituteText(JTextComponent c, final int offset, final int len, String toAdd) {
        final BaseDocument doc = (BaseDocument) c.getDocument();
        CharSequence prefix = getSubstitutionText();
        String text = prefix.toString();
        if (toAdd != null) {
            text += toAdd;
        }

        final String sText = text;
        doc.runAtomic(new Runnable() {

            public void run() {
                try {
                    Position position = doc.createPosition(offset);
                    doc.remove(offset, len);
                    doc.insertString(position.getOffset(), sText.toString(), null);
                } catch (BadLocationException ble) {
                    // nothing can be done to update
                }
            }
        });
    }

    protected CharSequence getSubstitutionText() {
        return getInsertPrefix();
    }

    public void processKeyEvent(KeyEvent evt) {

    }

    public int getPreferredWidth(Graphics g, Font defaultFont) {
        return CompletionUtilities.getPreferredWidth(getLeftHtmlText(),
                getRightHtmlText(), g, defaultFont);
    }

    public void render(Graphics g, Font defaultFont, Color defaultColor,
            Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(getIcon(), getLeftHtmlText(),
                getRightHtmlText(), g, defaultFont, defaultColor, width, height, selected);
    }

    public CompletionTask createDocumentationTask() {
        return null;
    }

    public CompletionTask createToolTipTask() {
        return null;
    }

    public boolean instantSubstitution(JTextComponent component) {
        defaultAction(component);
        return true;
    }

    protected String getLeftHtmlText() {
        return null;
    }

    protected String getRightHtmlText() {
        return null;
    }

    protected ImageIcon getIcon() {
        return null;
    }
}
