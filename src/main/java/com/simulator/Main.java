package com.simulator;

import com.simulator.algorithms.FCFS;
import com.simulator.algorithms.Priority;
import com.simulator.algorithms.RoundRobin;
import com.simulator.algorithms.SJF;
import com.simulator.algorithms.SchedulingStrategy;
import com.simulator.model.Process;
import com.simulator.metrics.PerformanceMetrics;
import com.simulator.utils.InputHandler;
import com.simulator.ui.ChartGenerator;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // --------------------------------------------------------
        // 1. CARGA DE DATOS
        // --------------------------------------------------------
        InputHandler inputHandler = new InputHandler();
        
        // Asegúrate de que este archivo exista y tenga datos
        String filePath = "src/main/resources/input_data/lote_prueba.txt"; 
        
        List<Process> processes = inputHandler.loadProcesses(filePath);

        if (processes.isEmpty()) {
            System.err.println("❌ Error: No se cargaron procesos. Revisa el archivo 'lote_prueba.txt'");
            return;
        }

        // --------------------------------------------------------
        // 2. SELECCIÓN DE ALGORITMO
        // (Descomenta el que quieras probar)
        // --------------------------------------------------------
        
        // SchedulingStrategy strategy = new FCFS();
        // SchedulingStrategy strategy = new SJF();
        // SchedulingStrategy strategy = new Priority();
        SchedulingStrategy strategy = new RoundRobin(3); // Quantum = 3

        System.out.println("\n==========================================");
        System.out.println(" 🚀 INICIANDO SIMULACIÓN: " + strategy.getName());
        System.out.println("==========================================");

        // --------------------------------------------------------
        // 3. EJECUCIÓN (CORE)
        // --------------------------------------------------------
        strategy.run(processes);

        // --------------------------------------------------------
        // 4. REPORTE EN CONSOLA (METRICS)
        // --------------------------------------------------------
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.printMetrics(processes);

        // --------------------------------------------------------
        // 5. VISUALIZACIÓN GRÁFICA (UI)
        // --------------------------------------------------------
        System.out.println("\n📊 Generando Diagrama de Gantt...");
        try {
            ChartGenerator.showChart("Simulación: " + strategy.getName(), processes);
            System.out.println("✅ Ventana de gráfico abierta correctamente.");
        } catch (Exception e) {
            System.err.println("⚠️ Error al abrir el gráfico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}