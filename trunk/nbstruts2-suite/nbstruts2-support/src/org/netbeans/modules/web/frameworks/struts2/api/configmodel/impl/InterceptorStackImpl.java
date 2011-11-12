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
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.InterceptorRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.InterceptorStack;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class InterceptorStackImpl extends StrutsComponentImpl.Named implements InterceptorStack {

    public InterceptorStackImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public InterceptorStackImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.INTERCEPTOR_STACK));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public List<InterceptorRef> getInterceptorRefs() {
        return super.getChildren(InterceptorRef.class);
    }

    public void addInterceptorRef(int index, InterceptorRef interceptorRef) {
        super.insertAtIndex(INTERCEPTOR_REF_PROPERTY, interceptorRef, index, InterceptorRef.class);
    }

    public void removeInterceptorRef(InterceptorRef interceptorRef) {
        super.removeChild(INTERCEPTOR_REF_PROPERTY, interceptorRef);
    }

}
