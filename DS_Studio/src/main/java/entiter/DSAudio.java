package entiter;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DSAudio {
    byte [] buffer;
    AudioFormat format;
    String path;
    String name;
    String extension;
    List<Integer> amplitudes = new ArrayList<Integer>();

    public DSAudio() {
    }

    public DSAudio(byte[] buffer, AudioFormat format) {
        this.buffer = buffer;
        this.format = format;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public AudioFormat getFormat() {
        return format;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public List<Integer> getAmplitudes() {
        return amplitudes;
    }

    public void setAmplitudes(List<Integer> amplitudes) {
        this.amplitudes = amplitudes;
    }

    public void initialize(File inputFile) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputFile);
        AudioFormat format = audioInputStream.getFormat();
        byte[] audioBytes = audioInputStream.readAllBytes();
        audioInputStream.close();
        this.setBuffer(audioBytes);
        this.setFormat(format);
    }

    public void amplifier(double gain,boolean isDistorsion) throws Exception {
        this.setAmplitudes(new ArrayList<>());

        AudioFormat format = this.getFormat();
        byte[] audioBytes = this.getBuffer();

        // Modifier l'amplitude
        for (int i = 0; i < audioBytes.length; i += 2) {
            int sample = (audioBytes[i + 1] << 8) | (audioBytes[i] & 0xFF);
            sample = (int) (sample * gain);
            if (isDistorsion){
                if (sample > Short.MAX_VALUE) sample = Short.MAX_VALUE;
                if (sample < Short.MIN_VALUE) sample = Short.MIN_VALUE;
            }
            this.getAmplitudes().add(sample);
            audioBytes[i] = (byte) (sample & 0xFF);
            audioBytes[i + 1] = (byte) ((sample >> 8) & 0xFF);
        }

        this.setBuffer(audioBytes);
        this.setFormat(format);
    }

    public void anti_distorsion() throws Exception {

        AudioFormat format = this.getFormat();
        byte[] audioBytes = this.getBuffer();

        // Modifier l'amplitude
        for (int i = 0; i < audioBytes.length; i += 2) {
            // Les échantillons sont souvent en 16 bits (2 octets, little-endian)
            int sample = (audioBytes[i + 1] << 8) | (audioBytes[i] & 0xFF);
            if (sample > Short.MAX_VALUE) sample = Short.MAX_VALUE;
            if (sample < Short.MIN_VALUE) sample = Short.MIN_VALUE;
            audioBytes[i] = (byte) (sample & 0xFF);
            audioBytes[i + 1] = (byte) ((sample >> 8) & 0xFF);
        }

        this.setBuffer(audioBytes);
        this.setFormat(format);
    }

//    public void amplifier(float gain) throws Exception {
//        AudioFormat format = this.getFormat();
//        byte[] audioBytes = this.getBuffer();
//
//        int sampleSizeInBits = format.getSampleSizeInBits();
//        boolean isBigEndian = format.isBigEndian();
//        boolean isSigned = format.getEncoding().toString().contains("PCM_SIGNED");
//
//        for (int i = 0; i < audioBytes.length; i += (sampleSizeInBits / 8)) {
//            long sample = 0;
//
//            // Lire l'échantillon en fonction de la taille et de l'endianness
//            if (sampleSizeInBits == 8) {
//                sample = audioBytes[i];
//                if (isSigned && sample < 0) sample += 256; // Convertir en non-signé
//            } else if (sampleSizeInBits == 16) {
//                sample = isBigEndian ? ((audioBytes[i] << 8) | (audioBytes[i + 1] & 0xFF))
//                        : ((audioBytes[i + 1] << 8) | (audioBytes[i] & 0xFF));
//                if (sample > 32767) sample -= 65536; // Gérer les valeurs signées
//            } else if (sampleSizeInBits == 24) {
//                sample = isBigEndian ? ((audioBytes[i] << 16) | ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i + 2] & 0xFF))
//                        : ((audioBytes[i + 2] << 16) | ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF));
//                if (sample > 8388607) sample -= 16777216;
//            } else if (sampleSizeInBits == 32) {
//                sample = isBigEndian ? ((audioBytes[i] << 24) | ((audioBytes[i + 1] & 0xFF) << 16) | ((audioBytes[i + 2] & 0xFF) << 8) | (audioBytes[i + 3] & 0xFF))
//                        : ((audioBytes[i + 3] << 24) | ((audioBytes[i + 2] & 0xFF) << 16) | ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF));
//            }
//
//            // Appliquer le gain
//            sample = (long) (sample * gain);
//
//            // Écrire l'échantillon modifié
//            for (int j = 0; j < (sampleSizeInBits / 8); j++) {
//                int shift = isBigEndian ? (sampleSizeInBits - 8 - j * 8) : (j * 8);
//                audioBytes[i + j] = (byte) ((sample >> shift) & 0xFF);
//            }
//        }
//
//        this.setBuffer(audioBytes);
//        this.setFormat(format);
//    }
//
//    public void anti_distorsion() throws Exception {
//        AudioFormat format = this.getFormat();
//        byte[] audioBytes = this.getBuffer();
//
//        int sampleSizeInBits = format.getSampleSizeInBits();
//        boolean isBigEndian = format.isBigEndian();
//        boolean isSigned = format.getEncoding().toString().contains("PCM_SIGNED");
//
//        for (int i = 0; i < audioBytes.length; i += (sampleSizeInBits / 8)) {
//            long sample = 0;
//
//            // Lire l'échantillon en fonction de la taille et de l'endianness
//            if (sampleSizeInBits == 8) {
//                sample = audioBytes[i];
//                if (isSigned && sample < 0) sample += 256; // Convertir en non-signé
//            } else if (sampleSizeInBits == 16) {
//                sample = isBigEndian ? ((audioBytes[i] << 8) | (audioBytes[i + 1] & 0xFF))
//                        : ((audioBytes[i + 1] << 8) | (audioBytes[i] & 0xFF));
//                if (sample > 32767) sample -= 65536; // Gérer les valeurs signées
//            } else if (sampleSizeInBits == 24) {
//                sample = isBigEndian ? ((audioBytes[i] << 16) | ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i + 2] & 0xFF))
//                        : ((audioBytes[i + 2] << 16) | ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF));
//                if (sample > 8388607) sample -= 16777216;
//            } else if (sampleSizeInBits == 32) {
//                sample = isBigEndian ? ((audioBytes[i] << 24) | ((audioBytes[i + 1] & 0xFF) << 16) | ((audioBytes[i + 2] & 0xFF) << 8) | (audioBytes[i + 3] & 0xFF))
//                        : ((audioBytes[i + 3] << 24) | ((audioBytes[i + 2] & 0xFF) << 16) | ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF));
//            }
//
//            // Saturer les valeurs pour éviter le dépassement
//            long maxValue = (1L << (sampleSizeInBits - 1)) - 1;
//            long minValue = -(1L << (sampleSizeInBits - 1));
//
//            if (sample > maxValue) sample = maxValue;
//            if (sample < minValue) sample = minValue;
//
//            // Écrire l'échantillon modifié
//            for (int j = 0; j < (sampleSizeInBits / 8); j++) {
//                int shift = isBigEndian ? (sampleSizeInBits - 8 - j * 8) : (j * 8);
//                audioBytes[i + j] = (byte) ((sample >> shift) & 0xFF);
//            }
//        }
//
//        this.setBuffer(audioBytes);
//        this.setFormat(format);
//    }

    public String save(String path){
        // Écrire dans un fichier WAV
        try {
            File outputFile = new File(path);

            // Créer un flux audio
            try (ByteArrayInputStream bais = new ByteArrayInputStream(this.getBuffer());
                 AudioInputStream audioInputStream = new AudioInputStream(bais, this.getFormat(), this.getBuffer().length / this.getFormat().getFrameSize());) {

                // Sauvegarder en format WAV
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);

                String emplacement = outputFile.getAbsolutePath();
                System.out.println("Fichier WAV créé : " + emplacement);
                return emplacement;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
