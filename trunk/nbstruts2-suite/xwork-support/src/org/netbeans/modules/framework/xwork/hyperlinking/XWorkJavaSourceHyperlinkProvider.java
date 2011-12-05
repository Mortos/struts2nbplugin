package org.netbeans.modules.framework.xwork.hyperlinking;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.netbeans.modules.framework.xwork.completion.XWorkJavaCompletionContext;
import org.netbeans.modules.framework.xwork.completion.annotation.DetectionUserTask;
import org.netbeans.modules.framework.xwork.completion.annotation.JavaSourceCompletionPoint;
import org.netbeans.modules.framework.xwork.completion.validator.XWorkValidatorTypeAttributeValueCompletor;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.spi.ParseException;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
@MimeRegistration(mimeType = "text/x-java", service = HyperlinkProvider.class)
public class XWorkJavaSourceHyperlinkProvider implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int caretOffset) {
        try {
            Source source = Source.create(document);
            DetectionUserTask detectionTask = new DetectionUserTask(document, caretOffset);
            Future<Void> result = ParserManager.parseWhenScanFinished(
                    Collections.singletonList(source), detectionTask);
            result.get();

            return JavaSourceCompletionPoint.CUSTOM_VALIDATOR_TYPE.equals(detectionTask.getCompletionPoint());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int caretOffset) {
        try {
            XWorkJavaCompletionContext context = new XWorkJavaCompletionContext(document, caretOffset);
            return new int[]{context.offset(), context.endOffset()};
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new int[]{caretOffset, caretOffset};
    }

    @Override
    public void performClickAction(Document document, int caretOffset) {
        try {
            XWorkJavaCompletionContext context = new XWorkJavaCompletionContext(document, caretOffset);
            XWorkValidatorTypeAttributeValueCompletor completor = new XWorkValidatorTypeAttributeValueCompletor(context);
            FileObject sourceFile = completor.getChoiceOrigin(context.text().toString());
            if (sourceFile != null) {
                openFileInEditor(sourceFile);
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void openFileInEditor(FileObject file) {
        try {
            DataObject dataObject = DataObject.find(file);
            EditorCookie editorCookie = dataObject.getCookie(EditorCookie.class);
            if (editorCookie != null) {
                editorCookie.open();
            }
        } catch (DataObjectNotFoundException ex) {
            Logger.getLogger("global").log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
