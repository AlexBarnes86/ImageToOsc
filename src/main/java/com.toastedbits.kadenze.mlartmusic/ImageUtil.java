package com.toastedbits.kadenze.mlartmusic;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ImageUtil {
    private final static Logger log = LoggerFactory.getLogger(ImageUtil.class);

    public static Image drawGridLines(final Image image, final int fitWidth, final int fitHeight, final int divisions) {
        checkNotNull(image);
        checkArgument(fitWidth > 0, "Width must be greater than 0");
        checkArgument(fitHeight > 0, "Height must be greater than 0");
        checkArgument(divisions > 0, "Divisions must be greater than 0");

        final Canvas canvas = new Canvas(fitWidth,fitHeight);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.drawImage(image, 0, 0, fitWidth, fitHeight);

        int gapWidth = fitWidth / divisions;
        int gapHeight = fitHeight / divisions;

        context.setLineWidth(1);
        context.setStroke(Color.BLACK);
        for(int i = 0; i < divisions; ++i) {
            context.strokeLine(gapWidth*i, 0, gapWidth*i, fitHeight);
        }
        for(int i = 0; i < divisions; ++i) {
            context.strokeLine(0, gapHeight*i, fitWidth, gapHeight*i);
        }

        WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableImage);
        return writableImage;
    }

    public static Optional<Image> readImageFile(final File imageFile) {
        checkNotNull(imageFile);

        try(FileInputStream fis = new FileInputStream(imageFile)) {
            //Normally avoid using exceptions for control flow,
            // however the simplest way to know for sure if we can create an image from the provided is to try and create the image
            return Optional.of(new Image(fis));
        }
        catch(RuntimeException | IOException e) {
            log.error("Unable to create image from file: " + imageFile, e);
            return Optional.empty();
        }
    }

    public static Optional<Image> readImageUrl(final String imageUrl) {
        checkNotNull(imageUrl);

        try (InputStream is = URLUtils.spoofBrowserConnection(imageUrl).getInputStream()) {
            return Optional.of(new Image(is));
        }
        catch (IOException | RuntimeException e) {
            log.error("Unable to load image: ", e);
            return Optional.empty();
        }
    }

    public static Image canvasToImage(final Canvas canvas) {
        checkNotNull(canvas);

        WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableImage);
        return writableImage;
    }

    public static Image colorMatrixToImage(final Color[][] blocks, int fitWidth, int fitHeight, int divisions) {
        checkNotNull(blocks);
        checkArgument(fitWidth > 0, "Width must be greater than 0");
        checkArgument(fitHeight > 0, "Height must be greater than 0");
        checkArgument(divisions > 0, "Divisions must be greater than 0");

        final Canvas canvas = new Canvas(fitWidth, fitHeight);
        GraphicsContext context = canvas.getGraphicsContext2D();

        int gapWidth = fitWidth / divisions;
        int gapHeight = fitHeight / divisions;

        for(int i = 0; i < blocks.length; ++i) {
            for(int j = 0; j < blocks[i].length; ++j) {
                context.setFill(blocks[i][j]);
                context.setLineWidth(0);
                context.fillRect(gapWidth*i, gapHeight*j, gapWidth, gapHeight);
            }
        }

        return canvasToImage(canvas);
    }

    public static Color[][] imageToColorMatrix(final Image image, int fitWidth, int fitHeight, int divisions) {
        checkNotNull(image);
        checkArgument(fitWidth > 0, "Width must be greater than 0");
        checkArgument(fitHeight > 0, "Height must be greater than 0");
        checkArgument(divisions > 0, "Divisions must be greater than 0");

        int gapWidth = fitWidth / divisions;
        int gapHeight = fitHeight / divisions;

        Color[][] colorMatrix = new Color[divisions][divisions];

        PixelReader pr = image.getPixelReader();
        for(int i = 0; i < divisions; ++i) {
            for(int j = 0; j < divisions; ++j) {

                double totalH = 0, totalS = 0, totalB = 0;
                for(int x = gapWidth*i; x < gapWidth*i + gapWidth; ++x) {
                    for(int y = gapHeight*j; y < gapHeight*j + gapHeight; ++y) {
                        Color color = pr.getColor(x, y);
                        totalH += color.getRed();
                        totalS += color.getGreen();
                        totalB += color.getBlue();
                    }
                }
                totalH /= (gapWidth * gapHeight);
                totalS /= (gapWidth * gapHeight);
                totalB /= (gapWidth * gapHeight);

                colorMatrix[i][j] = Color.rgb((int)(totalH*255), (int)(totalS*255), (int)(totalB*255));
            }
        }

        return colorMatrix;
    }

    public static Image drawImageToFit(final Image image, final int fitWidth, final int fitHeight) {
        checkArgument(fitWidth > 0, "Width must be greater than 0");
        checkArgument(fitHeight > 0, "Height must be greater than 0");

        final Canvas canvas = new Canvas(fitWidth, fitHeight);
        GraphicsContext context = canvas.getGraphicsContext2D();

        context.drawImage(image, 0, 0, fitWidth, fitHeight);

        return canvasToImage(canvas);
    }
}
