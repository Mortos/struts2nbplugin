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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.modules.framework.xwork.completion.resource.XWorkTextColors;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public class XWorkCompletionItem implements CompletionItem {

    private String caption;
    private String completion;
    private ImageIcon imageIcon;
    private XWorkTextColors colors;
    private XWorkCompletionContext context;

    public XWorkCompletionItem(XWorkCompletionContext context, String text, ImageIcon imageIcon, XWorkTextColors colors) {
        this.context = context;
        this.caption = this.completion = text;
        this.imageIcon = imageIcon;
        this.colors = colors;
    }

    public XWorkCompletionItem(XWorkCompletionContext context, String caption, String completion, ImageIcon imageIcon, XWorkTextColors colors) {
        this.context = context;
        this.caption = caption;
        this.completion = completion;
        this.imageIcon = imageIcon;
        this.colors = colors;
    }

    protected String getCaption() {
        return caption;
    }

    protected void setCaption(String caption) {
        this.caption = caption;
    }

    protected XWorkTextColors getColors() {
        return colors;
    }

    protected void setColors(XWorkTextColors colors) {
        this.colors = colors;
    }

    protected String getCompletion() {
        return completion;
    }

    protected void setCompletion(String completion) {
        this.completion = completion;
    }

    protected ImageIcon getImageIcon() {
        return imageIcon;
    }

    protected void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public void defaultAction(JTextComponent textComponent) {
        try {
            Document document = textComponent.getDocument();
            int caretOffset = textComponent.getCaretPosition();

            int startOffset = context.offset();

            document.remove(startOffset, caretOffset - startOffset);
            document.insertString(startOffset, completion, null);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            Completion.get().hideAll();
        }
    }

    @Override
    public void processKeyEvent(KeyEvent keyEvent) {
    }

    @Override
    public int getPreferredWidth(Graphics graphics, Font font) {
        return CompletionUtilities.getPreferredWidth(caption, null, graphics, font);
    }

    @Override
    public void render(Graphics graphics,
            Font defaultFont, Color defaultColor, Color backgroundColor,
            int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(imageIcon, caption, null,
                graphics, defaultFont, colors.foreColor(defaultColor, selected),
                width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return null;
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent jtc) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return caption;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XWorkCompletionItem other = (XWorkCompletionItem) obj;
        if ((this.completion == null) ? (other.completion != null) : !this.completion.equals(other.completion)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.completion != null ? this.completion.hashCode() : 0);
        return hash;
    }
}