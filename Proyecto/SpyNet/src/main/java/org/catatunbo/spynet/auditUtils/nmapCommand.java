package org.catatunbo.spynet.auditUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class nmapCommand {

    private Process proceso;

    public  String executeNmap(String arg, String ip){
        System.out.println("Hello, World!");
        String output = null;

        String[] argsSplit = arg.split(" ");
        List<String> command = new ArrayList<>();
        command.add("nmap");
        command.addAll(Arrays.asList(argsSplit));
        command.add(ip);

        System.out.println(arg+" "+ ip);
        try{
            ProcessBuilder builder = new ProcessBuilder(command);
            Process proceso = builder.start();

            // Capturar la salida del comando
            java.io.InputStream inputStream = proceso.getInputStream();
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
            output = s.hasNext() ? s.next() : "";
            s.close();

            int exitState = proceso.waitFor();
            System.out.println("estado de salida:" + exitState);
            System.out.println("Salida del comando:\n" + output);


        } catch (IOException | InterruptedException e){
            e.printStackTrace();
            return "Error al ejecutar Nmap: "+ e.getMessage();
        }
        

        return output;
    }

    public void cancel() {
        if (proceso != null && proceso.isAlive()) {
            proceso.destroy();  // Detiene el proceso del sistema
            System.out.println("Proceso Nmap cancelado.");
        }
    }
    
}
