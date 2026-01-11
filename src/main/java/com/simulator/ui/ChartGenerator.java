package com.simulator.ui;

import com.simulator.model.Process;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class ChartGenerator extends JFrame {

    public ChartGenerator(String title, List<Process> processes) {
        super(title);

        // 1. Crear el Dataset (Los datos del gráfico)
        IntervalCategoryDataset dataset = createDataset(processes);

        // 2. Crear el Gráfico
        JFreeChart chart = ChartFactory.createGanttChart(
                "Diagrama de Gantt - Ejecución de CPU", // Título del gráfico
                "Procesos",                             // Eje X (Categorías)
                "Tiempo (Segundos)",                    // Eje Y (Tiempo)
                dataset,                                // Datos
                true,                                   // Leyenda
                true,                                   // Tooltips
                false                                   // URLs
        );

        // 3. Personalizar el diseño
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        GanttRenderer renderer = (GanttRenderer) plot.getRenderer();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);
        
        // Hacer las barras un poco más gruesas
        renderer.setMaximumBarWidth(0.1); 

        // 4. Mostrar en un Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private IntervalCategoryDataset createDataset(List<Process> processes) {
        TaskSeriesCollection collection = new TaskSeriesCollection();
        TaskSeries series = new TaskSeries("Ejecución");

        // Fecha base ficticia para que JFreeChart pueda dibujar segundos (hack común)
        // Usamos el año 2024, mes 1, día 1, hora 0, minuto 0, segundo 0
        long baseTime = LocalDate.of(2024, 1, 1).atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;

        for (Process p : processes) {
            // Tarea principal (El nombre del proceso en el eje vertical)
            // Creamos una tarea que abarque desde el inicio total hasta el fin total
            // (Aunque visualmente usaremos las subtareas para los detalles)
            long startTotal = baseTime + (p.getStartTime() * 1000L);
            long endTotal = baseTime + (p.getFinishTime() * 1000L);
            
            Task mainTask = new Task(p.getId(), new Date(startTotal), new Date(endTotal));

            // Agregar los sub-intervalos (Fragmentos reales de ejecución)
            for (int[] interval : p.getExecutionHistory()) {
                long startPart = baseTime + (interval[0] * 1000L);
                long endPart = baseTime + (interval[1] * 1000L);
                
                // JFreeChart usa Subtasks para mostrar barras fragmentadas
                Task subTask = new Task(p.getId() + "_part", new Date(startPart), new Date(endPart));
                mainTask.addSubtask(subTask);
            }

            series.add(mainTask);
        }

        collection.add(series);
        return collection;
    }

    /**
     * Método estático para lanzar la ventana fácilmente
     */
    public static void showChart(String title, List<Process> processes) {
        SwingUtilities.invokeLater(() -> {
            ChartGenerator example = new ChartGenerator(title, processes);
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}