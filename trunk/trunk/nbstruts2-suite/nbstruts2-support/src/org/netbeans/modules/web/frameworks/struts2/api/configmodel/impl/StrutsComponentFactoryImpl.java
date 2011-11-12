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

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Action;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Bean;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Constant;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.DefaultActionRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.DefaultClassRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.DefaultInterceptorRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ExceptionMapping;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalExceptionMappings;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalResults;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Interceptor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.InterceptorRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.InterceptorStack;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Interceptors;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Param;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Result;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ResultType;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ResultTypes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponentFactory;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerStack;
import org.netbeans.modules.xml.xam.dom.AbstractDocumentComponent;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class StrutsComponentFactoryImpl implements StrutsComponentFactory {

    private StrutsModelImpl model;
    
    public StrutsComponentFactoryImpl(StrutsModelImpl model) {
        this.model = model;
    }

    public StrutsComponent create(Element element, StrutsComponent context) {
        if(context == null) {
            if(areSameQName(StrutsQNames.STRUTS, element)) {
                return new StrutsImpl(model, element);
            } else {
                return null;
            }
        } else {
            return new CreateVisitor().create(element, context);
        }
    }

    public Struts createStruts() {
        return new StrutsImpl(model);
    }

    public StrutsPackage createStrutsPackage() {
        return new StrutsPackageImpl(model);
    }

    public ResultTypes createResultTypes() {
        return new ResultTypesImpl(model);
    }

    public ResultType createResultType() {
        return new ResultTypeImpl(model);
    }

    public Param createParam() {
        return new ParamImpl(model);
    }

    public Interceptors createInterceptors() {
        return new InterceptorsImpl(model);
    }

    public Interceptor createInterceptor() {
        return new InterceptorImpl(model);
    }

    public InterceptorStack createInterceptorStack() {
        return new InterceptorStackImpl(model);
    }

    public InterceptorRef createInterceptorRef() {
        return new InterceptorRefImpl(model);
    }

    public DefaultInterceptorRef createDefaultInterceptorRef() {
        return new DefaultInterceptorRefImpl(model);
    }

    public DefaultClassRef createDefaultClassRef() {
        return new DefaultClassRefImpl(model);
    }

    public DefaultActionRef createDefaultActionRef() {
        return new DefaultActionRefImpl(model);
    }

    public GlobalResults createGlobalResults() {
        return new GlobalResultsImpl(model);
    }

    public Result createResult() {
        return new ResultImpl(model);
    }

    public GlobalExceptionMappings createGlobalExceptionMappings() {
        return new GlobalExceptionMappingsImpl(model);
    }

    public ExceptionMapping createExceptionMapping() {
        return new ExceptionMappingImpl(model);
    }

    public Action createAction() {
        return new ActionImpl(model);
    }

    public Include createInclude() {
        return new IncludeImpl(model);
    }

    public Bean createBean() {
        return new BeanImpl(model);
    }

    public Constant createConstant() {
        return new ConstantImpl(model);
    }

    public UnknownHandlerStack createUnknownHandlerStack() {
        return new UnknownHandlerStackImpl(model);
    }

    public UnknownHandlerRef createUnknownHandlerRef() {
        return new UnknownHandlerRefImpl(model);
    }

    public static boolean areSameQName(StrutsQNames q, Element e) {
        return q.getQName().equals(AbstractDocumentComponent.getQName(e));
    }
    
    public static class CreateVisitor extends StrutsVisitor.Default {
        Element element;
        StrutsComponent created;
        
        StrutsComponent create(Element element, StrutsComponent context) {
            this.element = element;
            context.accept(this);
            return created;
        }
        
        private boolean isElementQName(StrutsQNames q) {
            return areSameQName(q, element);
        }

        @Override
        public void visit(Struts context) {
            if(isElementQName(StrutsQNames.PACKAGE)) {
                created = new StrutsPackageImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.INCLUDE)) {
                created = new IncludeImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.BEAN)) {
                created = new BeanImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.CONSTANT)) {
                created = new ConstantImpl((StrutsModelImpl) context.getModel(), element);
            } else if (isElementQName(StrutsQNames.UNKNOWN_HANDLER_STACK)) {
                created = new UnknownHandlerStackImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(UnknownHandlerStack context) {
            if (isElementQName(StrutsQNames.UNKNOWN_HANDLER_REF)) {
                created = new UnknownHandlerRefImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(StrutsPackage context) {
            if(isElementQName(StrutsQNames.RESULT_TYPES)) {
                created = new ResultTypesImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.INTERCEPTORS)) {
                created = new InterceptorsImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.DEFAULT_INTERCEPTOR_REF)) {
                created = new DefaultInterceptorRefImpl((StrutsModelImpl) context.getModel(), element);
            } else if (isElementQName(StrutsQNames.DEFAULT_CLASS_REF)) {
                created = new DefaultClassRefImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.DEFAULT_ACTION_REF)) {
                created = new DefaultActionRefImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.GLOBAL_RESULTS)) {
                created = new GlobalResultsImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.GLOBAL_EXCEPTION_MAPPINGS)) {
                created = new GlobalExceptionMappingsImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.ACTION)) {
                created = new ActionImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(ResultTypes context) {
            if(isElementQName(StrutsQNames.RESULT_TYPE)) {
                created = new ResultTypeImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(ResultType context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(Interceptors context) {
            if(isElementQName(StrutsQNames.INTERCEPTOR)) {
                created = new InterceptorImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.INTERCEPTOR_STACK)) {
                created = new InterceptorStackImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(Interceptor context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(InterceptorStack context) {
            if(isElementQName(StrutsQNames.INTERCEPTOR_REF)) {
                created = new InterceptorRefImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(InterceptorRef context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(DefaultInterceptorRef context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(DefaultActionRef context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(GlobalResults context) {
            if(isElementQName(StrutsQNames.RESULT)) {
                created = new ResultImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(Result context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(GlobalExceptionMappings context) {
            if(isElementQName(StrutsQNames.EXCEPTION_MAPPING)) {
                created = new ExceptionMappingImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(ExceptionMapping context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            }
        }

        @Override
        public void visit(Action context) {
            if(isElementQName(StrutsQNames.PARAM)) {
                created = new ParamImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.RESULT)) {
                created = new ResultImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.INTERCEPTOR_REF)) {
                created = new InterceptorRefImpl((StrutsModelImpl) context.getModel(), element);
            } else if(isElementQName(StrutsQNames.EXCEPTION_MAPPING)) {
                created = new ExceptionMappingImpl((StrutsModelImpl) context.getModel(), element);
            } 
        }
    }
}
