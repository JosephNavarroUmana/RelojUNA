package cr.ac.una.relojuna.util;

import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimacionCumpleanos {

    private static final Random RANDOM = new Random();
    private static final Color[] COLORES = {
        Color.web("#FF4D6D"), Color.web("#FFB703"), Color.web("#4CC9F0"),
        Color.web("#8AC926"), Color.web("#B5179E"), Color.web("#FFD60A")
    };

    public static void reproducir(StackPane panel, Label lblFelicitacion, String nombre) {
        lblFelicitacion.setText("¡Feliz cumpleaños " + nombre + "!");

        panel.setOpacity(0);
        panel.setVisible(true);

        lanzarConfeti(panel, 60);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(350), panel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        //Texto con rebote
        lblFelicitacion.setScaleX(0.3);
        lblFelicitacion.setScaleY(0.3);
        ScaleTransition rebote = new ScaleTransition(Duration.millis(500), lblFelicitacion);
        rebote.setFromX(0.3);
        rebote.setFromY(0.3);
        rebote.setToX(1.15);
        rebote.setToY(1.15);
        rebote.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition asentar = new ScaleTransition(Duration.millis(150), lblFelicitacion);
        asentar.setToX(1.0);
        asentar.setToY(1.0);

        //Pulso continuo
        ScaleTransition pulso = new ScaleTransition(Duration.millis(450), lblFelicitacion);
        pulso.setFromX(1.0);
        pulso.setFromY(1.0);
        pulso.setToX(1.08);
        pulso.setToY(1.08);
        pulso.setCycleCount(6);
        pulso.setAutoReverse(true);

        PauseTransition espera = new PauseTransition(Duration.seconds(3.5));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), panel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition secuencia = new SequentialTransition(
                fadeIn, rebote, asentar, pulso, espera, fadeOut
        );
        secuencia.setOnFinished(e -> {
            panel.setVisible(false);
            panel.getChildren().removeIf(nodo -> nodo instanceof Rectangle);
        });
        secuencia.play();
    }

    private static void lanzarConfeti(StackPane panel, int cantidad) {
        double ancho = panel.getWidth() > 0 ? panel.getWidth() : 800;
        double alto = panel.getHeight() > 0 ? panel.getHeight() : 600;

        for (int i = 0; i < cantidad; i++) {
            Rectangle pieza = new Rectangle(8 + RANDOM.nextInt(6), 12 + RANDOM.nextInt(6));
            pieza.setFill(COLORES[RANDOM.nextInt(COLORES.length)]);
            pieza.setRotate(RANDOM.nextInt(360));

            double xInicio = RANDOM.nextDouble() * ancho - (ancho / 2);
            pieza.setTranslateX(xInicio);
            pieza.setTranslateY(-alto / 2 - 20);

            panel.getChildren().add(pieza);

            double caidaFinal = alto + 60;
            double duracionCaida = 1800 + RANDOM.nextInt(1200);
            double retraso = RANDOM.nextInt(500);

            TranslateTransition caer = new TranslateTransition(Duration.millis(duracionCaida), pieza);
            caer.setToY(caidaFinal - alto / 2);
            caer.setToX(xInicio + (RANDOM.nextDouble() * 100 - 50));
            caer.setInterpolator(Interpolator.EASE_IN);
            caer.setDelay(Duration.millis(retraso));

            RotateTransition girar = new RotateTransition(Duration.millis(duracionCaida), pieza);
            girar.setByAngle(360 * (RANDOM.nextBoolean() ? 1 : -1));
            girar.setDelay(Duration.millis(retraso));

            FadeTransition desvanecer = new FadeTransition(Duration.millis(500), pieza);
            desvanecer.setToValue(0);
            desvanecer.setDelay(Duration.millis(retraso + duracionCaida - 400));

            new ParallelTransition(caer, girar, desvanecer).play();
        }
    }
}
