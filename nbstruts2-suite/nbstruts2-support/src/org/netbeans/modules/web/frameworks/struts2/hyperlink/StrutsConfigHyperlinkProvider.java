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
package org.netbeans.modules.web.frameworks.struts2.hyperlink;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.TypeUtilities;
import org.netbeans.api.java.source.ui.ElementOpen;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.TokenItem;
import org.netbeans.editor.Utilities;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.web.api.webmodule.WebModule;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.*;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsQNames;
import org.netbeans.modules.xml.text.syntax.SyntaxElement;
import org.netbeans.modules.xml.text.syntax.XMLSyntaxSupport;
import org.netbeans.modules.xml.text.syntax.dom.EmptyTag;
import org.netbeans.modules.xml.text.syntax.dom.StartTag;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.netbeans.modules.xml.text.syntax.dom.TextImpl;
import org.netbeans.modules.xml.xam.Model.State;
import org.netbeans.modules.xml.xam.ModelSource;
import org.netbeans.modules.xml.xam.locator.CatalogModelException;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.NbBundle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Provides hyperlinking for Struts2 Configuration files
 *
 * @author Sujit Nair
 */
public class StrutsConfigHyperlinkProvider implements HyperlinkProvider {

    private static final int INVALID_OFFSET = -1;
    private String argString;
    private String tagString;
    private String valueString;
    private int startOffset;
    private int endOffset;
    private BaseDocument lastDocument;
    private String packageName;

    private enum HyperlinkType {

        NULL,
        GOTO_INTERCEPTOR,
        GOTO_ACTION,
        GOTO_PACKAGE,
        GOTO_RESULT_TYPE,
        GOTO_RESULT,
        OPEN_JAVA,
        OPEN_JAVA_METHOD,
        OPEN_NONJAVA
    }

    private enum PackageElements {

        INTERCEPTOR_REF("interceptor-ref"),
        DEFAULT_INTERCEPTOR_REF("default-interceptor-ref"),
        DEFAULT_ACTION_REF("default-action-ref"),
        PACKAGE("package"),
        RESULT_TYPES("result_types"),
        INTERCEPTORS("interceptors"),
        GLOBAL_RESULTS("global-results"),
        GLOBAL_EXCEPTION_MAPPINGS("global-exception-mappings"),
        ACTION("action"),
        RESULT_TYPE("result-type"),
        INTERCEPTOR("interceptor"),
        INTERCEPTOR_STACK("interceptor-stack"),
        RESULT("result"),
        EXCEPTION_MAPPING("exception-mapping");
        private String abbreviation;

        PackageElements(String abbr) {
            this.abbreviation = abbr;
        }

        private String getAbbreviation() {
            return this.abbreviation;
        }
    }
    private HyperlinkType linkType = HyperlinkType.NULL;
    private PackageElements[] packElements = PackageElements.values();

    public StrutsConfigHyperlinkProvider() {
        lastDocument = null;
    }

    @Override
    public boolean isHyperlinkPoint(Document document, int offset) {

        if (!(document instanceof BaseDocument)) {
            return false;
        }

        BaseDocument doc = (BaseDocument) document;
        JTextComponent target = Utilities.getFocusedComponent();

        if ((target == null) || (target.getDocument() != doc)) {
            return false;
        }

        return processHyperlinkPoint(doc, offset);
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int position) {
        if (!(document instanceof BaseDocument)) {
            return null;
        }

        BaseDocument bdoc = (BaseDocument) document;
        JTextComponent target = Utilities.getFocusedComponent();

        if ((target == null) || (lastDocument != bdoc)) {
            return null;
        }

        return new int[]{startOffset, endOffset};
    }

