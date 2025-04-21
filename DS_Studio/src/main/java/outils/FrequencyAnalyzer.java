package outils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FrequencyAnalyzer {

    public static double getFrequency(File audioFile) throws UnsupportedAudioFileException, IOException {
//        File audioFile = new File("input.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioInputStream.getFormat();

        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new UnsupportedOperationException("Format non supporté : Utilisez PCM_SIGNED.");
        }

        int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
        boolean isBigEndian = format.isBigEndian();
        float sampleRate = format.getSampleRate();

        byte[] audioBytes = audioInputStream.readAllBytes();
        audioInputStream.close();

        // Convertir les bytes en échantillons (16 bits PCM)
        int[] samples = bytesToSamples(audioBytes, sampleSizeInBytes, isBigEndian);

        // Taille de la fenêtre FFT (doit être une puissance de 2)
        int fftSize = 4096;
        double[] real = new double[fftSize];
        double[] imag = new double[fftSize];

        // Remplir la fenêtre FFT avec les premiers échantillons
        for (int i = 0; i < fftSize && i < samples.length; i++) {
            real[i] = samples[i];
        }

        // Appliquer la FFT
        fft(real, imag);

        // Trouver la fréquence dominante
        int peakIndex = findPeakFrequency(real, imag);
        double dominantFrequency = (double) peakIndex * sampleRate / fftSize;

        System.out.println("Fréquence dominante : " + dominantFrequency + " Hz");
        return dominantFrequency;
    }

    // Convertir les bytes en échantillons (16 bits PCM)
    private static int[] bytesToSamples(byte[] audioBytes, int sampleSizeInBytes, boolean isBigEndian) {
        int[] samples = new int[audioBytes.length / sampleSizeInBytes];
        ByteBuffer buffer = ByteBuffer.wrap(audioBytes);
        buffer.order(isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < samples.length; i++) {
            samples[i] = buffer.getShort();
        }

        return samples;
    }

    // Appliquer l'algorithme FFT (Fast Fourier Transform)
    private static void fft(double[] real, double[] imag) {
        int n = real.length;
        if (n == 0 || (n & (n - 1)) != 0) {
            throw new IllegalArgumentException("La taille de la FFT doit être une puissance de 2");
        }

        int bits = Integer.numberOfTrailingZeros(n);
        for (int i = 0; i < n; i++) {
            int j = Integer.reverse(i) >>> (32 - bits);
            if (j > i) {
                double temp = real[i];
                real[i] = real[j];
                real[j] = temp;
                temp = imag[i];
                imag[i] = imag[j];
                imag[j] = temp;
            }
        }

        for (int size = 2; size <= n; size *= 2) {
            int halfSize = size / 2;
            double phaseStep = -2 * Math.PI / size;
            for (int i = 0; i < n; i += size) {
                for (int j = 0; j < halfSize; j++) {
                    double angle = j * phaseStep;
                    double cos = Math.cos(angle);
                    double sin = Math.sin(angle);
                    double tReal = cos * real[i + j + halfSize] - sin * imag[i + j + halfSize];
                    double tImag = sin * real[i + j + halfSize] + cos * imag[i + j + halfSize];
                    real[i + j + halfSize] = real[i + j] - tReal;
                    imag[i + j + halfSize] = imag[i + j] - tImag;
                    real[i + j] += tReal;
                    imag[i + j] += tImag;
                }
            }
        }
    }

    // Trouver le pic de fréquence (fréquence dominante)
    private static int findPeakFrequency(double[] real, double[] imag) {
        double maxMagnitude = 0;
        int index = 0;
        for (int i = 0; i < real.length / 2; i++) {
            double magnitude = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
            if (magnitude > maxMagnitude) {
                maxMagnitude = magnitude;
                index = i;
            }
        }
        return index;
    }
}
