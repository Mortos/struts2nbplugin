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

package org.netbeans.modules.web.frameworks.struts2.editor;

import javax.xml.XMLConstants;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.xml.text.api.XMLDefaultTokenContext;
import org.netbeans.modules.xml.text.syntax.SyntaxElement;
import org.netbeans.modules.xml.text.syntax.dom.EmptyTag;
import org.netbeans.modules.xml.text.syntax.dom.StartTag;
import org.netbeans.modules.xml.text.syntax.dom.Tag;

/**
 *
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public final class ContextUtilities {

    private ContextUtilities() {
    }
    
    public static final String P_NAMESPACE = ""; // NOI18N
    
    public static boolean isValueToken(TokenItem currentToken) {
        if(currentToken != null) {
            if (currentToken.getTokenID().getNumericID() == XMLDefaultTokenContext.VALUE_ID) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isTagToken(TokenItem currentToken) {
        if(currentToken != null) {
            if (currentToken.getTokenID().getNumericID() == XMLDefaultTokenContext.TAG_ID) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isAttributeToken(TokenItem currentToken) {
        if(currentToken != null) {
            if (currentToken.getTokenID().getNumericID() == XMLDefaultTokenContext.ARGUMENT_ID) {
                return true;
            }
        }
        
        return false;
    }
    
    public static TokenItem getAttributeToken(TokenItem currentToken) {
        if(isValueToken(currentToken)) {
            TokenItem equalsToken = currentToken.getPrevious();
            while(equalsToken.getTokenID().getNumericID() != XMLDefaultTokenContext.OPERATOR_ID) {
                equalsToken = equalsToken.getPrevious();
            }
        
            TokenItem argumentToken = equalsToken.getPrevious();
            while(argumentToken.getTokenID().getNumericID() != XMLDefaultTokenContext.ARGUMENT_ID) {
                argumentToken = argumentToken.getPrevious();
            }
        
            return argumentToken;
        }
        
        return null;
    }
  
    public static Tag getCurrentTagElement(DocumentContext context) {
        SyntaxElement element = context.getCurrentElement();
        if(element instanceof StartTag) {
            return (StartTag) element;
        } else if(element instanceof EmptyTag) {
            return (EmptyTag) element;
        }
        
        return null;
    }
    
    public static TokenItem getAttributeToken(DocumentContext context) {
        if(isValueToken(context.getCurrentToken())) {
            TokenItem equalsToken = context.getCurrentToken().getPrevious();
            while(equalsToken.getTokenID().getNumericID() != XMLDefaultTokenContext.OPERATOR_ID) {
                equalsToken = equalsToken.getPrevious();
            }
        
            TokenItem argumentToken = equalsToken.getPrevious();
            while(argumentToken.getTokenID().getNumericID() != XMLDefaultTokenContext.ARGUMENT_ID) {
                argumentToken = argumentToken.getPrevious();
            }
        
            return argumentToken;
        }
        
        return null;
    }
    
    public static String getAttributeTokenImage(DocumentContext context) {
        TokenItem tok = getAttributeToken(context);
        if(tok != null) {
            return tok.getImage();
        }
        
        return null;
    }
    
    /**
     * Returns the prefix from the element's tag.
     */
    public static String getPrefixFromTag(String tagName) {
        if(tagName == null) return null;
        return (tagName.indexOf(":") == -1) ? null : // NOI18N
            tagName.substring(0, tagName.indexOf(":")); // NOI18N
    }
    
    /**
     * Returns the local name from the element's tag.
     */
    public static String getLocalNameFromTag(String tagName) {
        if(tagName == null) return null;
        return (tagName.indexOf(":") == -1) ? tagName : // NOI18N
            tagName.substring(tagName.indexOf(":")+1, tagName.length()); // NOI18N
    }
    
    /**
     * Returns any prefix declared with this namespace. For example, if
     * the namespace was declared as xmlns:po, the prefix 'po' will be returned.
     * Returns null for declaration that contains no prefix.
     */
    public static String getPrefixFromNamespaceDeclaration(String namespace) {
        if (!namespace.startsWith(XMLConstants.XMLNS_ATTRIBUTE)) return null;
        int xmlnsLength = XMLConstants.XMLNS_ATTRIBUTE.length();
        if (namespace.length() == xmlnsLength) {
            return ""; // NOI18N
        }
        if (namespace.charAt(xmlnsLength) == ':') {
            return namespace.substring(xmlnsLength + 1);
        }
        return null;
    }
    
    public static String getPrefixFromNodeName(String nodeName) {
        int colonIndex = nodeName.indexOf(':');
        if (colonIndex <= 0) {
            return null;
        }
        return nodeName.substring(0, colonIndex);
    }
    
    public static StartTag getRoot(SyntaxElement se) {
        StartTag root = null;
        while( se != null) {
            if(se instanceof StartTag) {
                root = (StartTag)se;
            }
            se = se.getPrevious();
        }
        
        return root;
    }
}

