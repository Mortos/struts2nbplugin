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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel;

import java.util.List;
import org.netbeans.modules.xml.xam.Nameable;
import org.netbeans.modules.xml.xam.Reference;

/**
 *
 * @author Rohan Ranade
 */
public interface StrutsPackage extends ReferenceableStrutsComponent, Nameable<StrutsComponent> {
    // Attributes
    public static final String EXTENDS_PROPERTY = "extends";
    public static final String NAMESPACE_PROPERTY = "namespace";
    public static final String ABSTRACT_PROPERTY = "abstract";
    public static final String EXTERNAL_REF_RESOLVER_PROPERTY = "externalReferenceResolver"; // XXX - Reference?
        
    String getNamespace();
    void setNamespace(String namespace);
    
    String getAbstract();
    void setAbstract(String abs);
    
    String getExternalReferenceResolver();
    void setExternalReferenceResolver(String externalReferenceResolver);
    
    // Children
    public static final String RESULT_TYPES_PROPERTY = "result-types"; // NOI18N
    public static final String INTERCEPTORS_PROPERTY = "interceptors"; // NOI18N
    public static final String DEFAULT_INTERCEPTOR_REF_PROPERTY = "default-interceptor-ref"; // NOI18N
    public static final String DEFAULT_ACTION_REF_PROPERTY = "default-action-ref"; // NOI18N
    public static final String DEFAULT_CLASS_REF_PROPERTY = "default-class-ref"; // NOI18N
    public static final String GLOBAL_RESULTS_PROPERTY = "global-results"; // NOI18N
    public static final String GLOBAL_EXCEPTION_MAPPINGS_PROPERTY = "global-exception-mappings"; // NOI18N
    public static final String ACTION_PROPERTY = "action"; // NOI18N
    
    ResultTypes getResultTypes();
    void setResultTypes(ResultTypes resultTypes);
    
    Interceptors getInterceptors();
    void setInterceptors(Interceptors interceptors);
    
    DefaultInterceptorRef getDefaultInterceptorRef();
    void setDefaultInterceptorRef(DefaultInterceptorRef defaultInterceptorRef);
    
    DefaultActionRef getDefaultActionRef();
    void setDefaultActionRef(DefaultActionRef defaultActionRef);

    DefaultClassRef getDefaultClassRef();
    void setDefaultClassRef(DefaultClassRef defaultClassRef);

    GlobalResults getGlobalResults();
    void setGlobalResults(GlobalResults globalResults);
    
    GlobalExceptionMappings getGlobalExceptionMappings();
    void setGlobalExceptionMappings(GlobalExceptionMappings globalExceptionMappings);
    
    List<Action> getActions();
    void addAction(int index, Action action);
    void removeAction(Action action);
    
    String getParentPackageName();
    void setParentPackageName(String packName);
    
    Reference<StrutsPackage> getParentPackage();
}
