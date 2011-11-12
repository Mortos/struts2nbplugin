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
import java.util.HashSet;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalExceptionMappings;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalResults;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.InterceptorStack;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Interceptors;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ResultType;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ResultTypes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerStack;

/**
 *
 * @author Rohan Ranade
 */
public enum StrutsQNames {
    STRUTS("struts"),
    PACKAGE(Struts.PACKAGE_PROPERTY),
    RESULT_TYPES(StrutsPackage.RESULT_TYPES_PROPERTY),
    RESULT_TYPE(ResultTypes.RESULT_TYPE_PROPERTY),
    PARAM(ResultType.PARAM_PROPERTY),
    INTERCEPTORS(StrutsPackage.INTERCEPTORS_PROPERTY),
    INTERCEPTOR(Interceptors.INTERCEPTOR_PROPERTY),
    INTERCEPTOR_STACK(Interceptors.INTERCEPTOR_STACK_PROPERTY),
    INTERCEPTOR_REF(InterceptorStack.INTERCEPTOR_REF_PROPERTY),
    DEFAULT_INTERCEPTOR_REF(StrutsPackage.DEFAULT_INTERCEPTOR_REF_PROPERTY),
    DEFAULT_CLASS_REF(StrutsPackage.DEFAULT_CLASS_REF_PROPERTY),
    DEFAULT_ACTION_REF(StrutsPackage.DEFAULT_ACTION_REF_PROPERTY),
    GLOBAL_RESULTS(StrutsPackage.GLOBAL_RESULTS_PROPERTY),
    RESULT(GlobalResults.RESULT_PROPERTY),
    GLOBAL_EXCEPTION_MAPPINGS(StrutsPackage.GLOBAL_EXCEPTION_MAPPINGS_PROPERTY),
    EXCEPTION_MAPPING(GlobalExceptionMappings.EXCEPTION_MAPPING_PROPERTY),
    ACTION(StrutsPackage.ACTION_PROPERTY),
    INCLUDE(Struts.INCLUDE_PROPERTY),
    BEAN(Struts.BEAN_PROPERTY),
    CONSTANT(Struts.CONSTANT_PROPERTY),
    UNKNOWN_HANDLER_STACK(Struts.UNKNOWN_HANDLER_STACK_PROPERTY),
    UNKNOWN_HANDLER_REF(UnknownHandlerStack.UNKNOWN_HANDLER_REF_PROPERTY);
    
    private QName qname;
    
    private static final Set<QName> mappedQNames = new HashSet<QName>();
    
    static {
        mappedQNames.add(STRUTS.getQName());
        mappedQNames.add(PACKAGE.getQName());
        mappedQNames.add(RESULT_TYPES.getQName());
        mappedQNames.add(RESULT_TYPE.getQName());
        mappedQNames.add(PARAM.getQName());
        mappedQNames.add(INTERCEPTORS.getQName());
        mappedQNames.add(INTERCEPTOR.getQName());
        mappedQNames.add(INTERCEPTOR_STACK.getQName());
        mappedQNames.add(INTERCEPTOR_REF.getQName());
        mappedQNames.add(DEFAULT_INTERCEPTOR_REF.getQName());
        mappedQNames.add(DEFAULT_CLASS_REF.getQName());
        mappedQNames.add(DEFAULT_ACTION_REF.getQName());
        mappedQNames.add(GLOBAL_RESULTS.getQName());
        mappedQNames.add(RESULT.getQName());
        mappedQNames.add(GLOBAL_EXCEPTION_MAPPINGS.getQName());
        mappedQNames.add(EXCEPTION_MAPPING.getQName());
        mappedQNames.add(ACTION.getQName());
        mappedQNames.add(INCLUDE.getQName());
        mappedQNames.add(BEAN.getQName());
        mappedQNames.add(CONSTANT.getQName());
        mappedQNames.add(UNKNOWN_HANDLER_STACK.getQName());
        mappedQNames.add(UNKNOWN_HANDLER_REF.getQName());
    }
    
    StrutsQNames(String localName) {
        this(XMLConstants.NULL_NS_URI, localName, XMLConstants.DEFAULT_NS_PREFIX);
    }
    
    StrutsQNames(String namespace, String localName, String prefix) {
        if(prefix == null) {
            qname = new QName(namespace, localName);
        } else {
            qname = new QName(namespace, localName, prefix);
        }
    }
    
    public QName getQName() {
        return qname;
    }
    
    public String getLocalName() {
        return qname.getLocalPart();
    }
    
    public String getQualifiedName() {
        return qname.getPrefix() + ":" + qname.getLocalPart();
    }
    
    public static Set<QName> getMappedQNames() {
        return Collections.unmodifiableSet(mappedQNames);
    }
}
