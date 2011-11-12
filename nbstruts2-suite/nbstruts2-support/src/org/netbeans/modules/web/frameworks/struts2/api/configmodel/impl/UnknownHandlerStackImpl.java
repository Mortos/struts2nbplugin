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
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerStack;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class UnknownHandlerStackImpl extends StrutsComponentImpl implements UnknownHandlerStack {

    public UnknownHandlerStackImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public UnknownHandlerStackImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.UNKNOWN_HANDLER_STACK));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public List<UnknownHandlerRef> getUnknownHandlerRefs() {
        return super.getChildren(UnknownHandlerRef.class);
    }

    public void addUnknownHandlerRef(int index, UnknownHandlerRef unknownHandlerRef) {
        super.insertAtIndex(UNKNOWN_HANDLER_REF_PROPERTY, unknownHandlerRef, index, UnknownHandlerRef.class);
    }

    public void removeUnknownHandlerRef(UnknownHandlerRef unknownHandlerRef) {
        super.removeChild(UNKNOWN_HANDLER_REF_PROPERTY, unknownHandlerRef);
    }

}
