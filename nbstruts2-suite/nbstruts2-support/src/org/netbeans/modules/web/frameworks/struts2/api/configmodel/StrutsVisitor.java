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

/**
 *
 * @author Rohan Ranade
 */
public interface StrutsVisitor {
    void visit(Struts struts);
    void visit(StrutsPackage strutsPackage);
    void visit(ResultTypes resultTypes);
    void visit(ResultType resultType);
    void visit(Param param);
    void visit(Interceptors interceptors);
    void visit(Interceptor interceptor);
    void visit(InterceptorStack interceptorStack);
    void visit(InterceptorRef interceptorRef);
    void visit(DefaultInterceptorRef defaultInterceptorRef);
    void visit(DefaultClassRef defaultClassRef);
    void visit(DefaultActionRef defaultActionRef);
    void visit(GlobalResults globalResults);
    void visit(Result result);
    void visit(GlobalExceptionMappings globalExceptionMappings);
    void visit(ExceptionMapping exceptionMapping);
    void visit(Action action);
    void visit(Include include);
    void visit(Bean bean);
    void visit(Constant constant);
    void visit(UnknownHandlerStack unknownHandlerStack);
    void visit(UnknownHandlerRef unknownHandlerRef);
    
    /**
     * Default shallow visitor
     */
    public static class Default implements StrutsVisitor {

        public void visit(Struts struts) {
            visitChild();
        }

        public void visit(StrutsPackage strutsPackage) {
            visitChild();
        }

        public void visit(ResultTypes resultTypes) {
            visitChild();
        }

        public void visit(ResultType resultType) {
            visitChild();
        }

        public void visit(Param param) {
            visitChild();
        }

        public void visit(Interceptors interceptors) {
            visitChild();
        }

        public void visit(Interceptor interceptor) {
            visitChild();
        }

        public void visit(InterceptorStack interceptorStack) {
            visitChild();
        }

        public void visit(InterceptorRef interceptorRef) {
            visitChild();
        }

        public void visit(DefaultInterceptorRef defaultInterceptorRef) {
            visitChild();
        }

        public void visit(DefaultClassRef defaultClassRef) {
            visitChild();
        }

        public void visit(DefaultActionRef defaultActionRef) {
            visitChild();
        }

        public void visit(GlobalResults globalResults) {
            visitChild();
        }

        public void visit(Result result) {
            visitChild();
        }

        public void visit(GlobalExceptionMappings globalExceptionMappings) {
            visitChild();
        }

        public void visit(ExceptionMapping exceptionMapping) {
            visitChild();
        }

        public void visit(Action action) {
            visitChild();
        }

        public void visit(Include include) {
            visitChild();
        }

        public void visit(Bean bean) {
            visitChild();
        }

        public void visit(Constant constant) {
            visitChild();
        }

        public void visit(UnknownHandlerStack unknownHandlerStack) {
            visitChild();
        }

        public void visit(UnknownHandlerRef unknownHandlerRef) {
            visitChild();
        }

        protected void visitChild() {
            
        }
    }
    
    /**
     * Deep visitor
     */
    public static class Deep extends Default {
        
        protected void visitChild(StrutsComponent component) {
            for(StrutsComponent child : component.getChildren()) {
                child.accept(this);
            }
        }
        
    }
}
