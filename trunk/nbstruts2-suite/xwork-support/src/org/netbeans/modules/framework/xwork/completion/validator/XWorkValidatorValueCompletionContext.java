package org.netbeans.modules.framework.xwork.completion.validator;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh Maksimovich
 */
public class XWorkValidatorValueCompletionContext {

    private Document document;
    private FileObject file;
    private boolean valid = false;
    private int offset;
    private int typedLength;
    private CharSequence typedText;
    private CharSequence attributeName;
    private CharSequence tagName;

    public XWorkValidatorValueCompletionContext(Document document, int caretOffset) throws BadLocationException {

        this.document = document;
        file = NbEditorUtilities.getFileObject(document);

        final TokenHierarchy<Document> tokenHierarchy = TokenHierarchy.get(document);
        final TokenSequence<XMLTokenId> tokenSequence = tokenHierarchy.tokenSequence(XMLTokenId.language());
        tokenSequence.move(caretOffset);
        if (tokenSequence.moveNext()) {
            final Token<XMLTokenId> token = tokenSequence.token();
            if (hasTokenId(token, XMLTokenId.VALUE)) {
                valid = true;
                initValue(tokenHierarchy, token, caretOffset);
                initAttribute(tokenSequence);
                initTag(tokenSequence);
            }
        }
    }

    public Document document() {
        return document;
    }

    public FileObject file() {
        return file;
    }

    public boolean isValid() {
        return valid;
    }

    public int offset() {
        return offset;
    }

    public int typedLength() {
        return typedLength;
    }

    public CharSequence typedText() {
        return typedText;
    }

    public CharSequence attributeName() {
        return attributeName;
    }

    public CharSequence tagName() {
        return tagName;
    }

    private boolean hasTokenId(final Token<XMLTokenId> token, final XMLTokenId id) {
        return id.equals(token.id());
    }

    private int internalOffset(final Token<XMLTokenId> token) {
        if (hasTokenId(token, XMLTokenId.VALUE)
                || hasTokenId(token, XMLTokenId.TAG)) {
            return 1;
        }
        return 0;
    }

    private CharSequence tokenText(final Token<XMLTokenId> token) {
        CharSequence tokenChars = token.text();
        if (tokenChars.length() > 0) {
            final int startOffset = internalOffset(token);
            final int finishOffset = token.length();
            return tokenChars.subSequence(startOffset, finishOffset);
        }
        return tokenChars;
    }

    private CharSequence tokenName(final XMLTokenId type, final TokenSequence<XMLTokenId> tokenSequence) {
        do {
            Token<XMLTokenId> testToken = tokenSequence.token();
            if (type.equals(testToken.id())) {
                return tokenText(testToken);
            }
        } while (tokenSequence.movePrevious());

        return null;
    }

    private void initValue(
            final TokenHierarchy<Document> tokenHierarchy, final Token<XMLTokenId> token,
            int caretOffset) {
        final int internalOffset = internalOffset(token);
        offset = token.offset(tokenHierarchy) + internalOffset;
        typedLength = caretOffset - offset;
        if (typedLength < 0) {
            valid = false;
        } else {
            typedText = tokenText(token).subSequence(0, typedLength);
        }
    }

    private void initAttribute(final TokenSequence<XMLTokenId> tokenSequence) {
        if (valid) {
            attributeName = tokenName(XMLTokenId.ARGUMENT, tokenSequence);
        }
    }

    private void initTag(final TokenSequence<XMLTokenId> tokenSequence) {
        if (valid) {
            tagName = tokenName(XMLTokenId.TAG, tokenSequence);
        }
    }
}
