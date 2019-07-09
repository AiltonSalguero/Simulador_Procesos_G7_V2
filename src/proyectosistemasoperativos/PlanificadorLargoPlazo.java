/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import java.util.*;
import utilitarios.*;


/**
 * Clase que contiene métodos propios de un planificador de largo plazo
 * @author EGARAMENDI
 */
public class PlanificadorLargoPlazo {
    
    /**
     * Captura los procesos nuevos, padres e hijos, que llegan en un tiempo t y los agrega al sistema
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosNuevos lista de procesos nuevos a actualizar
     * @param aProcesosSistema lista de procesos del sistema
     * @param aTiempo tiempo
     * @param aTotalBurstTimeHijos acumulador de los burst time de los hijos, para realiza cuadre
     * @param aSeCreoHijo variable indicadora si en la anterior ráfaga de ejecución se creo un hijo
     * @return [aSeCreoHijo,aTotalBurstTimeHijos] actualizados
     */
    public static BooleanInt CapturarProcesosNuevos(ArrayList<Pcb> aProcesosPadres, 
            ArrayList<Pcb> aProcesosHijos, ArrayList<Pcb> aProcesosNuevos, ArrayList<Pcb> aProcesosSistema,
            int aTiempo, int aTotalBurstTimeHijos,boolean aSeCreoHijo){
        BooleanInt retornoMultiple = new BooleanInt();//variable de retorno múltiple
        int burstTimeHijo;        
        CapturarNuevos(aProcesosPadres,aProcesosNuevos,aTiempo);//Captura padres nuevos
        if(aSeCreoHijo){//Capturar hijo nuevo
            burstTimeHijo=aProcesosHijos.get(aProcesosHijos.size()-1).getBurstTime();
            aTotalBurstTimeHijos=aTotalBurstTimeHijos+burstTimeHijo;//Acumulador de burstTime de todos los hijos
            CapturarNuevos(aProcesosHijos, aProcesosNuevos, aTiempo);//Captura hijo nuevo
            
            // TODO Esto es removible - para mostrar si se creó el hijo
            System.out.print("Proceso hijo nuevo¡¡ - Total de hijos: "+aProcesosHijos.size());
            System.out.println("Hijo: "+aProcesosNuevos.get(aProcesosNuevos.size()-1).toString());
        }
        aSeCreoHijo = false;//Ya se capturó proceso hijo nuevo
        
        // TODO Esto es removible - facilita visualizacón en ventana de comando de procesos nuevos
        if(!aProcesosNuevos.isEmpty()){
            System.out.println("PROCESOS NUEVOS -- Tiempo :"+aTiempo);
            for(Pcb proceso:aProcesosNuevos){
                System.out.println(proceso.toString());
            }
        }
        AgregarProcesos(aProcesosNuevos,aProcesosSistema);//Carga procesos nuevos al sistema
        retornoMultiple.AsignarValores(aSeCreoHijo,aTotalBurstTimeHijos);//variable de retorno
        return retornoMultiple;
    }
    
    /**
     * Captura los procesos nuevos que llegan en un tiempo t y los agrega al sistema. Se utiliza
    dentro del método proyectosistemasoperativos.PlanificadorLargoPlazo.CapturarProcesosNuevos()
     * @param aProcesosFuente lista fuente de procesos
     * @param aProcesosNuevos lista de procesos nuevos a actualizar
     * @param aTiempo tiempo
     */
    public static void CapturarNuevos(ArrayList<Pcb> aProcesosFuente, 
            ArrayList<Pcb> aProcesosNuevos,int aTiempo){
        int tiempoLlegada;//tiempo en el que el proceso llegó al sistema
        int tiempoLlegadaEvento;//tiempo en el que se cumplió un evento interruptor del proceso
        for(Pcb proceso:aProcesosFuente){
            tiempoLlegada=proceso.getTiempoLlegada();
            tiempoLlegadaEvento=proceso.getTiempoLlegadaEvento();
            /* if() Evalúa que el tiempo que llegó a cola sea el mismo que el tiempo de la simulación
            y además que no haya llegado después de un evento interruptor (ya que no sería nuevo)*/
            if((tiempoLlegada==aTiempo && tiempoLlegadaEvento==0)){
                aProcesosNuevos.add(proceso);
            }
        }
    }
    
    /**
     * Transfiere los elementos de la lista procesos nuevos a la lista procesos listos, luego
     * limpia la lista de procesos nuevos
     * @param aProcesosNuevos lista de procesos nuevos
     * @param aProcesosListos lista de procesos listos a actualizas
     */
    public static void CargarProcesosListosCompleto(ArrayList<Pcb> aProcesosNuevos,
            ArrayList<Pcb> aProcesosListos){
        if(!aProcesosNuevos.isEmpty()){
            AgregarProcesos(aProcesosNuevos,aProcesosListos);
            aProcesosNuevos.clear();
        }
    }
    
    /**
     * Agrega los procesos nuevos capturados a otra lista de procesos cualquiera
     * @param aProcesosNuevos lista de procesos nuevos
     * @param aProcesos lista de procesos a actualizar
     */
    public static void AgregarProcesos(ArrayList<Pcb> aProcesosNuevos 
            ,ArrayList<Pcb> aProcesos) {
        for(Pcb proceso:aProcesosNuevos){
            aProcesos.add(proceso);
        }
    }
    
    /**
     * Captura los procesosIO generados y los agrega a la cola de listos del 
     * dispositivo IO correspondiente.
     * @param aProcesosGeneralesIO lista general de procesosIO de un dispositivo IO
     * @param aProcesosListosIO lista de procesosIO de un dispositivo IO
     * @param aTiempo tiempo
     * @return lista de procesosIO listos actualizada
     */
    public static ArrayList<PcbIO> CapturarProcesosIOListos(ArrayList<PcbIO> aProcesosGeneralesIO, 
            ArrayList<PcbIO> aProcesosListosIO,int aTiempo){
        for(PcbIO proceso:aProcesosGeneralesIO){
            if((proceso.getTiempoLlegada()==aTiempo)){
                aProcesosListosIO.add(proceso);
            }
        }
        return aProcesosListosIO;
    }
}