    @Override
    public void performClickAction(Document document, int offset) {
        try {
            int entryOffset = offset; // have to set it to current location in document
            FileObject fo = NbEditorUtilities.getFileObject(document);
            FileObject foDup = fo;
            ModelSource source = org.netbeans.modules.xml.retriever.catalog.Utilities.createModelSource(fo, true);
            StrutsModel model = StrutsModelFactory.getInstance().getModel(source);
            if (model.getState() != State.VALID) {
                return;
            }

            Struts struts = model.getRootComponent();

            // 1. Get all packages
            List<StrutsPackage> packages = struts.getPackages();

            // Get the package to which the element/tag belongs
            StrutsPackage refPackage = getCurrentPackage(packages, packageName);

            switch (linkType) {

                case GOTO_INTERCEPTOR:
                    while (refPackage != null) {
                        Interceptors interceptors = refPackage.getInterceptors();
                        if (interceptors != null) {
                            entryOffset = findInterceptorInInterceptors(interceptors);
                        }
                        if (entryOffset != INVALID_OFFSET) {
                            foDup = refPackage.getModel().getModelSource().getLookup().lookup(FileObject.class);
                            break;
                        }
                        refPackage = refPackage.getParentPackage().get();
                    }
                    break;

                case GOTO_PACKAGE:
                    if (refPackage.getParentPackage().get() != null) {
                        entryOffset = refPackage.getParentPackage().get().findPosition();
                        foDup = refPackage.getParentPackage().get().getModel().getModelSource().getLookup().lookup(FileObject.class);
                    }
                    break;

                case GOTO_ACTION:
                    while (refPackage != null) {
                        entryOffset = findActionInPackage(refPackage);
                        if (entryOffset != INVALID_OFFSET) {
                            foDup = refPackage.getModel().getModelSource().getLookup().lookup(FileObject.class);
                            break;
                        }
                        refPackage = refPackage.getParentPackage().get();
                    }
                    break;

                case GOTO_RESULT:
                    while (refPackage != null) {
                        GlobalResults globalResults = refPackage.getGlobalResults();
                        if (globalResults != null) {
                            entryOffset = findResultInResultsProvider(globalResults);
                        }
                        if (entryOffset == INVALID_OFFSET) {
                            List<Action> actions = refPackage.getActions();
                            if (!actions.isEmpty()) {
                                for (Action action : actions) {
                                    entryOffset = findResultInResultsProvider(action);
                                }
                            }
                        }

                        if (entryOffset != INVALID_OFFSET) {
                            foDup = refPackage.getModel().getModelSource().getLookup().lookup(FileObject.class);
                            break;
                        }
                        refPackage = refPackage.getParentPackage().get();
                    }
                    break;

                case GOTO_RESULT_TYPE:
                    while (refPackage != null) {
                        ResultTypes resultTypes = refPackage.getResultTypes();
                        if (resultTypes != null) {
                            entryOffset = findResultTypeInResultTypes(resultTypes);
                        }
                        if (entryOffset != INVALID_OFFSET) {
                            foDup = refPackage.getModel().getModelSource().getLookup().lookup(FileObject.class);
                            break;
                        }
                        refPackage = refPackage.getParentPackage().get();
                    }
                    break;

                case OPEN_JAVA:
                    findAndOpenJavaClass(valueString, document);
                    break;

                //TODO    
                case OPEN_JAVA_METHOD:
                    if (argString != null) {
                        findAndOpenJavaClassMethod(argString, valueString, document);
                    }
                    break;

                case OPEN_NONJAVA:
                    openNonJavaFile(valueString, document);
                    break;

            }

            // Defaulting entryOffset to point to current location if element/tag not found.
            if (entryOffset == INVALID_OFFSET) {
                entryOffset = offset;
            }

            // Jump and point to the declaration of element/tag
            if (foDup == fo) {
                JTextComponent editorComp = Utilities.getFocusedComponent();
                editorComp.setCaretPosition(entryOffset);
            } else {
                openInIncludedFiles(foDup, entryOffset);
            }

        } catch (CatalogModelException cme) {
            // nothing - no jump action
        }
    }

    private StrutsPackage getCurrentPackage(List<StrutsPackage> packages, String packName) {
        if (!packages.isEmpty()) {
            for (StrutsPackage p : packages) {
                if (p.getName().equals(packName)) {
                    return p;
                }
            }
        }
        return null;
    }

