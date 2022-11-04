package edu.unibw.sse.madn.datenClient;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpielfeldKonfigurationImpl implements SpielfeldKonfiguration {
    @Override
    public int[] koordinatenVonFeld(int i) {
        return pointCoordinates[i];
    }

    @Override
    public int[] drehungVonFeld(int i) {
        return orientation[i];
    }

    @Override
    public Image brettBild() {
        return board;
    }

    @Override
    public Image pfadNormalBild() {
        return pathNormal;
    }

    @Override
    public Image wuerfelBild(int wurf) {
        return dice[wurf];
    }

    @Override
    public Image startfeldBild(int spieler) {
        return path[spieler];
    }

    @Override
    public Image spielerPersBild(int spieler) {
        return personal[spieler];
    }

    @Override
    public Image figurBild(int spieler) {
        return figure[spieler];
    }

    @Override
    public Image figurMarkiertBild(int spieler) {
        return figureHigh[spieler];
    }

    @Override
    public int klickRadius() {
        return clickRadius;
    }


    private static final String[] compareList = new String[]{"board.png", "dice0.png", "dice1.png", "dice2.png", "dice3.png", "dice4.png", "dice5.png", "dice6.png", "dice7.png", "figure0.png", "figure1.png", "figure2.png", "figure3.png", "figureHigh0.png", "figureHigh1.png", "figureHigh2.png", "figureHigh3.png", "path0.png", "path1.png", "path2.png", "path3.png", "pathNormal.png", "personal0.png", "personal1.png", "personal2.png", "personal3.png", "positions.txt"};

    private final int[][] pointCoordinates;
    private final int[][] orientation;
    private final Image board;
    private final Image pathNormal;
    private final Image[] dice;
    private final Image[] path;
    private final Image[] personal;
    private final Image[] figure;
    private final Image[] figureHigh;
    private static final int clickRadius = 17;

    private SpielfeldKonfigurationImpl(Builder builder) {
        pointCoordinates = builder.pointCoordinates;
        orientation = builder.orientation;
        board = builder.board;
        pathNormal = builder.pathNormal;
        dice = builder.dice;
        path = builder.path;
        personal = builder.personal;
        figure = builder.figure;
        figureHigh = builder.figureHigh;
    }

    static SpielfeldKonfiguration loadBoardKonfiguration(String dir) {
        if (!dir.endsWith("/")) dir = dir.concat("/");
        File f = new File(dir);
        if (!f.isDirectory()) return null;
        Builder builder = new Builder();
        if (!builder.read(f)) return null;
        return builder.build();
    }

    private static class Builder {
        private final int[][] pointCoordinates;
        private final int[][] orientation;
        private Image board;
        private Image pathNormal;
        private Image[] dice;
        private Image[] path;
        private Image[] personal;
        private Image[] figure;
        private Image[] figureHigh;

        private Builder() {
            pointCoordinates = new int[72][2];
            orientation = new int[72][2];
            dice = new Image[7];
            path = new Image[4];
            personal = new Image[4];
            figure = new Image[4];
            figureHigh = new Image[4];
        }

        private boolean read(File f) {
            // Bilder einlesen
            try {
                board = new Image(Paths.get(f.getAbsolutePath() + "/board.png").toUri().toString());
                pathNormal = new Image(Paths.get(f.getAbsolutePath() + "/pathNormal.png").toUri().toString());
                dice = readImages(f.getAbsolutePath(), "/dice", 7);
                path = readImages(f.getAbsolutePath(), "/path", 4);
                personal = readImages(f.getAbsolutePath(), "/personal", 4);
                figure = readImages(f.getAbsolutePath(), "/figure", 4);
                figureHigh = readImages(f.getAbsolutePath(), "/figureHigh", 4);
            } catch (NullPointerException | IllegalArgumentException e) {
                //e.printStackTrace(System.out);
                return false;
            }

            // Koordinaten einlesen
            File positions = new File(f.getAbsolutePath() + "/positions.txt");
            try (BufferedReader buf = new BufferedReader(new FileReader(positions))) {
                //Pattern pattern = Pattern.compile("[ \t]*(\\d+)[ \t]+(\\d+)[ \t]*");
                Pattern pattern = Pattern.compile("[ \t]*(\\d+)[ \t]+(\\d+)[ \t]*([ \t]+(-?\\d+)([ \t]+([01])[ \t]*)?)?");
                String in = buf.readLine();
                int line = 0;
                int count = 0;
                while (in != null) {
                    line++;
                    if (!in.contains("//")) {
                        Matcher matcher = pattern.matcher(in);
                        if (!matcher.matches()) throw new IOException("position file has error on line:" + line);
                        pointCoordinates[count][0] = Integer.parseInt(matcher.group(1));
                        pointCoordinates[count][1] = Integer.parseInt(matcher.group(2));
                        if (matcher.group(4) != null) {
                            orientation[count][0] = Integer.parseInt(matcher.group(4));
                        }
                        if (matcher.group(6) != null) {
                            orientation[count][1] = Integer.parseInt(matcher.group(6));
                        }
                        count++;
                    }
                    in = buf.readLine();
                }

            } catch (IOException e) {
                //e.printStackTrace(System.out);
                return false;
            }

            return true;
        }

        private SpielfeldKonfiguration build() {
            return new SpielfeldKonfigurationImpl(this);
        }

        private static Image[] readImages(String dir, String name, int count) {
            Image[] temp = new Image[count];
            for (int i = 0; i < count; i++) {
                temp[i] = new Image(Paths.get(dir + name + i + ".png").toUri().toString());
            }
            return temp;
        }

    }
}
