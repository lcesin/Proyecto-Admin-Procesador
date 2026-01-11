package com.simulator.utils;

import com.simulator.model.Process;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    /**
     * Lee un archivo de texto con formato CSV y retorna una lista de procesos.
     * Formato esperado por línea: ID, TiempoLlegada, TiempoRafaga, Prioridad
     * Ejemplo: P1,0,10,1
     */
    public List<Process> loadProcesses(String filePath) {
        List<Process> processes = new ArrayList<>();
        
        System.out.println("Intentando leer archivo desde: " + filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Ignorar líneas vacías o comentarios que empiecen con #
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    // Separar por comas
                    String[] parts = line.split(",");
                    
                    if (parts.length != 4) {
                        System.err.println("Advertencia: Línea " + lineNumber + " tiene formato incorrecto. Se salta.");
                        continue;
                    }

                    // Limpiar espacios en blanco y parsear
                    String id = parts[0].trim();
                    int arrival = Integer.parseInt(parts[1].trim());
                    int burst = Integer.parseInt(parts[2].trim());
                    int priority = Integer.parseInt(parts[3].trim());

                    processes.add(new Process(id, arrival, burst, priority));

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico en línea " + lineNumber + ": " + line);
                }
            }
            System.out.println("-> Se cargaron " + processes.size() + " procesos correctamente.");

        } catch (IOException e) {
            System.err.println("Error fatal leyendo el archivo: " + e.getMessage());
        }

        return processes;
    }
}