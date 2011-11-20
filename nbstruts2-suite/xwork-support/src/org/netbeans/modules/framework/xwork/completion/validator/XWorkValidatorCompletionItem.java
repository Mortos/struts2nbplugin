package org.netbeans.modules.framework.xwork.completion.validator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.modules.framework.xwork.completion.resources.XWorkValidatorCompletionItemColors;
import org.netbeans.modules.framework.xwork.completion.resources.XWorkValidatorCompletionItemIcons;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorCompletionItem implements CompletionItem {

    private String text;
    private XWorkValidatorCompletionItemIcons icons;
    private XWorkValidatorCompletionItemColors colors;

    public XWorkValidatorCompletionItem(String text, XWorkValidatorCompletionItemIcons icons, XWorkValidatorCompletionItemColors colors) {
        this.text = text;
        this.icons = icons;
        this.colors = colors;
    }

    @Override
    public void defaultAction(JTextComponent textComponent) {
        try {
            Document document = textComponent.getDocument();
            int caretOffset = textComponent.getCaretPosition();

            XWorkValidatorCompletionContext context = new XWorkValidatorCompletionContext(document, caretOffset);
            int startOffset = context.offset();

            document.remove(startOffset, caretOffset - startOffset);
            document.insertString(startOffset, text, null);
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
        return CompletionUtilities.getPreferredWidth(text, null, graphics, font);
    }

    @Override
    public void render(Graphics graphics,
            Font defaultFont, Color defaultColor, Color backgroundColor,
            int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(icons.getImageIcon(), text, null,
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
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return null;
    }
}