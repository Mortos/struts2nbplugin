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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsQNames;
import org.netbeans.modules.xml.text.syntax.SyntaxElement;
import org.netbeans.modules.xml.text.syntax.XMLSyntaxSupport;
import org.netbeans.modules.xml.text.syntax.dom.EmptyTag;
import org.netbeans.modules.xml.text.syntax.dom.StartTag;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
public class StrutsXMLUtils {

    /**
     * The method returns the reference to the root node in the XML file (Reference to Tag 'struts')
     * @param doc
     * @return
     */
    public static Tag getDocumentRoot(Document doc) {
        Tag retTag = null;
        try {
            XMLSyntaxSupport syntaxSupport = new XMLSyntaxSupport((BaseDocument) doc);
            TokenItem tok = syntaxSupport.getTokenChain(0, 1);
            if (tok != null) {
                while (!ContextUtilities.isTagToken(tok)) {
                    tok = tok.getNext();
                }
                SyntaxElement element = syntaxSupport.getElementChain(tok.getOffset() + tok.getImage().length());
                if (element instanceof StartTag || element instanceof EmptyTag) {
                    Tag tag = (Tag) element;
                    if (tag.getParentNode() instanceof org.w3c.dom.Document) {
                        return tag;
                    }
                }
            }
        } catch (BadLocationException ex) {
        // No context support available in this case
        }

        return retTag;
    }

    /**
     * This is a common method used to return a list of elements present withing a tag.
     * @param root
     * @param elementName
     * @return
     */
    public static List<Node> getElementsByName(Node root, String elementName) {
        NodeList list = null;
        List<Node> arrTag = new ArrayList<Node>();
        if (root != null) {
            list = root.getChildNodes();
        }
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(elementName)) {
                arrTag.add(n);
            }
        }

        return arrTag;
    }

    public static Node getPackageTag(Node startTag) {
        Node currTag = startTag;
        while (currTag != null) {
            String nodeName = currTag.getNodeName();
            if (nodeName == null) {
                break;
            }

            if (nodeName.equals(StrutsQNames.PACKAGE.getLocalName())) {
                return currTag;
            }

            currTag = currTag.getParentNode();
        }

        return null;
    }

    public static Tag getTag(Document document, int offset) {
        DocumentContext context = DocumentContext.createContext(document, offset);
        if (context == null) {
            return null;
        }

        SyntaxElement se = context.getCurrentElement();
        if (se == null) {
            return null;
        }

        while (se != null) {
            if (se instanceof StartTag || se instanceof EmptyTag) {
                return (Tag) se;
            }

            se = se.getPrevious();
        }

        return null;
    }

    public static List<FileObject> getVisibleIncludes(Document document, int offset) {
        FileObject parentDir = NbEditorUtilities.getFileObject(document).getParent();

        Tag root = getDocumentRoot(document);
        if (root == null) {
            return Collections.<FileObject>emptyList();
        }

        List<FileObject> incFiles = new ArrayList<FileObject>();
        List<Node> allIncludes = getElementsByName(root, StrutsQNames.INCLUDE.getLocalName());
        for (Node n : allIncludes) {
            Tag tag = (Tag) n;
            if (tag.getElementOffset() < offset && hasAttribute(tag, StrutsAttributes.FILE.getName())) {
                String filePath = getAttribute(tag, StrutsAttributes.FILE.getName());
                FileObject incFile = parentDir.getFileObject(filePath);
                if (incFile != null) {
                    incFiles.add(incFile);
                }
            }
        }

        return Collections.unmodifiableList(incFiles);
    }

    public static List<Node> getVisiblePackages(Document document, int offset) {
        Tag rootTag = getDocumentRoot(document);
        if (rootTag == null) {
            return Collections.<Node>emptyList();
        }

        List<Node> allPackages = getElementsByName(rootTag, StrutsQNames.PACKAGE.getLocalName());
        if (allPackages.isEmpty()) {
            return Collections.<Node>emptyList();
        }

        List<Node> visiblePackages = new ArrayList<Node>();
        for (Node currTag : allPackages) {
            Tag tag = (Tag) currTag;
            if (tag.getElementOffset() < offset) {
                visiblePackages.add(currTag);
            }
        }

        return Collections.<Node>unmodifiableList(visiblePackages);
    }

    public static String escape(String s) {
        if (s != null) {
            try {
                return XMLUtil.toAttributeValue(s);
            } catch (Exception ex) {
            }
        }
        return s;
    }

    public static final boolean hasAttribute(Node node, String attributeName) {
        return (node != null && node.getAttributes() != null && node.getAttributes().getNamedItem(attributeName) != null);
    }

    public static final String getAttribute(Node node, String attributeName) {
        if (hasAttribute(node, attributeName)) {
            return node.getAttributes().getNamedItem(attributeName).getNodeValue();
        }
        return null;
    }
}
