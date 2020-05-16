package com.toastedbits.kadenze.mlartmusic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImageToOscController {
    @FXML
    private VBox vbox;
    @FXML
    private ImageView sourceView;
    @FXML
    private ImageView destView;

    @FXML
    private CheckBox gridCheckBox;

    @FXML
    private Slider divisionsSlider;
    @FXML
    private Label divisionsLabel;

    @FXML
    private TextField oscHostField;
    @FXML
    private TextField oscPortField;
    @FXML
    private TextField oscAddressField;
    @FXML
    private Button oscSendButton;

    @FXML
    private TextArea msgTextArea;

    private Image srcImage;

    private int divisionsExponent = 0;

    private Color[][] colorMatrix = new Color[0][0];

    @FXML
    private void initialize() throws IOException {
        srcImage = new Image(ImageToOscController.class.getResourceAsStream("/DragAndDrop.jpg"));
        sourceView.setImage(srcImage);

        final Node dragTarget = vbox;

        dragTarget.setOnDragOver(e -> {
            final Dragboard dragboard = e.getDragboard();
            if(dragboard.hasFiles() || dragboard.hasImage() || dragboard.hasUrl()) {
                e.acceptTransferModes(TransferMode.ANY);
            }
            e.consume();
        });

        dragTarget.setOnDragDropped(event -> {
            final Dragboard dragboard = event.getDragboard();

            if (event.getGestureSource() != sourceView) {
                if(dragboard.hasImage()) {
                    /* allow for both copying and moving, whatever user chooses */
                    changeImage(event.getDragboard().getImage());
                }
                else if(dragboard.hasFiles()) {
                    Optional<Image> img = ImageUtil.readImageFile(dragboard.getFiles().get(0));
                    img.ifPresentOrElse(
                        this::changeImage,
                        () -> msgTextArea.appendText("Unable load file: " + dragboard.getFiles().get(0))
                    );
                }
                else if(dragboard.hasUrl()) {
                    Optional<Image> img = ImageUtil.readImageUrl(dragboard.getUrl());
                    img.ifPresentOrElse(
                        this::changeImage,
                        () -> msgTextArea.appendText("Unable to load url: " + dragboard.getUrl())
                    );
                }
            }

            //let the source know whether the string was successfully transferred and used
            event.setDropCompleted(true); //return successfully processed or not

            event.consume();
        });

        gridCheckBox.setOnAction(e -> changeImage(srcImage));

        divisionsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newDivPower = (int)Math.round(newValue.doubleValue());
            if(newDivPower != divisionsExponent) {
                divisionsExponent = newDivPower;
                int divs = getDivisions();
                divisionsLabel.setText(divisionsExponent + " image divisions -> " + divs * divs * 3 + " RGB Values");
                changeImage(srcImage);
            }
        });

        oscSendButton.setOnAction(event -> OSCHelper.builder()
            .withHost(oscHostField.getText())
            .withPort(Integer.parseInt(oscPortField.getText()))
            .withOscAddress(oscAddressField.getText())
            .build()
            .sendOscMessage(colorMatrix)
        );

        msgTextArea.appendText(new String(
            ImageToOscController.class.getResourceAsStream("/prompt.txt")
            .readAllBytes())
        );
    }

    private int getDivisions() {
        int powBase = 2;
        return (int)Math.pow(powBase, divisionsExponent);
    }

    private void changeImage(final Image image) {
        checkNotNull(image);

        int divisions = getDivisions();

        srcImage = ImageUtil.drawImageToFit(image, (int)sourceView.getFitWidth(), (int)sourceView.getFitHeight());
        if(gridCheckBox.isSelected()) {
            sourceView.setImage(ImageUtil.drawGridLines(srcImage, (int)sourceView.getFitWidth(), (int)sourceView.getFitHeight(), divisions));
        }
        else {
            sourceView.setImage(srcImage);
        }

        colorMatrix = ImageUtil.imageToColorMatrix(srcImage, (int)destView.getFitWidth(), (int)destView.getFitHeight(), divisions);
        Image destImage = ImageUtil.colorMatrixToImage(colorMatrix, (int) destView.getFitWidth(), (int) destView.getFitHeight(), divisions);

        if(gridCheckBox.isSelected()) {
            destView.setImage(ImageUtil.drawGridLines(destImage, (int)destView.getFitWidth(), (int)destView.getFitHeight(), divisions));
        }
        else {
            destView.setImage(destImage);
        }
    }
}
