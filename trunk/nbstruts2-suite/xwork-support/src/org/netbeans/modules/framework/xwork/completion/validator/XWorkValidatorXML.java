package org.netbeans.modules.framework.xwork.completion.validator;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Aleh
 */
public enum XWorkValidatorXML {

    CONFIGURATION("text/x-xwork-validator-config+xml") {

        {
            docTypes.add("-//OpenSymphony Group//XWork Validator Config 1.0//EN");
        }
    },
    VALIDATION("text/x-xwork-validator+xml") {

        {
            docTypes.add("-//OpenSymphony Group//XWork Validator 1.0//EN");
            docTypes.add("-//OpenSymphony Group//XWork Validator 1.0.2//EN");
            docTypes.add("-//OpenSymphony Group//XWork Validator 1.0.3//EN");
        }
    };
    private String mimeType;
    protected List<String> docTypes = new LinkedList<String>();

    private XWorkValidatorXML(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isMimeType(String testMimeType) {
        return mimeType.equals(testMimeType);
    }

    public boolean isDocType(String testDocType) {
        return docTypes.contains(testDocType);
    }
}
