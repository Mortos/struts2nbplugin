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

package org.netbeans.modules.web.frameworks.struts2.utils;

import java.util.Iterator;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;
import javax.swing.text.Document;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ElementUtilities;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.SourceUtils;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public final class JavaUtils {
    private JavaUtils() {
    }
    
    // copied from Spring impl
    public static JavaSource getJavaSource(FileObject fileObject) {
        if (fileObject == null) {
            return null;
        }
        Project project = FileOwnerQuery.getOwner(fileObject);
        if (project == null) {
            return null;
        }
        // XXX this only works correctly with projects with a single sourcepath,
        // but we don't plan to support another kind of projects anyway (what about Maven?).
        SourceGroup[] sourceGroups = ProjectUtils.getSources(project).getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        for (SourceGroup sourceGroup : sourceGroups) {
            return JavaSource.create(ClasspathInfo.create(sourceGroup.getRootFolder()));
        }
        return null;
    }
    
    public static JavaSource getJavaSource(Document document) {
        return getJavaSource(NbEditorUtilities.getFileObject(document));
    }
    
    public static boolean isAccessibleClass(TypeElement typeElement) {
        NestingKind nestingKind = typeElement.getNestingKind();
        return (nestingKind == NestingKind.TOP_LEVEL)
                || (nestingKind == NestingKind.MEMBER && typeElement.getModifiers().contains(Modifier.STATIC));
    }
    
    public static CharSequence getElementName(Element el, boolean fqn) {
        if (el == null || el.asType().getKind() == TypeKind.NONE)
            return "";
        return new ElementNameVisitor().visit(el, fqn);
    }
    
    private static class ElementNameVisitor extends SimpleElementVisitor6<StringBuilder,Boolean> {
        
        private ElementNameVisitor() {
            super(new StringBuilder());
        }

        @Override
        public StringBuilder visitPackage(PackageElement e, Boolean p) {
            return DEFAULT_VALUE.append((p ? e.getQualifiedName() : e.getSimpleName()).toString());
        }

	@Override
        public StringBuilder visitType(TypeElement e, Boolean p) {
            return DEFAULT_VALUE.append((p ? e.getQualifiedName() : e.getSimpleName()).toString());
        }        
    }
    
    public static CharSequence getTypeName(TypeMirror type, boolean fqn) {
        return getTypeName(type, fqn, false);
    }
    
    public static CharSequence getTypeName(TypeMirror type, boolean fqn, boolean varArg) {
	if (type == null)
            return "";
        return new TypeNameVisitor(varArg).visit(type, fqn);
    }
    
    private static final String UNKNOWN = "<unknown>"; //NOI18N
    private static final String CAPTURED_WILDCARD = "<captured wildcard>"; //NOI18N
    
    private static class TypeNameVisitor extends SimpleTypeVisitor6<StringBuilder,Boolean> {
        
        private boolean varArg;
        
        private TypeNameVisitor(boolean varArg) {
            super(new StringBuilder());
            this.varArg = varArg;
        }
        
        @Override
        public StringBuilder defaultAction(TypeMirror t, Boolean p) {
            return DEFAULT_VALUE.append(t);
        }
        
        @Override
        public StringBuilder visitDeclared(DeclaredType t, Boolean p) {
            Element e = t.asElement();
            if (e instanceof TypeElement) {
                TypeElement te = (TypeElement)e;
                DEFAULT_VALUE.append((p ? te.getQualifiedName() : te.getSimpleName()).toString());
                Iterator<? extends TypeMirror> it = t.getTypeArguments().iterator();
                if (it.hasNext()) {
                    DEFAULT_VALUE.append("<"); //NOI18N
                    while(it.hasNext()) {
                        visit(it.next(), p);
                        if (it.hasNext())
                            DEFAULT_VALUE.append(", "); //NOI18N
                    }
                    DEFAULT_VALUE.append(">"); //NOI18N
                }
                return DEFAULT_VALUE;                
            } else {
                return DEFAULT_VALUE.append(UNKNOWN); //NOI18N
            }
        }
                        
        @Override
        public StringBuilder visitArray(ArrayType t, Boolean p) {
            boolean isVarArg = varArg;
            varArg = false;
            visit(t.getComponentType(), p);
            return DEFAULT_VALUE.append(isVarArg ? "..." : "[]"); //NOI18N
        }

        @Override
        public StringBuilder visitTypeVariable(TypeVariable t, Boolean p) {
            Element e = t.asElement();
            if (e != null) {
                String name = e.getSimpleName().toString();
                if (!CAPTURED_WILDCARD.equals(name))
                    return DEFAULT_VALUE.append(name);
            }
            DEFAULT_VALUE.append("?"); //NOI18N
            TypeMirror bound = t.getLowerBound();
            if (bound != null && bound.getKind() != TypeKind.NULL) {
                DEFAULT_VALUE.append(" super "); //NOI18N
                visit(bound, p);
            } else {
                bound = t.getUpperBound();
                if (bound != null && bound.getKind() != TypeKind.NULL) {
                    DEFAULT_VALUE.append(" extends "); //NOI18N
                    if (bound.getKind() == TypeKind.TYPEVAR)
                        bound = ((TypeVariable)bound).getLowerBound();
                    visit(bound, p);
                }
            }
            return DEFAULT_VALUE;
        }

        @Override
        public StringBuilder visitWildcard(WildcardType t, Boolean p) {
            DEFAULT_VALUE.append("?"); //NOI18N
            TypeMirror bound = t.getSuperBound();
            if (bound == null) {
                bound = t.getExtendsBound();
                if (bound != null) {
                    DEFAULT_VALUE.append(" extends "); //NOI18N
                    if (bound.getKind() == TypeKind.WILDCARD)
                        bound = ((WildcardType)bound).getSuperBound();
                    visit(bound, p);
                } else {
                    bound = SourceUtils.getBound(t);
                    if (bound != null && (bound.getKind() != TypeKind.DECLARED || !((TypeElement)((DeclaredType)bound).asElement()).getQualifiedName().contentEquals("java.lang.Object"))) { //NOI18N
                        DEFAULT_VALUE.append(" extends "); //NOI18N
                        visit(bound, p);
                    }
                }
            } else {
                DEFAULT_VALUE.append(" super "); //NOI18N
                visit(bound, p);
            }
            return DEFAULT_VALUE;
        }

        @Override
        public StringBuilder visitError(ErrorType t, Boolean p) {
            Element e = t.asElement();
            if (e instanceof TypeElement) {
                TypeElement te = (TypeElement)e;
                return DEFAULT_VALUE.append((p ? te.getQualifiedName() : te.getSimpleName()).toString());
            }
            return DEFAULT_VALUE;
        }
    }
    
    public static int getImportanceLevel(String fqn) {
        int weight = 50;
        if (fqn.startsWith("java.lang") || fqn.startsWith("java.util")) // NOI18N
            weight -= 10;
        else if (fqn.startsWith("org.omg") || fqn.startsWith("org.apache")) // NOI18N
            weight += 10;
        else if (fqn.startsWith("com.sun") || fqn.startsWith("com.ibm") || fqn.startsWith("com.apple")) // NOI18N
            weight += 20;
        else if (fqn.startsWith("sun") || fqn.startsWith("sunw") || fqn.startsWith("netscape")) // NOI18N
            weight += 30;
        return weight;
    }
    
     public static TypeElement findClassElementByBinaryName(final String binaryName, CompilationController cc) {
        if (!binaryName.contains("$")) { // NOI18N
            // fast search based on fqn
            return cc.getElements().getTypeElement(binaryName);
        } else {
            // get containing package
            String packageName = ""; // NOI18N
            int dotIndex = binaryName.lastIndexOf("."); // NOI18N
            if (dotIndex != -1) {
                packageName = binaryName.substring(0, dotIndex);
            }
            PackageElement packElem = cc.getElements().getPackageElement(packageName);
            if (packElem == null) {
                return null;
            }

            // scan for element matching the binaryName
            return new BinaryNameTypeScanner().visit(packElem, binaryName);
        }
    }
    
    private static class BinaryNameTypeScanner extends SimpleElementVisitor6<TypeElement, String> {

        @Override
        public TypeElement visitPackage(PackageElement packElem, String binaryName) {
            for(Element e : packElem.getEnclosedElements()) {
                if(e.getKind().isClass()) {
                    TypeElement ret = e.accept(this, binaryName);
                    if(ret != null) {
                        return ret;
                    }
                }
            }
            
            return null;
        }

        @Override
        public TypeElement visitType(TypeElement typeElement, String binaryName) {
            String bName = ElementUtilities.getBinaryName(typeElement);
            if(binaryName.equals(bName)) {
                return typeElement;
            } else if(binaryName.startsWith(bName)) {
                for(Element child : typeElement.getEnclosedElements()) {
                    if(!child.getKind().isClass()) {
                        continue;
                    }
                    
                    TypeElement retVal = child.accept(this, binaryName);
                    if(retVal != null) {
                        return retVal;
                    }
                }
            }
            
            return null;
        }
        
    }
    
    public static boolean isExceptionType(TypeElement te) {
        while(te.getSuperclass() != null) {
            TypeMirror tm = te.getSuperclass();
            if(tm.getKind() != TypeKind.DECLARED) {
                return false;
            }
            DeclaredType dt = (DeclaredType) tm;
            TypeElement currElem = (TypeElement) dt.asElement();
            if(currElem.getQualifiedName().toString().equals("java.lang.Exception")) { // NOI18N
                return true;
            }
            
            te = currElem;
        }
        
        return false;
    }
}
