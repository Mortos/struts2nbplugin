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

import java.util.Collections;
import java.util.List;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Bean;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Constant;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerStack;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class StrutsImpl extends StrutsComponentImpl implements Struts {

    public StrutsImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }
    
    public StrutsImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.STRUTS));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public List<StrutsPackage> getPackages() {
        return super.getChildren(StrutsPackage.class);
    }

    public void addPackage(int index, StrutsPackage pack) {
        super.insertAtIndex(PACKAGE_PROPERTY, pack, index, StrutsPackage.class);
    }

    public void removePackage(StrutsPackage pack) {
        super.removeChild(PACKAGE_PROPERTY, pack);
    }

    public List<Include> getIncludes() {
        return super.getChildren(Include.class);
    }

    public void addInclude(int index, Include incl) {
        super.insertAtIndex(INCLUDE_PROPERTY, incl, index, Include.class);
    }

    public void removeInclude(Include incl) {
        super.removeChild(INCLUDE_PROPERTY, incl);
    }

    public List<Bean> getBeans() {
        return super.getChildren(Bean.class);
    }

    public void addBean(int index, Bean bean) {
        super.insertAtIndex(BEAN_PROPERTY, bean, index, Bean.class);
    }

    public void removeBean(Bean bean) {
        super.removeChild(BEAN_PROPERTY, bean);
    }

    public List<Constant> getConstants() {
        return super.getChildren(Constant.class);
    }

    public void addConstant(int index, Constant constant) {
        super.insertAtIndex(CONSTANT_PROPERTY, constant, index, Constant.class);
    }

    public void removeConstant(Constant constant) {
        super.removeChild(CONSTANT_PROPERTY, constant);
    }

    public UnknownHandlerStack getUnknownHandlerStack() {
        return super.getChild(UnknownHandlerStack.class);
    }

    public void setUnknownHandlerStack(UnknownHandlerStack unknownHandlerStack) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(UnknownHandlerStack.class, UNKNOWN_HANDLER_STACK_PROPERTY, unknownHandlerStack, empty);
    }
}
