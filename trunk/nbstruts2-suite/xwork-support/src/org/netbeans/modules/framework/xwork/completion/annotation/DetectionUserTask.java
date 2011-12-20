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
 * The Original Software is XWork Support. The Initial Developer of the Original
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
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.text.Document;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.spi.Parser;

/**
 *
 * @author Aleh
 */
public class DetectionUserTask extends UserTask {

    private static final String CUSTOM_VALIDATOR_ANNOTATION = "com.opensymphony.xwork2.validator.annotations.CustomValidator";
    private static final String TYPE_ATTRIBUTE = "type";
    private Document document;
    private int caretOffset;
    private JavaSourceCompletionPoint completionPoint;

    public DetectionUserTask(Document document, int caretOffset) {
        this.document = document;
        this.caretOffset = caretOffset;
    }

    @Override
    public void run(ResultIterator resultIterator) throws Exception {
        Parser.Result result = resultIterator.getParserResult(caretOffset);
        CompilationController controller = CompilationController.get(result);
        controller.toPhase(JavaSource.Phase.PARSED);
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
                            controller.toPhase(JavaSource.Phase.ELEMENTS_RESOLVED);
                            Trees trees = controller.getTrees();
                            TypeElement typeElement = (TypeElement) trees.getElement(new TreePath(grandParentPath, annotationTree.getAnnotationType()));
                            if (CUSTOM_VALIDATOR_ANNOTATION.equals(typeElement.getQualifiedName().toString())) {
                                AssignmentTree assignmentTree = (AssignmentTree) parentElement;
                                ExpressionTree expressionTree = assignmentTree.getVariable();
                                Element expressionElement = trees.getElement(new TreePath(parentPath, expressionTree));
                                if (TYPE_ATTRIBUTE.equals(expressionElement.getSimpleName().toString())) {
                                    completionPoint = JavaSourceCompletionPoint.CUSTOM_VALIDATOR_TYPE;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public JavaSourceCompletionPoint getCompletionPoint() {
        return completionPoint;
    }
}
