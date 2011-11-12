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
package org.netbeans.modules.web.frameworks.struts2.completion;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsQNames;
import org.netbeans.modules.web.frameworks.struts2.editor.ContextUtilities;
import org.netbeans.modules.web.frameworks.struts2.utils.StringUtils;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.netbeans.modules.xml.text.syntax.dom.TextImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Manages completion for Struts config files
 * 
 * @author Sujit Nair, Rohan Ranade
 */
public class StrutsXMLCompletionManager {

    private Map<String, CompletorFactory> completorFactories = new HashMap<String, CompletorFactory>();

    private StrutsXMLCompletionManager() {
        setupCompletors();
    }

    private void setupCompletors() {

        String[] defaultLazyInitItems = new String[]{
            "true", null, //XXX: Documentation // NOI18N
            "false", null, //XXX: Documentation // NOI18N
        };
        AttributeValueCompletorFactory completorFactory = new AttributeValueCompletorFactory(defaultLazyInitItems);
        registerCompletorFactory(StrutsQNames.BEAN.getLocalName(), StrutsAttributes.STATIC.getName(), completorFactory);
        registerCompletorFactory(StrutsQNames.BEAN.getLocalName(), StrutsAttributes.OPTIONAL.getName(), completorFactory);
        registerCompletorFactory(StrutsQNames.PACKAGE.getLocalName(), StrutsAttributes.ABSTRACT.getName(), completorFactory);

        String[] scopeItems = new String[]{
            "default", null, //XXX: Documentation // NOI18N
            "singleton", null, //XXX: Documentation // NOI18N
            "request", null, //XXX: Documentation // NOI18N
            "session", null, //XXX: Documentation // NOI18N
            "thread", null, //XXX: Documentation // NOI18N
        };
        completorFactory = new AttributeValueCompletorFactory(scopeItems);
        registerCompletorFactory(StrutsQNames.BEAN, StrutsAttributes.SCOPE, completorFactory);

        GenericCompletorFactory genericCompletorFactory = new GenericCompletorFactory(BeanClassCompletor.class);
        registerCompletorFactory(StrutsQNames.BEAN, StrutsAttributes.CLASS, genericCompletorFactory);

        genericCompletorFactory = new GenericCompletorFactory(BeanTypeCompletor.class);
        registerCompletorFactory(StrutsQNames.BEAN, StrutsAttributes.TYPE, genericCompletorFactory);

        // Register interceptor_ref
        genericCompletorFactory = new GenericCompletorFactory(InterceptorRefCompletor.class);
        registerCompletorFactory(StrutsQNames.INTERCEPTOR_REF, StrutsAttributes.NAME, genericCompletorFactory);
        
        // Register default_interceptor_ref
        genericCompletorFactory = new GenericCompletorFactory(DefaultInterceptorRefCompletor.class);
        registerCompletorFactory(StrutsQNames.DEFAULT_INTERCEPTOR_REF, StrutsAttributes.NAME, genericCompletorFactory);
        
        // Register default_action_ref
        genericCompletorFactory = new GenericCompletorFactory(DefaultActionRefCompletor.class);
        registerCompletorFactory(StrutsQNames.DEFAULT_ACTION_REF, StrutsAttributes.NAME, genericCompletorFactory);
        
        // Register result_type
        genericCompletorFactory = new GenericCompletorFactory(ResultTypeCompletor.class);
        registerCompletorFactory(StrutsQNames.RESULT, StrutsAttributes.TYPE, genericCompletorFactory);
        
        // Register result (exception-mapping, result)
        genericCompletorFactory = new GenericCompletorFactory(ExceptionMappingResultCompletor.class);
        registerCompletorFactory(StrutsQNames.EXCEPTION_MAPPING, StrutsAttributes.RESULT, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(ActionClassCompletor.class);
        registerCompletorFactory(StrutsQNames.ACTION, StrutsAttributes.CLASS, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(ActionMethodCompletor.class);
        registerCompletorFactory(StrutsQNames.ACTION, StrutsAttributes.METHOD, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(InterceptorClassCompletor.class);
        registerCompletorFactory(StrutsQNames.INTERCEPTOR, StrutsAttributes.CLASS, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(ExceptionMappingClassCompletor.class);
        registerCompletorFactory(StrutsQNames.EXCEPTION_MAPPING, StrutsAttributes.EXCEPTION, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(ResultTypeClassCompletor.class);
        registerCompletorFactory(StrutsQNames.RESULT_TYPE, StrutsAttributes.CLASS, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(IncludeFileCompletor.class);
        registerCompletorFactory(StrutsQNames.INCLUDE, StrutsAttributes.FILE, genericCompletorFactory);
        
        genericCompletorFactory = new GenericCompletorFactory(PackageExtendsCompletor.class);
        registerCompletorFactory(StrutsQNames.PACKAGE, StrutsAttributes.EXTENDS, genericCompletorFactory);
    }
    private static StrutsXMLCompletionManager INSTANCE = new StrutsXMLCompletionManager();

    public static StrutsXMLCompletionManager getDefault() {
        return INSTANCE;
    }

    public void completeAttributeValues(CompletionResultSet resultSet, CompletionContextImpl context) {
        String tagName = context.getTag().getNodeName();
        TokenItem attrib = ContextUtilities.getAttributeToken(context.getCurrentToken());
        String attribName = attrib != null ? attrib.getImage() : null;

        CompletorFactory completorFactory = locateCompletorFactory(tagName, attribName);
        if (completorFactory != null) {
            Completor completor = completorFactory.createCompletor();
            resultSet.addAllItems(completor.doCompletion(context));
            if (completor.getAnchorOffset() != -1) {
                resultSet.setAnchorOffset(completor.getAnchorOffset());
            }
        }
    }

    private void registerCompletorFactory(String tagName, String attribName,
            CompletorFactory completorFactory) {
        completorFactories.put(createRegisteredName(tagName, attribName), completorFactory);
    }

    private void registerCompletorFactory(StrutsQNames tag, StrutsAttributes attrib,
            CompletorFactory factory) {
        registerCompletorFactory(tag.getLocalName(), attrib.getName(), factory);
    }

    private static String createRegisteredName(String nodeName, String attributeName) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(nodeName)) {
            builder.append("/nodeName=");  // NOI18N
            builder.append(nodeName);
        } else {
            builder.append("/nodeName=");  // NOI18N
            builder.append("*");  // NOI18N
        }

        if (StringUtils.hasText(attributeName)) {
            builder.append("/attribute="); // NOI18N
            builder.append(attributeName);
        }

        return builder.toString();
    }

    // package private for testing purposes
    CompletorFactory locateCompletorFactory(String nodeName, String attributeName) {
        String key = createRegisteredName(nodeName, attributeName);
        if (completorFactories.containsKey(key)) {
            return completorFactories.get(key);
        }

        key = createRegisteredName(nodeName, null);
        if (completorFactories.containsKey(key)) {
            return completorFactories.get(key);
        }

        key = createRegisteredName("*", attributeName); // NOI18N
        if (completorFactories.containsKey(key)) {
            return completorFactories.get(key);
        }

        return null;
    }

    public static String getPackageName(Node currentTag) {
        String packageName = null;
        Node iteratorNode = null;
        String trimString = null;

        if (currentTag instanceof Tag) {
            currentTag = (Tag) currentTag;
        } else if (currentTag instanceof TextImpl) {
            currentTag = (TextImpl) currentTag;
            trimString = currentTag.getNodeValue().trim();
            if (trimString.equals("")) {
                trimString = null;
            }
        }


        if (trimString == null) {
            iteratorNode = (Node) currentTag;
        }


        while (iteratorNode != null) {
            if (iteratorNode.getNodeName().equals(StrutsQNames.PACKAGE.getLocalName())) {
                NamedNodeMap attList = iteratorNode.getAttributes();
                Node attName = attList.getNamedItem("name");
                packageName = attName.getNodeValue();
                break;
            } else {
                iteratorNode = iteratorNode.getParentNode();
            }
        }

        return packageName;
    }
}
