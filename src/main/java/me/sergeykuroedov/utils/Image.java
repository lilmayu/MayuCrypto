package me.sergeykuroedov.utils;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;

public class Image {
    private File file;

    public Image(File file) {
        this.file = file;
    }

    public Image(String pathToFile) {
        this.file = new File(pathToFile);
    }

    public File getFile() {
        return file;
    }

    public Image convert(String output) throws InterruptedException, IOException, IM4JavaException {
        ConvertCmd convert = new ConvertCmd();
        IMOperation op = new IMOperation();
        String path = this.file.getAbsoluteFile().getParent()+File.separator;
        op.addImage(this.file.getAbsolutePath());
        op.addImage(path+output);
        convert.run(op);
        return new Image(path+output);
    }

    public Image convertTransparent(String output) throws InterruptedException, IOException, IM4JavaException {
        ConvertCmd convert = new ConvertCmd();
        IMOperation op = new IMOperation();
        String path = this.file.getAbsoluteFile().getParent()+File.separator;
        op.background("none");
        op.addImage(this.file.getAbsolutePath());
        op.addImage(path+output);
        convert.run(op);
        return new Image(path+output);
    }
}
