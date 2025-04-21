package outils;

import entiter.DSAudio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;

public class NoiseFilter {
    public static void suppresionDeBruit(DSAudio audio,double noiseFrequency) throws Exception {

        // Fréquence de coupure (ex : 50Hz pour le bourdonnement électrique)
//        double noiseFrequency = 50.0;
        AudioFormat format = audio.getFormat();

        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new UnsupportedOperationException("Seuls les fichiers PCM_SIGNED sont pris en charge.");
        }

        // Lire l'audio en bytes
        byte[] audioBytes = audio.getBuffer();

        // Appliquer le Notch Filter
        byte[] filteredAudio = applyNotchFilter(audioBytes, format, noiseFrequency);

        audio.setBuffer(filteredAudio);
        audio.setFormat(format);
    }

//    // Appliquer un Notch Filter
//    public static byte[] applyNotchFilter(byte[] audioBytes, AudioFormat format, double notchFrequency) {
//        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
//        int numChannels = format.getChannels();
//        boolean isBigEndian = format.isBigEndian();
//        float sampleRate = format.getSampleRate();
//
//        // Calcul des coefficients du filtre Notch
//        double omega = 2 * Math.PI * notchFrequency / sampleRate;
//        double Q = 50; // Facteur de qualité (précision du filtre)
//        double alpha = Math.sin(omega) / (2 * Q);
//
//        double a0 = 1 + alpha;
//        double a1 = -2 * Math.cos(omega);
//        double a2 = 1 - alpha;
//        double b1 = a1;
//        double b2 = a2;
//
//        // Normaliser les coefficients
//        a1 /= a0;
//        a2 /= a0;
//        b1 /= a0;
//        b2 /= a0;
//
//        byte[] filteredBytes = new byte[audioBytes.length];
//
//        // État du filtre pour chaque canal
//        double[][] x = new double[numChannels][2]; // Entrées précédentes
//        double[][] y = new double[numChannels][2]; // Sorties précédentes
//
//        for (int i = 0; i < audioBytes.length; i += sampleSizeInBytes * numChannels) {
//            for (int channel = 0; channel < numChannels; channel++) {
//                long sample = bytesToSample(audioBytes, i + channel * sampleSizeInBytes, sampleSizeInBytes, isBigEndian);
//
//                // Appliquer l'équation du filtre Notch
//                double output = sample - b1 * x[channel][0] - b2 * x[channel][1] + a1 * y[channel][0] + a2 * y[channel][1];
//
//                // Mettre à jour l'état
//                x[channel][1] = x[channel][0];
//                x[channel][0] = sample;
//                y[channel][1] = y[channel][0];
//                y[channel][0] = output;
//
//                // Convertir et enregistrer l'échantillon filtré
//                sampleToBytes(filteredBytes, i + channel * sampleSizeInBytes, (long) output, sampleSizeInBytes, isBigEndian);
//            }
//        }
//
//        return filteredBytes;
//    }
//
//    // Convertir des bytes en échantillon (prise en charge 8, 16, 24, 32 bits)
//    private static long bytesToSample(byte[] buffer, int index, int sampleSizeInBytes, boolean isBigEndian) {
//        long sample = 0;
//
//        if (isBigEndian) {
//            for (int i = 0; i < sampleSizeInBytes; i++) {
//                sample = (sample << 8) | (buffer[index + i] & 0xFF);
//            }
//        } else {
//            for (int i = sampleSizeInBytes - 1; i >= 0; i--) {
//                sample = (sample << 8) | (buffer[index + i] & 0xFF);
//            }
//        }
//
//        // Sign extension si l'échantillon est signé
//        if ((sample & (1L << ((sampleSizeInBytes * 8) - 1))) != 0) {
//            sample -= (1L << (sampleSizeInBytes * 8));
//        }
//
//        return sample;
//    }
//
//    // Convertir un échantillon en bytes (prise en charge 8, 16, 24, 32 bits)
//    private static void sampleToBytes(byte[] buffer, int index, long sample, int sampleSizeInBytes, boolean isBigEndian) {
//        if (isBigEndian) {
//            for (int i = sampleSizeInBytes - 1; i >= 0; i--) {
//                buffer[index + i] = (byte) (sample & 0xFF);
//                sample >>= 8;
//            }
//        } else {
//            for (int i = 0; i < sampleSizeInBytes; i++) {
//                buffer[index + i] = (byte) (sample & 0xFF);
//                sample >>= 8;
//            }
//        }
//    }


    // Appliquer un Notch Filter
    public static byte[] applyNotchFilter(byte[] audioBytes, AudioFormat format, double notchFrequency) {
        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
        int numChannels = format.getChannels();
        boolean isBigEndian = format.isBigEndian();
        float sampleRate = format.getSampleRate();

        if (sampleSizeInBytes != 2) {
            throw new UnsupportedOperationException("Seuls les échantillons en 16 bits sont pris en charge.");
        }

        // Calcul des coefficients du filtre Notch
        double omega = 2 * Math.PI * notchFrequency / sampleRate;
        double Q = 50; // Facteur de qualité (précision du filtre)
        double alpha = Math.sin(omega) / (2 * Q);

        double a0 = 1 + alpha;
        double a1 = -2 * Math.cos(omega);
        double a2 = 1 - alpha;
        double b1 = a1;
        double b2 = a2;

        // Normaliser les coefficients
        a1 /= a0;
        a2 /= a0;
        b1 /= a0;
        b2 /= a0;

        byte[] filteredBytes = new byte[audioBytes.length];
        double x1 = 0, x2 = 0; // Échantillons précédents d'entrée
        double y1 = 0, y2 = 0; // Échantillons précédents de sortie

        for (int i = 0; i < audioBytes.length; i += sampleSizeInBytes * numChannels) {
            for (int channel = 0; channel < numChannels; channel++) {
                int sample = bytesToSample(audioBytes, i + channel * sampleSizeInBytes, isBigEndian);

                // Appliquer l'équation du filtre Notch
                double output = sample - b1 * x1 - b2 * x2 + a1 * y1 + a2 * y2;

                // Mise à jour des états
                x2 = x1;
                x1 = sample;
                y2 = y1;
                y1 = output;

                // Écrêter les valeurs pour rester dans la plage 16 bits
                int filteredSample = (int) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, output));
                sampleToBytes(filteredBytes, i + channel * sampleSizeInBytes, filteredSample, isBigEndian);
            }
        }

        return filteredBytes;
    }

    // Convertir des bytes en échantillon (16 bits signé)
    private static int bytesToSample(byte[] buffer, int index, boolean isBigEndian) {
        if (isBigEndian) {
            return (buffer[index] << 8) | (buffer[index + 1] & 0xFF);
        } else {
            return (buffer[index] & 0xFF) | (buffer[index + 1] << 8);
        }
    }

    // Convertir un échantillon en bytes (16 bits signé)
    private static void sampleToBytes(byte[] buffer, int index, int sample, boolean isBigEndian) {
        if (isBigEndian) {
            buffer[index] = (byte) ((sample >> 8) & 0xFF);
            buffer[index + 1] = (byte) (sample & 0xFF);
        } else {
            buffer[index] = (byte) (sample & 0xFF);
            buffer[index + 1] = (byte) ((sample >> 8) & 0xFF);
        }
    }
}
