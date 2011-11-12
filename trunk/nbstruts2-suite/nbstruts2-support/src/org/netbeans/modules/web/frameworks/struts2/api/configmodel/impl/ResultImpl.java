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
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Param;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Result;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class ResultImpl extends StrutsComponentImpl.Named implements Result {

    public ResultImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public ResultImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.RESULT));
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

    public List<Param> getParams() {
        return super.getChildren(Param.class);
    }

    public void addParam(int index, Param param) {
        super.insertAtIndex(PARAM_PROPERTY, param, index, Param.class);
    }

    public void removeParam(Param param) {
        super.removeChild(PARAM_PROPERTY, param);
    }

    public void setResultText(String text) {
        super.setText(TEXT_CONTENT_PROPERTY, text);
    }

    public String getResultText() {
        return super.getText();
    }
}
