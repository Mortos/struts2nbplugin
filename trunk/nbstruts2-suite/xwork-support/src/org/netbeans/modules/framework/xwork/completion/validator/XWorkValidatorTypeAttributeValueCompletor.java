package org.netbeans.modules.framework.xwork.completion.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.WeakHashMap;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorTypeAttributeValueCompletor implements XWorkValidatorCompletor {

    private static final String DEFAULT_XWORK_VALIDATORS_CONFIGURATION = "com/opensymphony/xwork2/validator/validators/default.xml";
    private static final String CUSTOM_XWORK_VALIDATORS_CONFIGURATION = "validators.xml";
    private static final WeakHashMap<FileObject, XWorkValidatorConfigurationScaner> scaners = new WeakHashMap<FileObject, XWorkValidatorConfigurationScaner>();
    private Set<String> choises = new HashSet<String>();
    private String typedText;

    public XWorkValidatorTypeAttributeValueCompletor(XWorkValidatorValueCompletionContext context) {
        typedText = context.typedText().toString();

        ClassPath compileClassPath = ClassPath.getClassPath(context.file(), ClassPath.COMPILE);
        FileObject xworkLibraryConfig = compileClassPath.findResource(DEFAULT_XWORK_VALIDATORS_CONFIGURATION);
        extractValidatorNames(xworkLibraryConfig);

        ClassPath sourceClassPath = ClassPath.getClassPath(context.file(), ClassPath.SOURCE);
        FileObject xworkSourceConfig = sourceClassPath.findResource(CUSTOM_XWORK_VALIDATORS_CONFIGURATION);
        extractValidatorNames(xworkSourceConfig);
    }

    @Override
    public Collection<XWorkValidatorCompletionItem> items() {
        Collection<XWorkValidatorCompletionItem> result = new LinkedList<XWorkValidatorCompletionItem>();
        for (String choise : choises) {
            if (choise.startsWith(typedText)) {
                result.add(new XWorkValidatorAttributeCompletionItem(choise));
            }
        }
        return result;
    }

    private void extractValidatorNames(FileObject file) {
        if (!isValidConfig(file)) {
            return;
        }

        XWorkValidatorConfigurationScaner scaner;
        if (scaners.containsKey(file)) {
            scaner = scaners.get(file);
        } else {
            scaner = new XWorkValidatorConfigurationScaner(file);
            file.addFileChangeListener(scaner);
            scaners.put(file, scaner);
        }

        choises.addAll(scaner.getChoises());
    }

    private boolean isValidConfig(FileObject file) {
        return (file != null)
                && file.isValid()
                && XWorkValidatorXML.CONFIGURATION.isMimeType(file.getMIMEType());
    }
}
