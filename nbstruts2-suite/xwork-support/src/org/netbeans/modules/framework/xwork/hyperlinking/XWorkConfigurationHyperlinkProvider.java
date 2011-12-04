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
package org.netbeans.modules.framework.xwork.hyperlinking;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletionContext;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
@MimeRegistration(mimeType = "text/x-xwork-validator-config+xml", service = HyperlinkProvider.class)
public class XWorkConfigurationHyperlinkProvider implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int caretOffset) {
        try {
            XWorkCompletionContext context = new XWorkCompletionContext(document, caretOffset);
            return XWorkConfigurationHyperlinkActionFactory.isRegisteredHyperlinkPoint(context);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int caretOffset) {
        try {
            XWorkCompletionContext context = new XWorkCompletionContext(document, caretOffset);
            return new int[]{context.offset(), context.endOffset()};
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new int[]{caretOffset, caretOffset};
    }

    @Override
    public void performClickAction(Document document, int caretOffset) {
        try {
            XWorkCompletionContext context = new XWorkCompletionContext(document, caretOffset);
            XWorkConfigurationHyperlinkActionFactory.hyperlinkAction(context);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
