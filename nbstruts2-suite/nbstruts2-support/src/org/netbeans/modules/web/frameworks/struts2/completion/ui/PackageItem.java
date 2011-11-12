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

import java.awt.event.KeyEvent;
import java.util.EnumSet;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.java.source.ui.ElementIcons;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
class PackageItem extends StrutsXMLConfigCompletionItem {

    private static final String PACKAGE_COLOR = "<font color=#005600>"; //NOI18N
    private static ImageIcon icon;
    private boolean deprecated;
    private String simpleName;
    private String sortText;
    private String leftText;

    public PackageItem(int substitutionOffset, String packageFQN, boolean deprecated) {
        super(substitutionOffset);
        int idx = packageFQN.lastIndexOf('.'); // NOI18N
        this.simpleName = idx < 0 ? packageFQN : packageFQN.substring(idx + 1);
        this.deprecated = deprecated;
        this.sortText = this.simpleName + "#" + packageFQN; //NOI18N
    }

    public int getSortPriority() {
        return 50;
    }

    public CharSequence getSortText() {
        return sortText;
    }

    public CharSequence getInsertPrefix() {
        return simpleName;
    }

    @Override
    public void processKeyEvent(KeyEvent evt) {
        if (evt.getID() == KeyEvent.KEY_TYPED) {
            if (evt.getKeyChar() == '.') { // NOI18N
                Completion.get().hideDocumentation();
                JTextComponent component = (JTextComponent) evt.getSource();
                int caretOffset = component.getSelectionEnd();
                substituteText(component, substitutionOffset, caretOffset - substitutionOffset, Character.toString(evt.getKeyChar()));
                Completion.get().showCompletion();
                evt.consume();
            }
        }
    }

    @Override
    protected ImageIcon getIcon() {
        if (icon == null) {
            icon = (ImageIcon) ElementIcons.getElementIcon(
                    ElementKind.PACKAGE, EnumSet.noneOf(Modifier.class));
        }
        return icon;
    }

    @Override
    protected String getLeftHtmlText() {
        if (leftText == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(PACKAGE_COLOR);
            if (deprecated) {
                sb.append(STRIKE);
            }
            sb.append(simpleName);
            if (deprecated) {
                sb.append(STRIKE_END);
            }
            sb.append(COLOR_END);
            leftText = sb.toString();
        }
        return leftText;
    }
}
