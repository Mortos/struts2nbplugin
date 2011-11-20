package org.netbeans.modules.framework.xwork.completion.resources;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public enum XWorkValidatorCompletionItemIcons {

    ATTRIBUTE_ICONS("attribute.png"),
    ELEMENT_ICONS("element.png"),
    VALUE_ICONS("value.png");
    private static final String ICONS_PACKAGE = "org/netbeans/modules/framework/xwork/completion/resources";
    private ImageIcon icon;

    private XWorkValidatorCompletionItemIcons(String iconName) {
        try {
            String resourcePath = String.format("%s/%s", ICONS_PACKAGE, iconName);
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            Image image = ImageIO.read(imageStream);
            icon = new ImageIcon(image);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            icon = null;
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
            icon = null;
        }
    }

    public ImageIcon getImageIcon() {
        return icon;
    }
}
