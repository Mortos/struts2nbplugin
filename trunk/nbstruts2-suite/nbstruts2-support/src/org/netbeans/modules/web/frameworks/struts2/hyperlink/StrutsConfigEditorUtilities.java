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

package org.netbeans.modules.web.frameworks.struts2.hyperlink;

import org.netbeans.editor.TokenItem;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsQNames;
import org.netbeans.modules.xml.text.api.XMLDefaultTokenContext;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.netbeans.modules.xml.text.syntax.dom.TextImpl;
import org.netbeans.modules.xml.text.syntax.dom.SyntaxNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Sujit Nair
 */
public final class StrutsConfigEditorUtilities {

    private StrutsConfigEditorUtilities() {
    }

    public static boolean isValueToken(TokenItem tok) {
        if (tok != null && tok.getTokenID().getNumericID() == XMLDefaultTokenContext.VALUE_ID) {
            return true;
        }

        return false;
    }

    public static boolean isTextToken(TokenItem tok) {
        if (tok != null && tok.getTokenID().getNumericID() == XMLDefaultTokenContext.TEXT_ID) {
            return true;
        }
        return false;
    }

    public static TokenItem getAttributeToken(TokenItem valueToken) {
        if (!isValueToken(valueToken)) {
            return null;
        }
        TokenItem currToken = valueToken;
        while ((currToken != null) && (currToken.getTokenID().getNumericID() != XMLDefaultTokenContext.ARGUMENT_ID)) {
            currToken = currToken.getPrevious();
        }

        return currToken;
    }

    // Function to return the name of the package to which the element/token belongs.
    public static String getPackageName(SyntaxNode currentTag) {
        String packageName = null;
        Node iteratorNode = null;
        String trimString = null;

        if (currentTag instanceof Tag) {
            currentTag = (Tag) currentTag;
        } else if (currentTag instanceof TextImpl) {
            currentTag = (TextImpl) currentTag;
            trimString = currentTag.getNodeValue().trim();
            if (trimString.equals("")) {
                trimString = null;
            }
        }

            
        if (trimString == null)  {
            iteratorNode = (Node) currentTag;
        }


        while (iteratorNode != null) {
            if (iteratorNode.getNodeName().equals(StrutsQNames.PACKAGE.getLocalName())) {
                NamedNodeMap attList = iteratorNode.getAttributes();
                Node attName = attList.getNamedItem("name");
                packageName = attName.getNodeValue();
                break;
            } else {
                iteratorNode = iteratorNode.getParentNode();
            }
        }

        return packageName;
    }
}
