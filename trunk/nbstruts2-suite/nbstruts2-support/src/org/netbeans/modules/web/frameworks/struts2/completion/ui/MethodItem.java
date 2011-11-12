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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.web.frameworks.struts2.completion.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.swing.ImageIcon;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.modules.web.frameworks.struts2.editor.StrutsXMLUtils;
import org.netbeans.modules.web.frameworks.struts2.utils.JavaUtils;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.ImageUtilities;

/**
 * FIXME: Use ElementIcons and not hard coded icon paths
 * 
 * @author Rohan Ranade
 */
public class MethodItem extends StrutsXMLConfigCompletionItem {

    private static final String METHOD_PUBLIC = "org/netbeans/modules/editor/resources/completion/method_16.png"; //NOI18N
        private static final String METHOD_PROTECTED = "org/netbeans/modules/editor/resources/completion/method_protected_16.png"; //NOI18N
        private static final String METHOD_PACKAGE = "org/netbeans/modules/editor/resources/completion/method_package_private_16.png"; //NOI18N
        private static final String METHOD_PRIVATE = "org/netbeans/modules/editor/resources/completion/method_private_16.png"; //NOI18N        
        private static final String METHOD_ST_PUBLIC = "org/netbeans/modules/editor/resources/completion/method_static_16.png"; //NOI18N
        private static final String METHOD_ST_PROTECTED = "org/netbeans/modules/editor/resources/completion/method_static_protected_16.png"; //NOI18N
        private static final String METHOD_ST_PRIVATE = "org/netbeans/modules/editor/resources/completion/method_static_private_16.png"; //NOI18N
        private static final String METHOD_ST_PACKAGE = "org/netbeans/modules/editor/resources/completion/method_static_package_private_16.png"; //NOI18N
        private static final String METHOD_COLOR = "<font color=#000000>"; //NOI18N
        private static final String PARAMETER_NAME_COLOR = "<font color=#a06001>"; //NOI18N
        private static ImageIcon icon[][] = new ImageIcon[2][4];
        
        private ElementHandle<ExecutableElement> elementHandle;
        private boolean isDeprecated;
        private String simpleName;
        private Set<Modifier> modifiers;
        private List<ParamDesc> params;
        private boolean isPrimitive;
        private String typeName;
        private String sortText;
        private String leftText;
        private boolean isInherited;
        private String rightText;
        
        public MethodItem(int substitutionOffset, ExecutableElement element, boolean isInherited, boolean isDeprecated) {
            super(substitutionOffset);
            this.elementHandle = ElementHandle.create(element);
            this.isDeprecated = isDeprecated;
            this.isInherited = isInherited;
            this.simpleName = element.getSimpleName().toString();
            this.modifiers = element.getModifiers();
            this.params = new ArrayList<ParamDesc>();
            Iterator<? extends VariableElement> it = element.getParameters().iterator();
            Iterator<? extends TypeMirror> tIt = ((ExecutableType) element.asType()).getParameterTypes().iterator();
            while(it.hasNext() && tIt.hasNext()) {
                TypeMirror tm = tIt.next();
                this.params.add(new ParamDesc(tm.toString(), JavaUtils.getTypeName(tm, false, element.isVarArgs() && !tIt.hasNext()).toString(), it.next().getSimpleName().toString()));
            }
            TypeMirror retType = element.getReturnType();
            
            this.typeName = JavaUtils.getTypeName(retType, false).toString();
            this.isPrimitive = retType.getKind().isPrimitive() || retType.getKind() == TypeKind.VOID;
        }

        public int getSortPriority() {
            return 100;
        }

        public CharSequence getSortText() {
            if (sortText == null) {
                StringBuilder sortParams = new StringBuilder();
                sortParams.append('('); // NOI18N
                int cnt = 0;
                for(Iterator<ParamDesc> it = params.iterator(); it.hasNext();) {
                    ParamDesc param = it.next();
                    sortParams.append(param.typeName);
                    if (it.hasNext()) {
                        sortParams.append(','); // NOI18N
                    }
                    cnt++;
                }
                sortParams.append(')'); // NOI18N
                sortText = simpleName + "#" + ((cnt < 10 ? "0" : "") + cnt) + "#" + sortParams.toString(); //NOI18N
            }
            return sortText;
        }

