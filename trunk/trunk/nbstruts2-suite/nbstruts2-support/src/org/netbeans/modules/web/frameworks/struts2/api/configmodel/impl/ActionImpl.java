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
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Action;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ExceptionMapping;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.InterceptorRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Param;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Result;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class ActionImpl extends StrutsComponentImpl.Named implements Action {

    public ActionImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public ActionImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.ACTION));
    }
    
    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public String getClassValue() {
        return super.getAttribute(StrutsAttributes.CLASS);
    }

    public void setClassValue(String classValue) {
        super.setAttribute(CLASS_PROPERTY, StrutsAttributes.CLASS, classValue);
    }

    public String getMethod() {
        return super.getAttribute(StrutsAttributes.METHOD);
    }

    public void setMethod(String method) {
        super.setAttribute(METHOD_PROPERTY, StrutsAttributes.METHOD, method);
    }

    public String getConverter() {
        return super.getAttribute(StrutsAttributes.CONVERTER);
    }

    public void setConverter(String converter) {
        super.setAttribute(CONVERTER_PROPERTY, StrutsAttributes.CONVERTER, converter);
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

    public List<Result> getResults() {
        return super.getChildren(Result.class);
    }

    public void addResult(int index, Result result) {
        super.insertAtIndex(RESULT_PROPERTY, result, index, Result.class);
    }

    public void removeResult(Result result) {
        super.removeChild(RESULT_PROPERTY, result);
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

    public List<ExceptionMapping> getExceptionMappings() {
        return super.getChildren(ExceptionMapping.class);
    }

    public void addExceptionMapping(int index, ExceptionMapping exceptionMapping) {
        super.insertAtIndex(EXCEPTION_MAPPING_PROPERTY, exceptionMapping, index, ExceptionMapping.class);
    }

    public void removeExceptionMapping(ExceptionMapping exceptionMapping) {
        super.removeChild(EXCEPTION_MAPPING_PROPERTY, exceptionMapping);
    }

}
