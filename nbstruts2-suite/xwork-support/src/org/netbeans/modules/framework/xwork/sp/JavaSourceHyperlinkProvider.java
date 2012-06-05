package org.netbeans.modules.framework.xwork.sp;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.netbeans.modules.framework.xwork.XWorkMimeType;
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
@MimeRegistration(mimeType = XWorkMimeType.JAVA_SOURCE_MIME, service = HyperlinkProvider.class)
public class JavaSourceHyperlinkProvider implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int caretOffset) {

        JTextComponent target = EditorRegistry.lastFocusedComponent();
        final StyledDocument styledDoc = (StyledDocument) target.getDocument();
        if (styledDoc == null) {
            return false;
        }

        // Work only with the open editor 
        //and the editor has to be the active component:
        if ((target == null) || (target.getDocument() != document)) {
            return false;
        }

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
        XWorkJavaCompletionContext context = new XWorkJavaCompletionContext((AbstractDocument) document, caretOffset);
        context.init();
        return new int[]{context.getInnerStartOffset(), context.getInnerEndOffset()};
    }

    @Override
    public void performClickAction(Document document, int caretOffset) {
        XWorkJavaCompletionContext context = new XWorkJavaCompletionContext((AbstractDocument) document, caretOffset);
        context.init();
        XWorkValidatorTypeAttributeValueCompletor completor = new XWorkValidatorTypeAttributeValueCompletor(context);
        FileObject sourceFile = completor.getChoiceOrigin(context.getInnerContent().toString());
        if (sourceFile != null) {
            openFileInEditor(sourceFile);
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