        public CharSequence getInsertPrefix() {
            return simpleName;
        }
        
        @Override
        protected String getLeftHtmlText() {
            if (leftText == null) {
                StringBuilder lText = new StringBuilder();
                lText.append(METHOD_COLOR);
                if (!isInherited)
                    lText.append(BOLD);
                if (isDeprecated)
                    lText.append(STRIKE);
                lText.append(simpleName);
                if (isDeprecated)
                    lText.append(STRIKE_END);
                if (!isInherited)
                    lText.append(BOLD_END);
                lText.append(COLOR_END);
                lText.append('('); // NOI18N
                for (Iterator<ParamDesc> it = params.iterator(); it.hasNext();) {
                    ParamDesc paramDesc = it.next();
                    lText.append(StrutsXMLUtils.escape(paramDesc.typeName));
                    lText.append(' ');
                    lText.append(PARAMETER_NAME_COLOR);
                    lText.append(paramDesc.name);
                    lText.append(COLOR_END);
                    if (it.hasNext()) {
                        lText.append(", "); //NOI18N
                    }
                }
                lText.append(')'); // NOI18N
                return lText.toString();
            }
            return leftText;
        }
        
        @Override
        protected String getRightHtmlText() {
            if (rightText == null)
                rightText = StrutsXMLUtils.escape(typeName);
            return rightText;
        }
        
        @Override
        protected ImageIcon getIcon() {
            int level = getProtectionLevel(modifiers);
            boolean isStatic = modifiers.contains(Modifier.STATIC);
            ImageIcon cachedIcon = icon[isStatic?1:0][level];
            if (cachedIcon != null)
                return cachedIcon;
            
            String iconPath = METHOD_PUBLIC;            
            if (isStatic) {
                switch (level) {
                    case PRIVATE_LEVEL:
                        iconPath = METHOD_ST_PRIVATE;
                        break;

                    case PACKAGE_LEVEL:
                        iconPath = METHOD_ST_PACKAGE;
                        break;

                    case PROTECTED_LEVEL:
                        iconPath = METHOD_ST_PROTECTED;
                        break;

                    case PUBLIC_LEVEL:
                        iconPath = METHOD_ST_PUBLIC;
                        break;
                }
            }else{
                switch (level) {
                    case PRIVATE_LEVEL:
                        iconPath = METHOD_PRIVATE;
                        break;

                    case PACKAGE_LEVEL:
                        iconPath = METHOD_PACKAGE;
                        break;

                    case PROTECTED_LEVEL:
                        iconPath = METHOD_PROTECTED;
                        break;

                    case PUBLIC_LEVEL:
                        iconPath = METHOD_PUBLIC;
                        break;
                }
            }
            ImageIcon newIcon = new ImageIcon(ImageUtilities.loadImage(iconPath));
            icon[isStatic?1:0][level] = newIcon;
            return newIcon;            
        }

        @Override
        public CompletionTask createDocumentationTask() {
            return new AsyncCompletionTask(new JavaElementDocQuery(elementHandle), EditorRegistry.lastFocusedComponent());
        }
        
        private static final int PUBLIC_LEVEL = 3;
        private static final int PROTECTED_LEVEL = 2;
        private static final int PACKAGE_LEVEL = 1;
        private static final int PRIVATE_LEVEL = 0;

        private static int getProtectionLevel(Set<Modifier> modifiers) {
            if (modifiers.contains(Modifier.PUBLIC)) {
                return PUBLIC_LEVEL;
            }
            if (modifiers.contains(Modifier.PROTECTED)) {
                return PROTECTED_LEVEL;
            }
            if (modifiers.contains(Modifier.PRIVATE)) {
                return PRIVATE_LEVEL;
            }
            return PACKAGE_LEVEL;
        }

        static class ParamDesc {

            private String fullTypeName;
            private String typeName;
            private String name;

            public ParamDesc(String fullTypeName, String typeName, String name) {
                this.fullTypeName = fullTypeName;
                this.typeName = typeName;
                this.name = name;
            }
        }

}
