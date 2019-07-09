/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarios;
import java.util.*;
import proyectosistemasoperativos.*;
/**
 *
 * @author EGARAMENDI
 */
public class Configuracion {
    
    /**
     * Configura las políticas disponibles que se simularán
     * @param aPoliticasSistema Lista de políticas
     */
    public static void CrearPoliticas(ArrayList<Politica> aPoliticasSistema){
        Politica FCFS_NoExpropiativo = new Politica("FCFS", false);
        Politica SJF_NoExpropiativo = new Politica("SJF", false);
        Politica SJF_Expropiativo = new Politica("SJF", true);
        Politica RR_Expropiativo = new Politica("RR", true);
        Politica PRIORIDAD_Expropiativo = new Politica("PRIORIDAD", true);
        aPoliticasSistema.add(FCFS_NoExpropiativo);
        aPoliticasSistema.add(SJF_NoExpropiativo);
        aPoliticasSistema.add(SJF_Expropiativo);
        aPoliticasSistema.add(RR_Expropiativo);
        aPoliticasSistema.add(PRIORIDAD_Expropiativo);
        
        // TODO Esto es removible
        System.out.println("******** Políicas del Sistema ********");
        for(Politica politica:aPoliticasSistema){
            System.out.println(politica.toString());
        }
    }
    
    /**
     * Configura los recursos hardware con lo que se contará en la simulación
     * @param aRecursosHardware Lista de recursos hardware
     */
    public static void CrearRecursos(ArrayList<RecursoHardware> aRecursosHardware){
        RecursoHardware procesador = new RecursoHardware("procesador");
        RecursoHardware discoDuro = new RecursoHardware("disco duro",10,20,25);
        RecursoHardware impresora = new RecursoHardware("impresora",20,30,15);
        RecursoHardware teclado = new RecursoHardware("teclado",5,10,15);
        aRecursosHardware.add(procesador);
        aRecursosHardware.add(discoDuro);
        aRecursosHardware.add(impresora);
        aRecursosHardware.add(teclado);
        
        System.out.println("******** Recursos Hardware del Sistema ********");
        for(RecursoHardware recurso:aRecursosHardware){
            System.out.println(recurso.toString());
        }
    }
    
    /**
     * Crea la cantidad de procesos padres que quiere el usuario.
     * @param aCantidadProcesos cantidad de procesos a crear
     * @param aBurstTimeMinimo cota inferior para el burst time de los procesos
     * @param aBurstTimeMaximo cota superior para el burst time de los procesos
     * @param aTiempoLlegadaMinimo cota inferior para el tiempo de llegada de los procesos
     * @param aTiempoLlegadaMaximo cota superior para el tiempo de llegada de los procesos
     * @param aProcesosPadres lista de procesos padres
     * @param aTotalBurstTimePadres total de burst time de procesos padres
     * @return Lista con todos los procesos padres generados
     */
    public static int CrearProcesos(int aCantidadProcesos, int aBurstTimeMinimo, int aBurstTimeMaximo,
            int aTiempoLlegadaMinimo, int aTiempoLlegadaMaximo, ArrayList<Pcb> aProcesosPadres,
            int aTotalBurstTimePadres){
        int burstTime;
        int tiempoLlegadaCrea;
        for(int n=0;n<aCantidadProcesos;n++){//Creación procesos padres
            burstTime = HerramientasUtiles.GenerarRandomEntre(aBurstTimeMinimo, aBurstTimeMaximo);
            tiempoLlegadaCrea = HerramientasUtiles.GenerarRandomEntre(aTiempoLlegadaMinimo, aTiempoLlegadaMaximo);
            aTiempoLlegadaMinimo=tiempoLlegadaCrea;//siguientes procesos tendrán tiempos de llegada mayores
            aProcesosPadres.add(new Pcb());
            aProcesosPadres.get(n).AsignarValoresPadre(burstTime, tiempoLlegadaCrea);
            aTotalBurstTimePadres=aTotalBurstTimePadres+burstTime;//variable de cuadre
        }
        return aTotalBurstTimePadres;
    }
}
