package outils;

import entiter.DSAudio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SoundGenerator {

    public DSAudio generateSound(float sampleRate,int duration,double frequency,int sampleSizeInBits,int numChannels) {
//        sampleRate = 44100; // Fréquence d'échantillonnage (Hz)
//        duration = 3;         // Durée (secondes)
//        frequency = 440.0; // Fréquence du son (Hz - La3)
//        sampleSizeInBits = 16; // Résolution (bits : 16 pour un son de qualité)
//        numChannels = 1;      // Mono (1 canal)

        // Création du format audio (16 bits, mono, PCM)
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, numChannels, true, true);
        // Taille du buffer : (durée * fréquence d'échantillonnage * 2 octets/échantillon)
        byte[] buffer = new byte[(int) (sampleRate * duration * 2)];

        // Génération de l'onde sinusoïdale
        for (int i = 0; i < buffer.length / 2; i++) {
            double angle = 2.0 * Math.PI * frequency * i / sampleRate;
            short sample = (short) (Math.sin(angle) * Short.MAX_VALUE); // Amplitude max 16 bits

            // Stockage en format Little-Endian (Standard WAV)
            buffer[2 * i] = (byte) (sample >> 8);         // Octet de poids fort (big byte)
            buffer[2 * i + 1] = (byte) (sample & 0xFF);   // Octet de poids faible (small byte)
        }

        return new DSAudio(buffer,format);
    }

//    public DSAudio generateSound(float sampleRate, int duration, double frequency, int sampleSizeInBits, int numChannels) {
//        // Création du format audio
//        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, numChannels, true, true);
//
//        // Calcul de la taille du buffer
//        int sampleSizeInBytes = sampleSizeInBits / 8;
//        int bufferLength = (int) (sampleRate * duration * sampleSizeInBytes * numChannels);
//        byte[] buffer = new byte[bufferLength];
//
//        // Génération de l'onde sinusoïdale
//        for (int i = 0; i < bufferLength / (sampleSizeInBytes * numChannels); i++) {
//            double angle = 2.0 * Math.PI * frequency * i / sampleRate;
//            long sample = (long) (Math.sin(angle) * ((1L << (sampleSizeInBits - 1)) - 1));
//
//            for (int channel = 0; channel < numChannels; channel++) {
//                int index = (i * numChannels + channel) * sampleSizeInBytes;
//                sampleToBytes(buffer, index, sample, sampleSizeInBytes, format.isBigEndian());
//            }
//        }
//
//        return new DSAudio(buffer, format);
//    }

    // Convertir un échantillon en bytes (prise en charge 8, 16, 24, 32 bits)
    private void sampleToBytes(byte[] buffer, int index, long sample, int sampleSizeInBytes, boolean isBigEndian) {
        if (isBigEndian) {
            for (int i = sampleSizeInBytes - 1; i >= 0; i--) {
                buffer[index + i] = (byte) (sample & 0xFF);
                sample >>= 8;
            }
        } else {
            for (int i = 0; i < sampleSizeInBytes; i++) {
                buffer[index + i] = (byte) (sample & 0xFF);
                sample >>= 8;
            }
        }
    }


    public static String save_sound(DSAudio audioFile,String path){
        // Écrire dans un fichier WAV
        try {
            File outputFile = new File(path);

            // Créer un flux audio
            try (ByteArrayInputStream bais = new ByteArrayInputStream(audioFile.getBuffer());
                 AudioInputStream audioInputStream = new AudioInputStream(bais, audioFile.getFormat(), audioFile.getBuffer().length / 2)) {

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
