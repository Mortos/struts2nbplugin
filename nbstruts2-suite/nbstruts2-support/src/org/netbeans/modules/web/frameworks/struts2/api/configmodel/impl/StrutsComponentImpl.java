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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl;

import java.util.List;
import javax.xml.namespace.QName;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.xml.xam.Nameable;
import org.netbeans.modules.xml.xam.dom.AbstractDocumentComponent;
import org.netbeans.modules.xml.xam.dom.Attribute;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Rohan Ranade
 */
public abstract class StrutsComponentImpl extends AbstractDocumentComponent<StrutsComponent> 
        implements StrutsComponent {

    public StrutsComponentImpl(StrutsModelImpl model, Element element) {
        super(model, element);
    }

    @Override
    public StrutsModelImpl getModel() {
        return (StrutsModelImpl) super.getModel();
    }
    
    static public Element createElementNS(StrutsModel model, StrutsQNames sq) {
        QName q = sq.getQName();
        return model.getDocument().createElementNS(q.getNamespaceURI(), sq.getQualifiedName());
    }

    protected Object getAttributeValueOf(Attribute attr, String stringValue) {
        return stringValue;
    }

    protected void populateChildren(List<StrutsComponent> children) {
        NodeList nl = getPeer().getChildNodes();
        if(nl != null) {
            for(int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if(n instanceof Element) {
                    StrutsModel model = getModel();
                    StrutsComponent comp = model.getFactory().create((Element)n, this);
                    if(comp != null) {
                        children.add(comp);
                    }
                }
            }
        }
    }
    
    public static abstract class Named extends StrutsComponentImpl 
            implements Nameable<StrutsComponent> {
        public Named(StrutsModelImpl model, Element element) {
            super(model, element);
        }

        public String getName() {
            return super.getAttribute(StrutsAttributes.NAME);
        }

        public void setName(String name) {
            super.setAttribute(Nameable.NAME_PROPERTY, StrutsAttributes.NAME, name);
        }
    }
}