    private void openInIncludedFiles(FileObject foDup, int entryOffset) {
        DataObject dObj = null;
        try {
            dObj = DataObject.find(foDup);
        } catch (DataObjectNotFoundException ex) {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.FINE, ex.getMessage(), ex);
        }
        if (dObj != null) {
            EditorCookie ec = (EditorCookie) dObj.getLookup().lookup(EditorCookie.class);
            if (ec != null) {
                ec.open();
                JEditorPane[] panes = ec.getOpenedPanes();
                if (panes.length > 0) {
                    panes[0].setCaretPosition(entryOffset);
                }
            }
        }
    }

    private void findAndOpenJavaClass(final String fqn, Document doc) {
        FileObject fo = NbEditorUtilities.getFileObject(doc);
        if (fo != null) {
            WebModule wm = WebModule.getWebModule(fo);
            if (wm != null) {
                try {
                    final ClasspathInfo cpi = ClasspathInfo.create(wm.getDocumentBase());
                    JavaSource js = JavaSource.create(cpi, Collections.<FileObject>emptyList());
                    js.runUserActionTask(new Task<CompilationController>() {
                        @Override
                        public void run(CompilationController cc) throws Exception {
                            Elements elements = cc.getElements();
                            if (elements != null) {
                                TypeElement element = elements.getTypeElement(fqn.trim());
                                if (element != null) {
                                    if (!ElementOpen.open(cpi, element)) {
                                        String key = "goto_source_not_found"; // NOI18N
                                        String msg = NbBundle.getMessage(StrutsConfigHyperlinkProvider.class, key);
                                        org.openide.awt.StatusDisplayer.getDefault().setStatusText(MessageFormat.format(msg, new Object[]{fqn}));

                                    }
                                }
                            }


                        }
                    }, false);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    private void findAndOpenJavaClassMethod(final String fileName, final String methodName, Document doc) {
        FileObject fo = NbEditorUtilities.getFileObject(doc);
        if (fo != null) {
            WebModule wm = WebModule.getWebModule(fo);
            if (wm != null) {
                try {
                    final ClasspathInfo cpi = ClasspathInfo.create(wm.getDocumentBase());
                    JavaSource js = JavaSource.create(cpi, Collections.<FileObject>emptyList());
                    js.runUserActionTask(new Task<CompilationController>() {
                        @Override
                        public void run(CompilationController cc) throws Exception {
                            Elements elements = cc.getElements();
                            if (elements != null) {
                                TypeElement element = elements.getTypeElement(fileName.trim());
                                if (element != null) {

                                    Element methodElement = null;
                                    List<? extends Element> enclosedElements = element.getEnclosedElements();
                                    for (Element item : enclosedElements) {
                                        if (ElementKind.METHOD.equals(item.getKind())
                                                && (item.getSimpleName().contentEquals(methodName))
                                                && item.getModifiers().contains(Modifier.PUBLIC)) {

                                            ExecutableElement method = (ExecutableElement) item;
                                            
                                            boolean returnsString = "java.lang.String".equals(cc.getTypeUtilities().getTypeName(method.getReturnType(), TypeUtilities.TypeNameOptions.PRINT_FQN));
                                            boolean hasNoArguments = method.getParameters().isEmpty();

                                            if (returnsString && hasNoArguments) {
                                                methodElement = item;
                                                break;
                                            }
                                        }
                                    }

                                    if (methodElement != null) {
                                        if (!ElementOpen.open(cpi, methodElement)) {
                                            String key = "goto_source_not_found"; // NOI18N
                                            String msg = NbBundle.getMessage(StrutsConfigHyperlinkProvider.class, key);
                                            org.openide.awt.StatusDisplayer.getDefault().setStatusText(MessageFormat.format(msg, new Object[]{fileName + "." + methodName}));

                                        }
                                    } else {
                                        if (!ElementOpen.open(cpi, element)) {
                                            String key = "goto_source_not_found"; // NOI18N
                                            String msg = NbBundle.getMessage(StrutsConfigHyperlinkProvider.class, key);
                                            org.openide.awt.StatusDisplayer.getDefault().setStatusText(MessageFormat.format(msg, new Object[]{fileName}));

                                        }
                                    }
                                }
                            }


                        }
                    }, false);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    private void openNonJavaFile(final String relPath, Document doc) {
        FileObject fo = NbEditorUtilities.getFileObject(doc);
        FileObject parent = fo.getParent();

        FileObject targetFO = parent.getFileObject(relPath);
        if (targetFO != null) {
            try {
                DataObject dObj = DataObject.find(targetFO);
                EditorCookie editorCookie = dObj.getLookup().lookup(EditorCookie.class);
                if (editorCookie != null) {
                    editorCookie.open();
                }
            } catch (DataObjectNotFoundException ex) {
                Logger.getLogger("global").log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private int findInterceptorInInterceptors(Interceptors interceptors) {
        List<Interceptor> entries = interceptors.getInterceptors();
        for (Interceptor entry : entries) {
            if (entry.getName().equals(valueString)) {
                return (entry.findPosition());
            }
        }
        List<InterceptorStack> entriesStack = interceptors.getInterceptorStacks();
        for (InterceptorStack entry : entriesStack) {
            if (entry.getName().equals(valueString)) {
                return (entry.findPosition());
            }
        }
        return INVALID_OFFSET;
    }

    private int findActionInPackage(StrutsPackage refPackage) {
        List<Action> entries = refPackage.getActions();
        if (!entries.isEmpty()) {
            for (Action entry : entries) {
                if (entry.getName().equals(valueString)) {
                    return (entry.findPosition());
                }
            }
        }
        return INVALID_OFFSET;
    }

    private int findResultInResultsProvider(ResultsProvider resParentElement) {
        List<Result> entries = resParentElement.getResults();
        for (Result entry : entries) {
            if (entry.getName().equals(valueString)) {
                return (entry.findPosition());
            }
        }
        return INVALID_OFFSET;
    }

    private int findResultTypeInResultTypes(ResultTypes resultTypes) {
        List<ResultType> entries = resultTypes.getResultTypes();
        for (ResultType entry : entries) {
            if (entry.getName().equals(valueString)) {
                return (entry.findPosition());
            }
        }
        return INVALID_OFFSET;
    }

    private boolean isPackageElement(String tagString) {
        if (tagString == null) {
            return false;
        }
        for (PackageElements i : packElements) {
            if (tagString.equals(i.getAbbreviation())) {
                return true;
            }
        }
        return false;
    }

    private boolean processHyperlinkPoint(BaseDocument doc, int offset) {
        XMLSyntaxSupport syntaxSupport = new XMLSyntaxSupport(doc);

        try {
            TokenItem token = syntaxSupport.getTokenChain(offset, offset + 1);
            if (StrutsConfigEditorUtilities.isValueToken(token)) {
                // Attribute value string - remove " or '
                valueString = token.getImage();
                valueString = valueString.substring(0, valueString.length() - 1).substring(1);
                startOffset = token.getOffset() + 1;
                endOffset = token.getOffset() + token.getImage().length() - 1;

                // Get the argument name
                TokenItem argToken = StrutsConfigEditorUtilities.getAttributeToken(token);
                if (argToken == null) {
                    return false;
                }
                argString = argToken.getImage();

                // Get the tag name
                SyntaxElement element = syntaxSupport.getElementChain(offset);
                Tag tagElement = null;

                if (element instanceof StartTag) {
                    tagElement = (StartTag) element;
                } else if (element instanceof EmptyTag) {
                    tagElement = (EmptyTag) element;
                }
                tagString = tagElement.getTagName();
                if (isPackageElement(tagString)) {
                    packageName = StrutsConfigEditorUtilities.getPackageName(tagElement);
                }

                lastDocument = doc;

                // Test the type of hyperlink
                // 1. Tag: INTERCEPTOR_REF Attribute: NAME
                if (tagString.equals(StrutsQNames.INTERCEPTOR_REF.getLocalName())) {
                    if (argString.equals(StrutsAttributes.NAME.getName())) {
                        linkType = HyperlinkType.GOTO_INTERCEPTOR;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.PACKAGE.getLocalName())) {
                    if (argString.equals(StrutsAttributes.EXTENDS.getName())) {
                        linkType = HyperlinkType.GOTO_PACKAGE;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.DEFAULT_INTERCEPTOR_REF.getLocalName())) {
                    if (argString.equals(StrutsAttributes.NAME.getName())) {
                        linkType = HyperlinkType.GOTO_INTERCEPTOR;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.DEFAULT_ACTION_REF.getLocalName())) {
                    if (argString.equals(StrutsAttributes.NAME.getName())) {
                        linkType = HyperlinkType.GOTO_ACTION;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.RESULT.getLocalName())) {
                    if (argString.equals(StrutsAttributes.TYPE.getName())) {
                        linkType = HyperlinkType.GOTO_RESULT_TYPE;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.EXCEPTION_MAPPING.getLocalName())) {
                    if (argString.equals(StrutsAttributes.RESULT.getName())) {
                        linkType = HyperlinkType.GOTO_RESULT;
                        return true;
                    } else if (argString.equals(StrutsAttributes.EXCEPTION.getName())) {
                        linkType = HyperlinkType.OPEN_JAVA;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.INCLUDE.getLocalName())) {
                    if (argString.equals(StrutsAttributes.FILE.getName())) {
                        linkType = HyperlinkType.OPEN_NONJAVA;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.BEAN.getLocalName())) {
                    if (argString.equals(StrutsAttributes.TYPE.getName()) || argString.equals(StrutsAttributes.CLASS.getName())) {
                        linkType = HyperlinkType.OPEN_JAVA;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.RESULT_TYPE.getLocalName()) || tagString.equals(StrutsQNames.INTERCEPTOR.getLocalName())) {
                    if (argString.equals(StrutsAttributes.CLASS.getName())) {
                        linkType = HyperlinkType.OPEN_JAVA;
                        return true;
                    }
                } else if (tagString.equals(StrutsQNames.ACTION.getLocalName())) {
                    if (argString.equals(StrutsAttributes.METHOD.getName())) {
                        // Re-using argString to store the name of the class
                        argString = tagElement.getAttribute("class");
                        linkType = HyperlinkType.OPEN_JAVA_METHOD;
                        return true;
                    }
                    if (argString.equals(StrutsAttributes.CLASS.getName())) {
                        linkType = HyperlinkType.OPEN_JAVA;
                        return true;
                    }
                }
            } else if (StrutsConfigEditorUtilities.isTextToken(token)) {
                // Get the element value and the offset
                valueString = token.getImage();
                startOffset = token.getOffset();
                endOffset = token.getOffset() + token.getImage().length() - 1;

                // Get the tag name
                SyntaxElement element = syntaxSupport.getElementChain(offset);
                Node parentText;
                if (element instanceof TextImpl) {
                    TextImpl textData = (TextImpl) element;

                    // Check for attempted clicks on empty string values
                    if (!textData.getData().trim().equals("")) {
                        parentText = textData.getParentNode();
                        tagString = parentText.getNodeName();
                    } else {
                        return false;
                    }

                    if (isPackageElement(tagString)) {
                        packageName = StrutsConfigEditorUtilities.getPackageName(textData);
                    }

                    if (tagString.equals(StrutsQNames.RESULT.getLocalName())) {
                        NamedNodeMap attList = parentText.getAttributes();
                        Node attType = attList.getNamedItem("type");
                        if (attType != null) {
                            argString = attType.getNodeValue();
                        }
                    } else if (tagString.equals(StrutsQNames.PARAM.getLocalName())) {
                        Node grandParent = parentText.getParentNode();
                        if (grandParent.getNodeName().equals(StrutsQNames.RESULT.getLocalName())) {
                            NamedNodeMap attList = grandParent.getAttributes();
                            Node attType = attList.getNamedItem("type");
                            if (attType != null) {
                                argString = attType.getNodeValue();
                            }
                        }
                    }
                }

                lastDocument = doc;

                // 13. Check for 'Result' element and retrieve its type.
                if (tagString.equals(StrutsQNames.RESULT.getLocalName())) {
                    if (argString != null) {
                        if (argString.equals("chain")) {
                            linkType = HyperlinkType.GOTO_ACTION;
                            return true;
                        } else if (argString.equals("dispatcher") || argString.equals("velocity") || argString.equals("freemarker")) {
                            linkType = HyperlinkType.OPEN_NONJAVA;
                            return true;
                        }
                    }
                } else if (tagString.equals(StrutsQNames.PARAM.getLocalName())) {
                    if (argString != null) {
                        if (argString.equals("xslttransformation") || argString.equals("dispatcher")) {
                            linkType = HyperlinkType.OPEN_NONJAVA;
                            return true;
                        }
                    }
                }
            }
        } catch (BadLocationException ble) {
            // Nothing to do. No hyperlinking
        }

        return false;
    }
}
