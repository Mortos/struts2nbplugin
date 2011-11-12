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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.netbeans.modules.web.frameworks.struts2.Struts2DataLoader;
import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Rohan.Ranade@Sun.COM
 */
public class IncludeFileCompletor extends Completor {

    public IncludeFileCompletor() {
    }

    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(CompletionContext context) {
        List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        FileObject fileObject = context.getFileObject().getParent();
        String typedChars = context.getTypedPrefix();

        int lastSlashIndex = typedChars.lastIndexOf("/"); // NOI18N

        String prefix = typedChars;

        if (lastSlashIndex != -1) {
            String pathStr = typedChars.substring(0, typedChars.lastIndexOf("/")); // NOI18N

            fileObject = fileObject.getFileObject(pathStr);
            if (lastSlashIndex != typedChars.length() - 1) {
                prefix = typedChars.substring(Math.min(typedChars.lastIndexOf("/") + 1, // NOI18N
                        typedChars.length() - 1));
            } else {
                prefix = "";
            }
        }

        if (fileObject == null) {
            return Collections.emptyList();
        }

        if (prefix == null) {
            prefix = "";
        }

        Enumeration<? extends FileObject> folders = fileObject.getFolders(false);
        while (folders.hasMoreElements()) {
            FileObject fo = folders.nextElement();
            if (fo.getName().startsWith(prefix)) {
                results.add(StrutsXMLConfigCompletionItem.createFolderItem(context.getCaretOffset() - prefix.length(),
                        fo));
            }
        }

        Enumeration<? extends FileObject> files = fileObject.getData(false);
        while (files.hasMoreElements()) {
            FileObject fo = files.nextElement();
            if (fo.getName().startsWith(prefix) && Struts2DataLoader.REQUIRED_MIME.equals(fo.getMIMEType())) {
                results.add(StrutsXMLConfigCompletionItem.createSpringXMLFileItem(context.getCaretOffset() - prefix.length(), fo));
            }
        }

        setAnchorOffset(context.getCaretOffset() - prefix.length());
        return results;
    }
}


