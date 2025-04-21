package outils;

import entiter.DSAudio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioAnalyzer {

    public List<Integer> getAmplitudes(String path) {
        File audioFile = new File(path);
        List<Integer> amplitudes = new ArrayList<Integer> ();
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
            AudioFormat format = audioInputStream.getFormat();
//            System.out.println("Format audio : " + format);

//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
//                for (int i = 0; i < bytesRead; i += 2) {
//                    // Convertir les 2 octets en un échantillon (16 bits, signé)
//                    int sample = (buffer[i + 1] << 8) | (buffer[i] & 0xFF);
////                    System.out.println("Amplitude : " + sample);
//                    amplitudes.add(sample);
//                }
//            }

            byte[] audioBytes = audioInputStream.readAllBytes();
            // Modifier l'amplitude
            for (int i = 0; i < audioBytes.length; i += 2) {
                // Les échantillons sont souvent en 16 bits (2 octets, little-endian)
                int sample = (audioBytes[i + 1] << 8) | (audioBytes[i] & 0xFF);
                amplitudes.add(sample);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return amplitudes;
    }

//    public List<Long> getAmplitudes(String path) {
//        File audioFile = new File(path);
//        List<Long> amplitudes = new ArrayList<>();
//
//        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
//            AudioFormat format = audioInputStream.getFormat();
//
//            int sampleSizeInBytes = format.getSampleSizeInBits() / 8;
//            boolean isBigEndian = format.isBigEndian();
//            int numChannels = format.getChannels();
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
//                for (int i = 0; i < bytesRead; i += sampleSizeInBytes * numChannels) {
//                    for (int channel = 0; channel < numChannels; channel++) {
//                        int sampleIndex = i + channel * sampleSizeInBytes;
//                        long sample = bytesToSample(buffer, sampleIndex, sampleSizeInBytes, isBigEndian);
//                        amplitudes.add(sample);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return amplitudes;
//    }
//
//    // Convertir des bytes en échantillon (compatible 8, 16, 24, 32 bits)
//    private long bytesToSample(byte[] buffer, int index, int sampleSizeInBytes, boolean isBigEndian) {
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
//        // Étendre le bit de signe si l'échantillon est signé
//        if ((sample & (1L << ((sampleSizeInBytes * 8) - 1))) != 0) {
//            sample -= (1L << (sampleSizeInBytes * 8));
//        }
//
//        return sample;
//    }

    public static void updateDetailsForChart(String filePath,List<String> labels,List<Integer> amplitude){
        List<Integer> listAmplitude = new AudioAnalyzer().getAmplitudes(filePath);
        double count = 0;
        long max = Collections.max(listAmplitude);
        long min = Collections.min(listAmplitude);
        Boolean isMax = null;
        int repetition = 0;
        for (Integer ap : listAmplitude){
            if (count==0){
                amplitude.add(ap);
                labels.add("1");
                count++;
            }
            else {
                if (count<=20){
                    if (isMax!=null){
                        boolean isValid = false;
                        if (isMax && !isValid){
                            if (ap == min){
                                amplitude.add(ap);
                                labels.add(String.valueOf(repetition));
                                repetition=1;
                                isMax=false;
                                count++;
                            }
                            if (ap == max){
                                repetition++;
                            }
                            isValid = true;
                        }
                        if (!isMax && !isValid){
                            if (ap == min){
                                repetition++;
                                isMax=false;
                            }
                            if (ap == max){
                                amplitude.add(ap);
                                labels.add(String.valueOf(repetition));
                                repetition=1;
                                isMax=true;
                                count++;
                            }
                            isValid = true;
                        }
                    }
                    if (isMax==null){
                        if (ap == min){
                            amplitude.add(ap);
                            repetition++;
                            isMax=false;
                        }
                        if (ap == max){
                            amplitude.add(ap);
                            repetition++;
                            isMax=true;
                        }
                    }
                }
            }
        }
    }

    public static void updateDetailsForChart2(String filePath,List<String> labels,List<Integer> amplitude,List<Integer> listAmplitude){
        double count = 0;
        long max = Collections.max(listAmplitude);
        long min = Collections.min(listAmplitude);
        Boolean isMax = null;
        int repetition = 0;
        for (Integer ap : listAmplitude){
            if (count==0){
                amplitude.add(ap);
                labels.add("1");
                count++;
            }
            else {
                if (count<=20){
                    if (isMax!=null){
                        boolean isValid = false;
                        if (isMax && !isValid){
                            if (ap == min){
                                amplitude.add(ap);
                                labels.add(String.valueOf(repetition));
                                repetition=1;
                                isMax=false;
                                count++;
                            }
                            if (ap == max){
                                repetition++;
                            }
                            isValid = true;
                        }
                        if (!isMax && !isValid){
                            if (ap == min){
                                repetition++;
                                isMax=false;
                            }
                            if (ap == max){
                                amplitude.add(ap);
                                labels.add(String.valueOf(repetition));
                                repetition=1;
                                isMax=true;
                                count++;
                            }
                            isValid = true;
                        }
                    }
                    if (isMax==null){
                        if (ap == min){
                            amplitude.add(ap);
                            repetition++;
                            isMax=false;
                        }
                        if (ap == max){
                            amplitude.add(ap);
                            repetition++;
                            isMax=true;
                        }
                    }
                }
            }
        }
    }

}
