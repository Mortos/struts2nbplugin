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
 * The Original Software is XWork Support. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.sp;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.netbeans.modules.framework.xwork.XWorkMimeType;
import org.netbeans.modules.framework.xwork.completion.XMLEditorSupport;
import org.netbeans.modules.framework.xwork.hyperlinking.XWorkConfigurationHyperlinkActionFactory;

/**
 *
 * @author Aleh
 */
@MimeRegistration(mimeType = XWorkMimeType.VALIDATOR_CONFIG_XML_MIME, service = HyperlinkProvider.class)
public class ValidatorConfigHyperlinkProvider implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int caretOffset) {
        JTextComponent target = EditorRegistry.lastFocusedComponent();
        final StyledDocument styledDoc = (StyledDocument) target.getDocument();
        if (styledDoc == null) {
            return false;
        }

        // Work only with the open editor 
        //and the editor has to be the active component:
        if ((target == null) || (target.getDocument() != document)) {
            return false;
        }

        XMLEditorSupport context = new XMLEditorSupport((AbstractDocument) document, caretOffset);
        context.init();
        return XWorkConfigurationHyperlinkActionFactory.isRegisteredHyperlinkPoint(context);
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int caretOffset) {
        XMLEditorSupport context = new XMLEditorSupport((AbstractDocument) document, caretOffset);
        context.init();
        return new int[]{context.getInnerStartOffset(), context.getInnerEndOffset()};
    }

    @Override
    public void performClickAction(Document document, int caretOffset) {
        XMLEditorSupport context = new XMLEditorSupport((AbstractDocument) document, caretOffset);
        context.init();
        XWorkConfigurationHyperlinkActionFactory.hyperlinkAction(context);
    }
}
