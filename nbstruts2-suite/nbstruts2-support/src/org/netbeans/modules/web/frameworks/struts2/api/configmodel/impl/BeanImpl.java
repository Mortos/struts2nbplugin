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

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Bean;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class BeanImpl extends StrutsComponentImpl.Named implements Bean {

    public BeanImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public BeanImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.BEAN));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public String getType() {
        return super.getAttribute(StrutsAttributes.TYPE);
    }

    public void setType(String type) {
        super.setAttribute(TYPE_PROPERTY, StrutsAttributes.TYPE, type);
    }

    public String getClassValue() {
        return super.getAttribute(StrutsAttributes.CLASS);
    }

    public void setClassValue(String classValue) {
        super.setAttribute(CLASS_PROPERTY, StrutsAttributes.CLASS, classValue);
    }

    public String getScope() {
        return super.getAttribute(StrutsAttributes.SCOPE);
    }

    public void setScope(String scope) {
        super.setAttribute(SCOPE_PROPERTY, StrutsAttributes.SCOPE, scope);
    }

    public String getStatic() {
        return super.getAttribute(StrutsAttributes.STATIC);
    }

    public void setStatic(String st) {
        super.setAttribute(STATIC_PROPERTY, StrutsAttributes.STATIC, st);
    }

    public String getOptional() {
        return super.getAttribute(StrutsAttributes.OPTIONAL);
    }

    public void setOptional(String opt) {
        super.setAttribute(OPTIONAL_PROPERTY, StrutsAttributes.OPTIONAL, opt);
    }

}
