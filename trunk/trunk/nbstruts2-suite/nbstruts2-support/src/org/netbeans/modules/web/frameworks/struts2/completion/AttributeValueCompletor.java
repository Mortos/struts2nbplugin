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

package org.netbeans.modules.web.frameworks.struts2.completion;

import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple completor for general attribute value items
 * 
 * Takes an array of strings, the even elements being the display text of the items
 * and the odd ones being the corresponding documentation of the items
 *  
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public class AttributeValueCompletor extends Completor {

    private String[] itemTextAndDocs;

    public AttributeValueCompletor(String[] itemTextAndDocs) {
        this.itemTextAndDocs = itemTextAndDocs;
    }

    public List<StrutsXMLConfigCompletionItem> doCompletion(CompletionContext context) {
        List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        int caretOffset = context.getCaretOffset();
        String typedChars = context.getTypedPrefix();

        for (int i = 0; i < itemTextAndDocs.length; i += 2) {
            if (itemTextAndDocs[i].startsWith(typedChars)) {
                StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createAttribValueItem(caretOffset - typedChars.length(),
                        itemTextAndDocs[i], itemTextAndDocs[i + 1]);
                results.add(item);
            }
        }

        setAnchorOffset(context.getCurrentToken().getOffset() + 1);
        return results;
    }
}
