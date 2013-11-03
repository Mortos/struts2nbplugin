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

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Constant;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class ConstantImpl extends StrutsComponentImpl.Named implements Constant {

    public ConstantImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public ConstantImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.CONSTANT));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public String getValue() {
        return super.getAttribute(StrutsAttributes.VALUE);
    }

    public void setValue(String value) {
        super.setAttribute(VALUE_PROPERTY, StrutsAttributes.VALUE, value);
    }

}