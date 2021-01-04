package me.lilmayu.mayuCrypto.main.utils;

import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

/**
 * Code from MurKoin, edited by lilmayu
 */

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
        String path = this.file.getAbsoluteFile().getParent() + File.separator;
        op.addImage(this.file.getAbsolutePath());
        op.addImage(path + output);
        convert.run(op);
        return new Image(path + output);
    }

    public Image convertTransparent(String output) throws InterruptedException, IOException, IM4JavaException {
        ConvertCmd convert = new ConvertCmd();
        IMOperation op = new IMOperation();
        String path = this.file.getAbsoluteFile().getParent() + File.separator;
        //op.background("none");
        op.addImage(this.file.getAbsolutePath());
        op.addImage(path + output);
        Logger.debug("op: '" + op.toString() + "'");
        convert.run(op);
        return new Image(path + output);
    }

    public Image convertToPng(String output) throws IOException, TranscoderException {
        String path = Main.getChartManager().getPathToCharts();
        String svg_URI_input = Paths.get("temp.svg").toUri().toURL().toString();
        TranscoderInput input_svg_image = new TranscoderInput(svg_URI_input);
        //Step-2: Define OutputStream to PNG Image and attach to TranscoderOutput
        OutputStream png_ostream = new FileOutputStream(path + output);
        TranscoderOutput output_png_image = new TranscoderOutput(png_ostream);
        // Step-3: Create PNGTranscoder and define hints if required
        PNGTranscoder my_converter = new PNGTranscoder();
        // Step-4: Convert and Write output
        my_converter.transcode(input_svg_image, output_png_image);
        // Step 5- close / flush Output Stream
        png_ostream.flush();
        png_ostream.close();
        return new Image(path + output);
    }
}
