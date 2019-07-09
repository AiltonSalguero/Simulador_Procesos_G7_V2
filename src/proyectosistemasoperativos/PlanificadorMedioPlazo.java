/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import utilitarios.*;
import java.util.*;

/**
 * Clase que contiene métodos propios de un planificador de medio plazo
 * @author EGARAMENDI
 */
public class PlanificadorMedioPlazo {
    
    /**
     * Ordena las colas de procesos listos y procesosIO listos
     * @param aProcesosListos lista de procesos listos, en espera del procesador
     * @param esExpropiativo true: es expropiativo - false: no es expropiativo
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aAlgoritmo nombre de la política de ordenamiento
     * @param aProcesoEjecutandose lista de proceso en ejecución
     * @param aTiempo tiempo
     * @param aListasProcesosIOs todas las listas de procesosIOs
     * @param aRecursosHardware lista de los recursos hardware
     */
    public static void OrdenarPocesosListos(String aAlgoritmo,boolean esExpropiativo,
            ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,ArrayList<Pcb> aProcesosListos,
            ArrayList<Pcb> aProcesoEjecutandose, ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs, 
            ArrayList<RecursoHardware> aRecursosHardware,int aTiempo){
        //Ordenamiento de procesos en base a a la política en ejecución
        if(!esExpropiativo){//Solo ordena lista de listos, no expropia
            switch(aAlgoritmo){
                case "FCFS":
                    Collections.sort(aProcesosListos, Comparator.comparing(Pcb::getTiempoLlegada));
                    break;
                case "SJF":
                    Collections.sort(aProcesosListos, Comparator.comparing(Pcb::getBurstTime));
                    break;
            }
        }else{//Ordena lista de listos y además puede expropiar
            switch(aAlgoritmo){
                case "SJF":
                    Collections.sort(aProcesosListos, Comparator.comparing(Pcb::getBurstTime));
                    EvaluarExpropiarProcesador(aProcesosPadres, aProcesosHijos, aProcesosListos, 
                            aProcesoEjecutandose, aAlgoritmo, aRecursosHardware, aTiempo);
                    break;
                case "RR"://Expropia procesador no por ordenamiento sino por uso continuo
                    Collections.sort(aProcesosListos, Comparator.comparing(Pcb::getTiempoLlegada));
                    break;
                case "PRIORIDAD":
                    HerramientasUtiles.AsignarPrioridades(aProcesosListos,aTiempo);
                    Collections.sort(aProcesosListos, Comparator.comparing(Pcb::getPrioridad));
                    EvaluarExpropiarProcesador(aProcesosPadres, aProcesosHijos, aProcesosListos, 
                            aProcesoEjecutandose, aAlgoritmo, aRecursosHardware, aTiempo);
                    break;
            }
        }
        /*En caso sea un esquema no expropiativo, la regla por defecto es que si hay un hijo 
        recién creado en cola de listos se le asignará el procesador a él, por lo que aquí se
        trasladará al proceso hijo al incio de la cola de listos*/
        if(!esExpropiativo&&!aProcesosListos.isEmpty()){
            for(Pcb proceso:aProcesosListos){
                if(proceso.getTipoParentezco()=="Padre"&&proceso.getTiempoLlegadaCrea()==(aTiempo)){
                    aProcesosListos.remove(proceso);
                    aProcesosListos.add(0, proceso);
                    break;//solo puede haber un hijo creado en un instante dado ya que no hay más de un procesador
                }
            } 
        }
        // TODO Esto es removible
        if(!aProcesosListos.isEmpty()){
            System.out.println("Procesos listos ordenados");
            for(Pcb proceso:aProcesosListos){
                System.out.println(proceso.toString());
            }
        }
        
        //Ordenamiento de los procesosIO
        OrdenarProcesosIO(aRecursosHardware,aListasProcesosIOs);
    }
    
