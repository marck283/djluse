package it.disi.unitn.imggen.lasagna;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.engine.EngineException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDList;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import it.disi.unitn.imggen.lasagna.nnimpl.Block;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException,
            TranslateException {
        System.out.println("Hello world!");

        Translator<byte[], Image[]> translator = new Translator<>() {
            @Override
            public Image @NotNull [] processOutput(TranslatorContext ctx, NDList list) {
                Image[] imageArr = new Image[10];
                for (int i = 0; i < 10; i++) {
                    imageArr[i] = ImageFactory.getInstance().fromNDArray(list.get(i));
                }
                return imageArr;
            }

            @Override
            public NDList processInput(@NotNull TranslatorContext ctx, byte[] input) {
                try (NDList list = new NDList()) {
                    list.add(ctx.getNDManager().create(input));
                    return list;
                }
            }
        };

        try (Model model = Model.newInstance("attngan2.zip", Device.cpu(), "PyTorch")) {
            model.setBlock(new Block());
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            String inputString = scanner.next();
            byte[] input = inputString.getBytes();
            Predictor<byte[], Image[]> predictor = model.newPredictor(translator);
            Image[] generatedImages = predictor.predict(input); // feed input, receive output

            Path outputPath = Paths.get("build/output/");
            Files.createDirectories(outputPath);

            for (int i = 0; i < generatedImages.length; i++) {
                Path path = outputPath.resolve("img" + i + ".png");
                generatedImages[i].save(Files.newOutputStream(path), "png");
            }
        } catch (EngineException ex) {
            ex.printStackTrace();
        }
    }
}