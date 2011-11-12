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
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerRef;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.UnknownHandlerStack;
import org.netbeans.modules.xml.xam.ComponentUpdater;
import org.netbeans.modules.xml.xam.ComponentUpdater.Operation;

/**
 *
 * @author Rohan Ranade
 */
public class SyncUpdateVisitor extends StrutsVisitor.Default 
        implements ComponentUpdater<StrutsComponent> {

    private StrutsComponent target;
    private Operation operation;
    private int index;
    
    public SyncUpdateVisitor() {
    }

    public void update(StrutsComponent target, StrutsComponent child, Operation operation) {
        update(target, child, -1, operation);
    }

    public void update(StrutsComponent target, StrutsComponent child, int index, Operation operation) {
        assert target != null;
        assert child != null;
        this.target = target;
        this.index = index;
        this.operation = operation;
        child.accept(this);
    }
    
    private void insert(String propertyName, StrutsComponent component) {
        ((StrutsComponentImpl)target).insertAtIndex(propertyName, component, index);
    }

    private void remove(String propertyName, StrutsComponent component) {
        ((StrutsComponentImpl)target).removeChild(propertyName, component);
    }

    @Override
    public void visit(StrutsPackage component) {
        if(target instanceof Struts) {
            if(operation == Operation.ADD) {
                insert(Struts.PACKAGE_PROPERTY, component);
            } else {
                remove(Struts.PACKAGE_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Include component) {
        if(target instanceof Struts) {
            if(operation == Operation.ADD) {
                insert(Struts.INCLUDE_PROPERTY, component);
            } else {
                remove(Struts.INCLUDE_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Bean component) {
        if(target instanceof Struts) {
            if(operation == Operation.ADD) {
                insert(Struts.BEAN_PROPERTY, component);
            } else {
                remove(Struts.BEAN_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Constant component) {
        if(target instanceof Struts) {
            if(operation == Operation.ADD) {
                insert(Struts.CONSTANT_PROPERTY, component);
            } else {
                remove(Struts.CONSTANT_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(UnknownHandlerStack component) {
        if(target instanceof Struts) {
            if(operation == Operation.ADD) {
                insert(Struts.UNKNOWN_HANDLER_STACK_PROPERTY, component);
            } else {
                remove(Struts.UNKNOWN_HANDLER_STACK_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(ResultTypes component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.RESULT_TYPES_PROPERTY, component);
            } else {
                remove(StrutsPackage.RESULT_TYPES_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Interceptors component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.INTERCEPTORS_PROPERTY, component);
            } else {
                remove(StrutsPackage.INTERCEPTORS_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(DefaultInterceptorRef component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.DEFAULT_INTERCEPTOR_REF_PROPERTY, component);
            } else {
                remove(StrutsPackage.DEFAULT_INTERCEPTOR_REF_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(DefaultClassRef component) {
        if (target instanceof StrutsPackage) {
            if (operation == Operation.ADD) {
                insert(StrutsPackage.DEFAULT_CLASS_REF_PROPERTY, component);
            } else {
                remove(StrutsPackage.DEFAULT_CLASS_REF_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(DefaultActionRef component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.DEFAULT_ACTION_REF_PROPERTY, component);
            } else {
                remove(StrutsPackage.DEFAULT_ACTION_REF_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(GlobalResults component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.GLOBAL_RESULTS_PROPERTY, component);
            } else {
                remove(StrutsPackage.GLOBAL_RESULTS_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(GlobalExceptionMappings component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.GLOBAL_EXCEPTION_MAPPINGS_PROPERTY, component);
            } else {
                remove(StrutsPackage.GLOBAL_EXCEPTION_MAPPINGS_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Action component) {
        if(target instanceof StrutsPackage) {
            if(operation == Operation.ADD) {
                insert(StrutsPackage.ACTION_PROPERTY, component);
            } else {
                remove(StrutsPackage.ACTION_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(ResultType component) {
        if(target instanceof ResultTypes) {
            if(operation == Operation.ADD) {
                insert(ResultTypes.RESULT_TYPE_PROPERTY, component);
            } else {
                remove(ResultTypes.RESULT_TYPE_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Param component) {
        if(target instanceof ResultType) {
            if(operation == Operation.ADD) {
                insert(ResultType.PARAM_PROPERTY, component);
            } else {
                remove(ResultType.PARAM_PROPERTY, component);
            }
        } else if(target instanceof Interceptor) {
            if(operation == Operation.ADD) {
                insert(Interceptor.PARAM_PROPERTY, component);
            } else {
                remove(Interceptor.PARAM_PROPERTY, component);
            }
        } else if(target instanceof InterceptorRef) {
            if(operation == Operation.ADD) {
                insert(InterceptorRef.PARAM_PROPERTY, component);
            } else {
                remove(InterceptorRef.PARAM_PROPERTY, component);
            }
        } else if(target instanceof DefaultInterceptorRef) {
            if(operation == Operation.ADD) {
                insert(DefaultInterceptorRef.PARAM_PROPERTY, component);
            } else {
                remove(DefaultInterceptorRef.PARAM_PROPERTY, component);
            }
        } else if(target instanceof DefaultActionRef) {
            if(operation == Operation.ADD) {
                insert(DefaultActionRef.PARAM_PROPERTY, component);
            } else {
                remove(DefaultActionRef.PARAM_PROPERTY, component);
            }
        } else if(target instanceof Action) {
            if(operation == Operation.ADD) {
                insert(Action.PARAM_PROPERTY, component);
            } else {
                remove(Action.PARAM_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Interceptor component) {
        if(target instanceof Interceptors) {
            if(operation == Operation.ADD) {
                insert(Interceptors.INTERCEPTOR_PROPERTY, component);
            } else {
                remove(Interceptors.INTERCEPTOR_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(InterceptorStack component) {
        if(target instanceof Interceptors) {
            if(operation == Operation.ADD) {
                insert(Interceptors.INTERCEPTOR_STACK_PROPERTY, component);
            } else {
                remove(Interceptors.INTERCEPTOR_STACK_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(InterceptorRef component) {
        if(target instanceof InterceptorStack) {
            if(operation == Operation.ADD) {
                insert(InterceptorStack.INTERCEPTOR_REF_PROPERTY, component);
            } else {
                remove(InterceptorStack.INTERCEPTOR_REF_PROPERTY, component);
            }
        } else if(target instanceof Action) {
            if(operation == Operation.ADD) {
                insert(Action.INTERCEPTOR_REF_PROPERTY, component);
            } else {
                remove(Action.INTERCEPTOR_REF_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(Result component) {
        if(target instanceof GlobalResults) {
            if(operation == Operation.ADD) {
                insert(GlobalResults.RESULT_PROPERTY, component);
            } else {
                remove(GlobalResults.RESULT_PROPERTY, component);
            }
        } else if(target instanceof Action) {
            if(operation == Operation.ADD) {
                insert(Action.RESULT_PROPERTY, component);
            } else {
                remove(Action.RESULT_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(ExceptionMapping component) {
        if(target instanceof GlobalExceptionMappings) {
            if(operation == Operation.ADD) {
                insert(GlobalExceptionMappings.EXCEPTION_MAPPING_PROPERTY, component);
            } else {
                remove(GlobalExceptionMappings.EXCEPTION_MAPPING_PROPERTY, component);
            }
        } else if(target instanceof Action) {
            if(operation == Operation.ADD) {
                insert(Action.EXCEPTION_MAPPING_PROPERTY, component);
            } else {
                remove(Action.EXCEPTION_MAPPING_PROPERTY, component);
            }
        }
    }

    @Override
    public void visit(UnknownHandlerRef component) {
        if (target instanceof UnknownHandlerStack) {
            if(operation == Operation.ADD) {
                insert(UnknownHandlerStack.UNKNOWN_HANDLER_REF_PROPERTY, component);
            } else {
                remove(UnknownHandlerStack.UNKNOWN_HANDLER_REF_PROPERTY, component);
            }
        }
    }
}
