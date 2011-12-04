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
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion.annotation;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.text.Document;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletionContext;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkEmptyCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkJavaCompletionContext;
import org.netbeans.modules.framework.xwork.completion.validator.XWorkValidatorTypeAttributeValueCompletor;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser.Result;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public class XWorkJavaCodeCompletionQuery extends AsyncCompletionQuery {

    @Override
    protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
        try {
            Source source = Source.create(document);
            DetectionUserTask detectionTask = new DetectionUserTask(document, caretOffset);
            Future<Void> result = ParserManager.parseWhenScanFinished(
                    Collections.singletonList(source), detectionTask);
            result.get();
            completionResultSet.addAllItems(detectionTask.getCompletor().items());

        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            completionResultSet.finish();
        }
    }
}

class DetectionUserTask extends UserTask {

    private static final String CUSTOM_VALIDATOR_ANNOTATION = "com.opensymphony.xwork2.validator.annotations.CustomValidator";
    private static final String TYPE_ATTRIBUTE = "type";
    private Document document;
    private int caretOffset;
    private XWorkCompletor completor = XWorkEmptyCompletor.instance();

    public DetectionUserTask(Document document, int caretOffset) {
        this.document = document;
        this.caretOffset = caretOffset;
    }

    @Override
    public void run(ResultIterator resultIterator) throws Exception {
        Result result = resultIterator.getParserResult(caretOffset);
        CompilationController controller = CompilationController.get(result);
        controller.toPhase(Phase.PARSED);
        int offset = controller.getSnapshot().getEmbeddedOffset(caretOffset);
        TreePath path = controller.getTreeUtilities().pathFor(offset);
        Tree currentElement = path.getLeaf();
        Tree.Kind currentElementKind = currentElement.getKind();
        if (Tree.Kind.STRING_LITERAL.equals(currentElementKind)) {
            TreePath parentPath = path.getParentPath();
            if (parentPath != null) {
                Tree parentElement = parentPath.getLeaf();
                Tree.Kind parentElementKind = parentElement.getKind();
                if (Tree.Kind.ASSIGNMENT.equals(parentElementKind)) {
                    TreePath grandParentPath = parentPath.getParentPath();
                    if (grandParentPath != null) {
                        Tree grandParentElement = grandParentPath.getLeaf();
                        Tree.Kind grandParentElementKind = grandParentElement.getKind();
                        if (Tree.Kind.ANNOTATION.equals(grandParentElementKind)) {
                            AnnotationTree annotationTree = (AnnotationTree) grandParentElement;
                            controller.toPhase(Phase.ELEMENTS_RESOLVED);
                            Trees trees = controller.getTrees();
                            TypeElement typeElement = (TypeElement) trees.getElement(new TreePath(grandParentPath, annotationTree.getAnnotationType()));
                            if (CUSTOM_VALIDATOR_ANNOTATION.equals(typeElement.getQualifiedName().toString())) {
                                AssignmentTree assignmentTree = (AssignmentTree) parentElement;
                                ExpressionTree expressionTree = assignmentTree.getVariable();
                                Element expressionElement = trees.getElement(new TreePath(parentPath, expressionTree));
                                if (TYPE_ATTRIBUTE.equals(expressionElement.getSimpleName().toString())) {
                                    XWorkCompletionContext context = new XWorkJavaCompletionContext(document, caretOffset);
                                    completor = new XWorkValidatorTypeAttributeValueCompletor(context);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public XWorkCompletor getCompletor() {
        return completor;
    }
}
