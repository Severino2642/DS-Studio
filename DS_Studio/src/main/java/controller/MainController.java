package controller;

import entiter.DSAudio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Part;
import outils.AudioAnalyzer;
import outils.FrequencyAnalyzer;
import outils.NoiseFilter;
import outils.SoundGenerator;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;

@MultipartConfig
@WebServlet(name = "MainController" , value = "*.MainController")
public class MainController extends MereController {
    @CtrlAnnotation(name = "GenerateSound")
    public void GenerateSound () throws SQLException, ServletException, IOException {
        float sampleRate = Float.parseFloat(request.getParameter("sampleRate"));
        int duration = Integer.parseInt(request.getParameter("duration"));
        double frequency = Double.parseDouble(request.getParameter("frequency"));
//        int sampleSizeInBits = Integer.parseInt(request.getParameter("sampleSizeInBits"));
        int sampleSizeInBits = 16;
        int numChannels = 1;

        DSAudio audio = new SoundGenerator().generateSound(sampleRate, duration, frequency, sampleSizeInBits, numChannels);
        audio.setExtension("wav");
        audio.setPath(getServletContext().getRealPath("/upload")+"/output.wav");
        audio.setName("output.wav");
        SoundGenerator.save_sound(audio,audio.getPath());
        request.setAttribute("audio", audio);

        RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
        rd.forward(request, response);
    }

    @CtrlAnnotation(name = "ModifSound")
    public void ModifSound () throws Exception {
        boolean antiDistortion = Boolean.valueOf(request.getParameter("antiDistortion"));
        Part filePart = request.getPart("audio");
        Upload(filePart,"input");
        String path = getServletContext().getRealPath("/upload")+"/input.wav";
        File inputFile = new File(path);
        DSAudio audio = new DSAudio();
        audio.initialize(inputFile);

//        if (request.getPart("noiseSound") != null) {
//            Part noisePart = request.getPart("noiseSound");
//            Upload(filePart,"noise");
//            double noiseFrequency = FrequencyAnalyzer.getFrequency(new File(getServletContext().getRealPath("/upload")+"/noise.wav"));
//            NoiseFilter.suppresionDeBruit(audio,noiseFrequency);
//        }

        if (!request.getParameter("amplification").isEmpty()) {
            double amplification = Double.parseDouble(request.getParameter("amplification"));
            audio.amplifier(amplification,antiDistortion);
        }

        audio.setExtension("wav");
        audio.setPath(getServletContext().getRealPath("/upload")+"/output.wav");
        audio.setName("output.wav");
        audio.save(audio.getPath());
        request.setAttribute("audio", audio);
        request.setAttribute("path",path);

        RequestDispatcher rd = request.getRequestDispatcher("/modification.jsp");
        rd.forward(request, response);
    }

    @CtrlAnnotation(name = "ModifSound2")
    public void ModifSound2 () throws Exception {
        Part filePart = request.getPart("audio");
        Upload(filePart,"input");
        String path = getServletContext().getRealPath("/upload")+"/input.wav";
        File inputFile = new File(path);
        DSAudio audio = new DSAudio();
        audio.initialize(inputFile);

        if (request.getPart("noiseSound") != null) {
            Part noisePart = request.getPart("noiseSound");
            Upload(filePart,"noise");
            double noiseFrequency = FrequencyAnalyzer.getFrequency(new File(getServletContext().getRealPath("/upload")+"/noise.wav"));
            NoiseFilter.suppresionDeBruit(audio,noiseFrequency);
        }

        audio.setExtension("wav");
        audio.setPath(getServletContext().getRealPath("/upload")+"/output.wav");
        audio.setName("output.wav");
        audio.save(audio.getPath());
        request.setAttribute("audio", audio);
        request.setAttribute("path",path);
        audio.setAmplitudes(new AudioAnalyzer().getAmplitudes(audio.getPath()));
        RequestDispatcher rd = request.getRequestDispatcher("/modification.jsp");
        rd.forward(request, response);
    }

    public String Upload(Part filePart,String name) throws ServletException, IOException, Exception {
        String fileName = null;
        try{

            // Obtenez le nom du fichier
            //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String [] extension = Paths.get(filePart.getSubmittedFileName()).getFileName().toString().split("\\.");
            fileName =  name+"."+extension[extension.length-1];

            // Définissez le répertoire de destination pour le téléchargement
            String uploadDir = getServletContext().getRealPath("/upload");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdir();
            }

            // Copiez le fichier vers le répertoire de destination
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return fileName;
    }
}
