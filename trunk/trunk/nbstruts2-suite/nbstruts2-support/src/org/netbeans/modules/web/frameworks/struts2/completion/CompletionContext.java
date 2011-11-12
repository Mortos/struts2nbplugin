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

import java.util.List;
import javax.swing.text.Document;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.web.frameworks.struts2.editor.DocumentContext;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Node;

/**
 *
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public interface CompletionContext {
    
    public enum CompletionType {
        VALUE,
        ATTRIBUTE,
        ATTRIBUTE_VALUE,
        TAG,
        NONE
    };
    
    public CompletionType getCompletionType();

    public String getTypedPrefix();

    public Document getDocument();

    public DocumentContext getDocumentContext();

    public int getCaretOffset();

    public Node getTag();

    public TokenItem getCurrentToken();

    public List<String> getExistingAttributes();
    
    public FileObject getFileObject();
}

