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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.PackageElement;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClassIndex.SearchScope;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import org.netbeans.modules.web.frameworks.struts2.utils.JavaUtils;
import org.openide.util.Exceptions;

/**
 *
 *  
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public abstract class JavaClassCompletor extends Completor {

    public JavaClassCompletor() {
    }

    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(final CompletionContext context) {
        final List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        try {
            final JavaSource js = JavaUtils.getJavaSource(context.getFileObject());
            if (js == null) {
                return Collections.<StrutsXMLConfigCompletionItem>emptyList();
            }

            final String typedPrefix = context.getTypedPrefix();
            final int dotIndex = typedPrefix.indexOf("."); // NOI18N
            
            js.runUserActionTask(new Task<CompilationController>() {
                public void run(CompilationController cc) throws Exception {
                    cc.toPhase(Phase.ELEMENTS_RESOLVED);
                    ClassIndex ci = cc.getJavaSource().getClasspathInfo().getClassIndex();
                    if (dotIndex != -1 || typedPrefix.equals("")) { // NOI18N
                        doNormalJavaCompletion(cc, ci, results, typedPrefix, context.getCurrentToken().getOffset() + 1);
                    } else {
                        doSmartJavaCompletion(cc, ci, results, typedPrefix, context.getCurrentToken().getOffset() + 1);
                    }
                }
            }, true);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return results;
    }
    
    protected void doNormalJavaCompletion(CompilationController cc, ClassIndex ci, 
            final List<StrutsXMLConfigCompletionItem> results,
            final String typedPrefix, final int substitutionOffset) {
        String packName = typedPrefix;
        int index = substitutionOffset;
        int dotIndex = typedPrefix.lastIndexOf("."); // NOI18N
        if (dotIndex != -1) {
            index += (dotIndex + 1);
            packName = typedPrefix.substring(0, dotIndex);
        }
        
        addPackages(ci, results, typedPrefix, index);
        
        PackageElement pkgElem = cc.getElements().getPackageElement(packName);
        if (pkgElem == null) {
            return;
        }
        
        addNormalTypes(cc, ci, results, typedPrefix, packName, pkgElem, substitutionOffset);
        setAnchorOffset(index);
    }
    
    protected void addPackages(ClassIndex ci, List<StrutsXMLConfigCompletionItem> results, String typedPrefix, int substitutionOffset) {
        Set<String> packages = ci.getPackageNames(typedPrefix, true, EnumSet.allOf(SearchScope.class));
        for(String pkg : packages) {
            StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createPackageItem(substitutionOffset, pkg, false);
            results.add(item);
        }
    }

    protected abstract void addNormalTypes(CompilationController cc, ClassIndex ci, 
            List<StrutsXMLConfigCompletionItem> results, 
            String typedPrefix, String packageName, PackageElement packageElem, int substitutionOffset);
    
    protected void doSmartJavaCompletion(CompilationController cc, ClassIndex ci, 
            final List<StrutsXMLConfigCompletionItem> results,
            final String typedPrefix, final int substitutionOffset) {
        addPackages(ci, results, typedPrefix, substitutionOffset);
        addSmartTypes(cc, ci, results, typedPrefix, typedPrefix, substitutionOffset);
        setAnchorOffset(substitutionOffset);
    }
    
    protected abstract void addSmartTypes(CompilationController cc, ClassIndex ci,
            List<StrutsXMLConfigCompletionItem> results, 
            String typedPrefix, String packageName, int substitutionOffset);
}
