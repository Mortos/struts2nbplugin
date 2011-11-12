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

import java.util.EnumSet;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.ui.ElementIcons;
import org.netbeans.modules.web.frameworks.struts2.utils.JavaUtils;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * Represents a class in the completion popup. (Based on spring impl)
 * 
 * Heavily derived from Java Editor module's JavaCompletionItem class
 * 
 */
class TypeItem extends StrutsXMLConfigCompletionItem {

    private static final String CLASS_COLOR = "<font color=#560000>"; //NOI18N
    private static final String PKG_COLOR = "<font color=#808080>"; //NOI18N
    private ElementHandle<TypeElement> elemHandle;
    private ElementKind elementKind;
    private boolean deprecated;
    private String displayName;
    private String enclName;
    private String sortText;
    private String leftText;
    private boolean smartItem;
    private ImageIcon icon;

    public TypeItem(int substitutionOffset, TypeElement elem, ElementHandle<TypeElement> elemHandle,
            boolean deprecated, boolean smartItem) {
        super(substitutionOffset);
        this.elemHandle = elemHandle;
        this.elementKind = elem.getKind();
        this.deprecated = deprecated;
        this.displayName = smartItem ? elem.getSimpleName().toString() : getRelativeName(elem);
        this.enclName = JavaUtils.getElementName(elem.getEnclosingElement(), true).toString();
        this.sortText = this.displayName + JavaUtils.getImportanceLevel(this.enclName) + "#" + this.enclName; //NOI18N
        this.smartItem = smartItem;
    }

    private String getRelativeName(TypeElement elem) {
        StringBuilder sb = new StringBuilder();
        sb.append(elem.getSimpleName().toString());
        Element parent = elem.getEnclosingElement();
        while (parent.getKind() != ElementKind.PACKAGE) {
            sb.insert(0, parent.getSimpleName().toString() + "$"); // NOI18N
            parent = parent.getEnclosingElement();
        }

        return sb.toString();
    }

    public int getSortPriority() {
        return 200;
    }

    public CharSequence getSortText() {
        return sortText;
    }

    public CharSequence getInsertPrefix() {
        return smartItem ? "" : elemHandle.getBinaryName(); // NOI18N
    }

    @Override
    protected CharSequence getSubstitutionText() {
        return elemHandle.getBinaryName();
    }

    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    @Override
    protected String getLeftHtmlText() {
        if (leftText == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getColor());
            if (deprecated) {
                sb.append(STRIKE);
            }
            sb.append(displayName);
            if (deprecated) {
                sb.append(STRIKE_END);
            }
            if (smartItem && enclName != null && enclName.length() > 0) {
                sb.append(COLOR_END);
                sb.append(PKG_COLOR);
                sb.append(" ("); //NOI18N
                sb.append(enclName);
                sb.append(")"); //NOI18N
            }
            sb.append(COLOR_END);
            leftText = sb.toString();
        }
        return leftText;
    }

    protected String getColor() {
        return CLASS_COLOR;
    }

    @Override
    protected ImageIcon getIcon() {
        if(icon == null) {
            icon = (ImageIcon) ElementIcons.getElementIcon(elementKind, EnumSet.noneOf(Modifier.class));
        }
        return icon;
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return new AsyncCompletionTask(new JavaElementDocQuery(elemHandle), EditorRegistry.lastFocusedComponent());
    }
}
