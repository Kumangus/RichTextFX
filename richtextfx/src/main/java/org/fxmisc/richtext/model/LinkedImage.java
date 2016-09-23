package org.fxmisc.richtext.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import javafx.scene.Node;


/**
 * A custom object which contains a file path to an image file.
 * When rendered in the rich text editor, the image is loaded from the 
 * specified file.  
 */
public class LinkedImage<S> extends CustomObject<S> {

    private /*final*/ String imagePath;

    LinkedImage() {}

    /**
     * Creates a new linked image object.
     *
     * @param imagePath The path to the image file.
     * @param style The text style to apply to the corresponding segment.
     */
    public LinkedImage(String imagePath, S style) {
        super(style); // , DefaultSegmentTypes.LINKED_IMAGE);

        // if the image is below the current working directory,
        // then store as relative path name.
        String currentDir = System.getProperty("user.dir") + File.separatorChar;
        if (imagePath.startsWith(currentDir)) {
            imagePath = imagePath.substring(currentDir.length());
        }

        this.imagePath = imagePath;
    }


    /**
     * @return The path of the image to render.
     */
    public String getImagePath() {
        return imagePath;
    }


    @Override
    public void encode(DataOutputStream os, Codec<S> styleCodec) throws IOException {
        Codec.STRING_CODEC.encode(os, imagePath);
        styleCodec.encode(os, style);
    }

    @Override
    public void decode(DataInputStream is, Codec<S> styleCodec) throws IOException {
        imagePath = Codec.STRING_CODEC.decode(is);
        style = styleCodec.decode(is);
    }

    @Override
    public String toString() {
        return String.format("LinkedImage[path=%s]", imagePath);
    }

    @SuppressWarnings("rawtypes")
    public static Function<LinkedImage, Node> nodeFactory;

    @Override
    public Node createNode() {
        return nodeFactory.apply(this);
    }

}