    /**
     * Ordena las colas procesosIO listos
     * @param aRecursosHardware lista de los recursos hardware
     * @param aListasProcesosIOs todas las listas de procesosIOs
     */
    public static void OrdenarProcesosIO(ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs){
        int m;
        String nombreRecurso;
        ArrayList<PcbIO> ProcesosListosIO = new ArrayList<>();
        for(int n=1;n<=aRecursosHardware.size()-1;n++){
            m=n-1;//IMPORTANTE: las listas del procesador se manejan fuera del arreglo aListasProcesosIOs
            ProcesosListosIO = aListasProcesosIOs.get(m).get(1);//Lista de procesosIO listos en dispositivo IO
            Collections.sort(ProcesosListosIO, Comparator.comparing(PcbIO::getTiempoLlegada));//FCFS
            
            // TODO Esto es removible
            if(!ProcesosListosIO.isEmpty()){
                nombreRecurso=aRecursosHardware.get(n).getNombreRecurso();
                //System.out.println("ProcesosIO listos del recurso \""+(n+1)+"\"");//n+1=codigoRecursoHardware
                System.out.println("ProcesosIO listos y ordenados de \""+nombreRecurso+"\"");
                for(PcbIO procesoIOs:ProcesosListosIO){
                    System.out.println(procesoIOs.toString());
                }
            }
        }
    }
    
    
    /**
     * Asigna el uso de un recurso hardware  a un proceso que esté listo.
     * @param aRecursosHardware lista de los recursos hardware
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosListos lista de procesos listos
     * @param aProcesoEjecutandose lista de proceso en ejecución
     * @param aListasProcesosIOs todas las listas de procesosIOs
     * @param aTiempo tiempo
     */
    public static void AsignarRecurso(ArrayList<RecursoHardware> aRecursosHardware, 
            ArrayList<Pcb> aProcesosListos, ArrayList<Pcb> aProcesosPadres, ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesoEjecutandose,ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs,
            int aTiempo){
        
        //Asignar procesador
        AsignarProcesador(aRecursosHardware, aProcesosPadres, aProcesosHijos, aProcesosListos, 
                aProcesoEjecutandose,aTiempo);
        //Asignar dispositivos IO
        AsignarRecursoIO(aRecursosHardware, aListasProcesosIOs);
    }
    
    /**
     * Asigna el procesador a un proceso
     * @param aRecursosHardware lista de los recursos hardware
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosListos lista de procesos listos
     * @param aProcesoEjecutandose lista de proceso en ejecución
     * @param aTiempo tiempo
     */
    public static void AsignarProcesador(ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesosListos,ArrayList<Pcb> aProcesoEjecutandose,int aTiempo){
        int codigoPrimerListo;
        boolean recursoEstaLibre = aRecursosHardware.get(0).getEstaLibre();
        //Verificar si procesador está libre y si hay procesos en cola de listos
        if(recursoEstaLibre&&!aProcesosListos.isEmpty()){
            codigoPrimerListo=aProcesosListos.get(0).getCodigo();
            if(HerramientasUtiles.EsPadre(aProcesosListos)){//acualiza en lista de padres
                HerramientasUtiles.CambiarEstadoEn(codigoPrimerListo, aProcesosPadres, "Ejecutándose");
                HerramientasUtiles.ActualizarTiempoEsperaEn(codigoPrimerListo, aProcesosPadres, aTiempo);
                HerramientasUtiles.AgregarProcesoEn(codigoPrimerListo, aProcesosPadres, aProcesoEjecutandose);
                //Inicializar ráfagas consecutivas - útil para política RR
                HerramientasUtiles.InicializarRafagasConsecutivas(codigoPrimerListo, aProcesosPadres);
            }else{//acualiza en lista de hijo
                HerramientasUtiles.CambiarEstadoEn(codigoPrimerListo, aProcesosHijos, "Ejecutándose");
                HerramientasUtiles.ActualizarTiempoEsperaEn(codigoPrimerListo, aProcesosHijos, aTiempo);
                HerramientasUtiles.AgregarProcesoEn(codigoPrimerListo, aProcesosHijos, aProcesoEjecutandose);
                //Inicializar ráfagas consecutivas - útil para política RR
                HerramientasUtiles.InicializarRafagasConsecutivas(codigoPrimerListo, aProcesosHijos);
            }
            HerramientasUtiles.EliminarProcesoEn(codigoPrimerListo, aProcesosListos);
            aRecursosHardware.get(0).setEstaLibre(false);
        }
    }
    
