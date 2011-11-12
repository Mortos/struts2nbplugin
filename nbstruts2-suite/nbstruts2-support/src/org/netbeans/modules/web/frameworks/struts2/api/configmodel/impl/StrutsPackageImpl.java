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
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Action;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.DefaultActionRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.DefaultClassRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.DefaultInterceptorRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalExceptionMappings;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalResults;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Interceptors;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ResultTypes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.visitors.ParentPackageLocator;
import org.netbeans.modules.xml.xam.AbstractReference;
import org.netbeans.modules.xml.xam.Reference;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class StrutsPackageImpl extends StrutsComponentImpl.Named implements StrutsPackage {

    public StrutsPackageImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public StrutsPackageImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.PACKAGE));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public String getNamespace() {
        return super.getAttribute(StrutsAttributes.NAMESPACE);
    }

    public void setNamespace(String namespace) {
        super.setAttribute(NAMESPACE_PROPERTY, StrutsAttributes.NAMESPACE, namespace);
    }

    public String getAbstract() {
        return super.getAttribute(StrutsAttributes.ABSTRACT);
    }

    public void setAbstract(String abs) {
        super.setAttribute(ABSTRACT_PROPERTY, StrutsAttributes.ABSTRACT, abs);
    }

    public String getExternalReferenceResolver() {
        return super.getAttribute(StrutsAttributes.EXTERNAL_REFERENCE_RESOLVER);
    }

    public void setExternalReferenceResolver(String externalReferenceResolver) {
        super.setAttribute(EXTERNAL_REF_RESOLVER_PROPERTY, StrutsAttributes.EXTERNAL_REFERENCE_RESOLVER, externalReferenceResolver);
    }

    public ResultTypes getResultTypes() {
        return super.getChild(ResultTypes.class);
    }

    public void setResultTypes(ResultTypes resultTypes) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(ResultTypes.class, RESULT_TYPES_PROPERTY, resultTypes, empty);
    }

    public Interceptors getInterceptors() {
        return super.getChild(Interceptors.class);
    }

    public void setInterceptors(Interceptors interceptors) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(Interceptors.class, INTERCEPTORS_PROPERTY, interceptors, empty);
    }

    public DefaultInterceptorRef getDefaultInterceptorRef() {
        return super.getChild(DefaultInterceptorRef.class);
    }

    public void setDefaultInterceptorRef(DefaultInterceptorRef defaultInterceptorRef) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(DefaultInterceptorRef.class, DEFAULT_INTERCEPTOR_REF_PROPERTY, defaultInterceptorRef, empty);
    }

    public DefaultActionRef getDefaultActionRef() {
        return super.getChild(DefaultActionRef.class);
    }

    public void setDefaultActionRef(DefaultActionRef defaultActionRef) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(DefaultActionRef.class, DEFAULT_ACTION_REF_PROPERTY, defaultActionRef, empty);
    }

    public GlobalResults getGlobalResults() {
        return super.getChild(GlobalResults.class);
    }

    public void setGlobalResults(GlobalResults globalResults) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(GlobalResults.class, GLOBAL_RESULTS_PROPERTY, globalResults, empty);
    }

    public GlobalExceptionMappings getGlobalExceptionMappings() {
        return super.getChild(GlobalExceptionMappings.class);
    }

    public void setGlobalExceptionMappings(GlobalExceptionMappings globalExceptionMappings) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(GlobalExceptionMappings.class, GLOBAL_EXCEPTION_MAPPINGS_PROPERTY, globalExceptionMappings, empty);
    }

    public List<Action> getActions() {
        return super.getChildren(Action.class);
    }

    public void addAction(int index, Action action) {
        super.insertAtIndex(ACTION_PROPERTY, action, index, Action.class);
    }

    public void removeAction(Action action) {
        super.removeChild(ACTION_PROPERTY, action);
    }

    public String getParentPackageName() {
        return super.getAttribute(StrutsAttributes.EXTENDS);
    }

    public void setParentPackageName(String packName) {
        super.setAttribute(EXTENDS_PROPERTY, StrutsAttributes.EXTENDS, packName);
    }

    public Reference<StrutsPackage> getParentPackage() {
        return new PackageReference();
    }

    public DefaultClassRef getDefaultClassRef() {
        return super.getChild(DefaultClassRef.class);
    }

    public void setDefaultClassRef(DefaultClassRef defaultClassRef) {
        List<Class<? extends StrutsComponent>> empty = Collections.emptyList();
        setChild(DefaultClassRef.class, DEFAULT_CLASS_REF_PROPERTY, defaultClassRef, empty);
    }

    private final class PackageReference extends AbstractReference<StrutsPackage> {

        public PackageReference() {
            super(StrutsPackage.class, StrutsPackageImpl.this, StrutsPackageImpl.this.getParentPackageName());
        }

        public StrutsPackage get() {
            if(getReferenced() == null) {
                StrutsPackage parentPack = 
                        new ParentPackageLocator().findParentPackage(StrutsPackageImpl.this);
                if(parentPack != null) {
                    setReferenced(parentPack);
                }
            }
            
            return getReferenced();
        }
        
        protected StrutsComponent getReferencingComponent() {
            return (StrutsComponent) super.getParent();
        }
    }
}