    /**
     * Asigna los dispositivos IO a procesos IO
     * @param aRecursosHardware lista de los recursos hardware
     * @param aListasProcesosIOs todas las listas de procesosIOs
     */
    public static void AsignarRecursoIO(ArrayList<RecursoHardware> aRecursosHardware, 
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs){
        //Variables para almacenar las colas por cada dispositivo IO
        ArrayList<PcbIO> ProcesosGeneralIO = new ArrayList<>();
        ArrayList<PcbIO> ProcesosListosIO = new ArrayList<>();
        ArrayList<PcbIO> ProcesoEjecutandoseIO = new ArrayList<>();
        int m;
        int codigoPrimerIOListo;
        boolean recursoEstaLibre;
        for(int n=1;n<=aRecursosHardware.size()-1;n++){
            m=n-1;// importante ya qe las listas del procesador se manejan fuera del arreglo de dispositivos IO
            //Listas de procesos IO de cada dispositivo IO
            ProcesosGeneralIO = aListasProcesosIOs.get(m).get(0);
            ProcesosListosIO = aListasProcesosIOs.get(m).get(1);
            ProcesoEjecutandoseIO = aListasProcesosIOs.get(m).get(2);
            recursoEstaLibre = aRecursosHardware.get(n).getEstaLibre();
            //Verificar si el dispostivo IO está libre y si hay procesos en su cola de listos
            if(recursoEstaLibre&&!ProcesosListosIO.isEmpty()){
                codigoPrimerIOListo=ProcesosListosIO.get(0).getCodigoIO();
                HerramientasUtiles.IOCambiarEstadoEn(codigoPrimerIOListo, ProcesosGeneralIO, "Ejecutándose");
                HerramientasUtiles.IOAgregarProcesoEn(codigoPrimerIOListo, ProcesosGeneralIO, ProcesoEjecutandoseIO);
                HerramientasUtiles.IOEliminarProcesoEn(codigoPrimerIOListo, ProcesosListosIO);
                //Ocupar el recurso
                aRecursosHardware.get(n).setEstaLibre(false);
            }   
        }  
    }
      
    /**
     * Evalúa si se debe expropiar del procesador al proceso en ejecución
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosListos lista de procesos listos
     * @param aProcesoEjecutandose lista de proceso ejecutándose
     * @param aAlgoritmo nombre de la política de ordenamiento
     * @param aRecursosHardware lista de los recursos hardware
     * @param aTiempo tiempo
     */
    public static void EvaluarExpropiarProcesador(ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesosListos,ArrayList<Pcb> aProcesoEjecutandose,String aAlgoritmo,
            ArrayList<RecursoHardware> aRecursosHardware,int aTiempo){
        //Se inicializan para evitar error
        int criterioProcesoEjecucion=0;//almacena atributo de asignación del proceso en ejecución
        int criterioProcesoListo=0;//almacena atributo de asignación del primero proceso en cola
        if(!aProcesoEjecutandose.isEmpty()&&!aProcesosListos.isEmpty()){//Prevenir error
            switch(aAlgoritmo){//solo se expropia por un atributo de asgnación en "SJF" y "PRIORIDAD"
                case "SJF":
                    criterioProcesoEjecucion=aProcesoEjecutandose.get(0).getBurstTime();
                    criterioProcesoListo=aProcesosListos.get(0).getBurstTime();
                    break;
                case "PRIORIDAD":
                    criterioProcesoEjecucion=aProcesoEjecutandose.get(0).getPrioridad();
                    criterioProcesoListo=aProcesosListos.get(0).getPrioridad();
                    break;
            }
            //menor valor del atributo de asignación manda
            if(criterioProcesoListo<criterioProcesoEjecucion){//se expropia procesador
                ExpropiarProcesador(aProcesosPadres,aProcesosHijos,aProcesosListos,
                        aProcesoEjecutandose,aRecursosHardware,aTiempo);
            }
        }
    }
    
    /**
     * Expropia del procesador al proceso en ejecución
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosListos lista de procesos listos
     * @param aProcesoEjecutandose lista de proceso ejecutándose
     * @param aRecursosHardware lista de los recursos hardware
     * @param aTiempo tiempo 
     */
    public static void ExpropiarProcesador(ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesosListos,ArrayList<Pcb> aProcesoEjecutandose,
            ArrayList<RecursoHardware> aRecursosHardware,int aTiempo){
        System.out.println("se expropia procesador");
        int aCodigoProcesoEje=aProcesoEjecutandose.get(0).getCodigo();
        if(HerramientasUtiles.EsPadre(aProcesoEjecutandose)){//acualiza en lista de padres
            HerramientasUtiles.CambiarEstadoEn(aCodigoProcesoEje, aProcesosPadres, "Listo");
            HerramientasUtiles.AsignarTiempoEventoEn(aCodigoProcesoEje, aProcesosPadres, aTiempo+1);
            HerramientasUtiles.AgregarProcesoEn(aCodigoProcesoEje, aProcesosPadres, aProcesosListos);
        }else{//acualiza en lista de hijos
            HerramientasUtiles.CambiarEstadoEn(aCodigoProcesoEje, aProcesosHijos, "Listo");
            HerramientasUtiles.AsignarTiempoEventoEn(aCodigoProcesoEje, aProcesosHijos, aTiempo+1);
            HerramientasUtiles.AgregarProcesoEn(aCodigoProcesoEje, aProcesosHijos, aProcesosListos);
        }
        HerramientasUtiles.EliminarProcesoEn(aCodigoProcesoEje, aProcesoEjecutandose);
        aRecursosHardware.get(0).setEstaLibre(true);
    }
}
