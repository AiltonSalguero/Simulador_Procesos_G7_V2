/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import utilitarios.HerramientasUtiles;
import java.util.*;

/**
 *
 * @author EGARAMENDI
 */
public class PlanificadorCortoPlazo {
    
    /**
     * Dismiye en 1 unidad el burst time del proceso en ejecución y a la vez aumenta
     * su número de ráfagas consecutivas en 1 unidad (para algoritmo "RR")
     * @param aProcesoEjecutandose lista de proceso en ejecución
     */
    public static void ActualizarPC(ArrayList<Pcb> aProcesoEjecutandose){
        
        if(!aProcesoEjecutandose.isEmpty()){//Evalúa si efectivamente existe proceso en ejecución
                // TODO Esto es removible - seguimiento de error
                System.out.println(aProcesoEjecutandose.toString());
                // TODO Esto es removible - seguimiento de error
                /*for (Pcb proceso:procesosSistema){
                    System.out.println(proceso.toString());
                }*/
        if(aProcesoEjecutandose.get(0).getBurstTime()!=0){
            int burstTime=aProcesoEjecutandose.get(0).getBurstTime();
            aProcesoEjecutandose.get(0).setBurstTime(burstTime-1);// Actualiza PC
            HerramientasUtiles.IncrementarRafagasConsecutivas(aProcesoEjecutandose);
            // TODO Esto es removible 
            System.out.println("En ejecucion (BT-1): "+aProcesoEjecutandose.get(0).toString());
        }
        }
    }

    /**
     * Evalúa la creación de un proceso hijo durante la ejecución de un potecial proceso padre.
     * @param aProcesosHijo lista de procesos hijos
     * @param aSeCreoHijo variable a retornar
     * @param aBurstTimeMinimo cota inferior para el burst time del hijo
     * @param aBurstTimeMaximo cota superior para el burst time del hijo
     * @param aTiempo tiempo
     * @param aProbabilidadExito probabilidad máxima de éxito para creación del hijo
     * @param aProcesosPadres lista de procesos padres
     * @param aRecursosHardware lista de los recursos hardware
     * @param aProcesoEjecutandose lista de proceso en ejecución
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @return aSeCreoHijo - true: se creó proceso hijo - false: no se creó
     */
    public static boolean EvaluarCrearHijo(ArrayList<Pcb> aProcesosHijo, boolean aSeCreoHijo,
            int aBurstTimeMinimo, int aBurstTimeMaximo, int aTiempo, int aProbabilidadExito,
            ArrayList<Pcb> aProcesosPadres,ArrayList<RecursoHardware> aRecursosHardware, 
            ArrayList<Pcb> aProcesoEjecutandose,ArrayList<Pcb> aProcesosBloqueados){
        int burstTime;
        int probabilidadCrearHijo=HerramientasUtiles.GenerarProbabilidad();
        int codProcesoEjecucion = aProcesoEjecutandose.get(0).getCodigo();
        if(probabilidadCrearHijo<=aProbabilidadExito){//Evaluar si se crea hijo
            Pcb procesoHijoCreado = new Pcb();
            burstTime=HerramientasUtiles.GenerarRandomEntre(aBurstTimeMinimo, aBurstTimeMaximo);
            procesoHijoCreado.AsignarValoresHijo(codProcesoEjecucion,burstTime,aTiempo+1);
            aProcesosHijo.add(procesoHijoCreado);
            aSeCreoHijo = true;
            BloquearPadre(codProcesoEjecucion, aProcesosPadres, aProcesosBloqueados, aTiempo);
            aRecursosHardware.get(0).setEstaLibre(true);//Liberar procesador
            aProcesoEjecutandose.clear();// Limpiar lista de proceso en ejecución
        }
        return aSeCreoHijo;
    }
    
    /**
     * Bloquea un proceso luego de un evento interruptor
     * @param aCodProcesoEjecucion codigo del proceso a bloquear
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aTiempo tiempo
     */
    public static void BloquearPadre(int aCodProcesoEjecucion,ArrayList<Pcb> aProcesosPadres,
            ArrayList<Pcb> aProcesosBloqueados,int aTiempo){
        for(Pcb procesoPadre: aProcesosPadres){
            if(procesoPadre.getCodigo()==aCodProcesoEjecucion){
                procesoPadre.setEstado("Bloqueado");
                procesoPadre.setTiempoOcurrioBloqueo(aTiempo);
                aProcesosBloqueados.add(procesoPadre);
                // TODO Esto es removible
                System.out.println("Bloqueado por hijo: "+procesoPadre.toString());
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Desbloquea un proceso padre luego de que su hijo haya sido terminado.
     * @param aProcesoEjecutandose lista de proceso en ejecución
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aProcesosListos lista de procesos listos
     * @param aTiempo tiempo
     */
    public static void DesbloquearPadrexHijo(ArrayList<Pcb> aProcesoEjecutandose,ArrayList<Pcb> aProcesosPadres,
            ArrayList<Pcb> aProcesosBloqueados,ArrayList<Pcb> aProcesosListos,int aTiempo){
        int codigoPadre = aProcesoEjecutandose.get(0).getCodigoPcbParentezco();
        for(Pcb proceso:aProcesosPadres){// Cambiar estado del padre
            if(proceso.getCodigo()==codigoPadre){
                proceso.setEstado("Listo");
                proceso.setTiempoLlegadaEvento(aTiempo);
                proceso.setTiempoBloqueado();
                aProcesosListos.add(proceso);
                HerramientasUtiles.EliminarProcesoEn(codigoPadre, aProcesosBloqueados);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Evalúa si un proceso debe terminarse
     * @param aProcesoEjecutandose lista de proceso en ejecución
     * @return true: es fin de proces - false: no es fin de proceso
     */
    public static boolean EsFinProceso(ArrayList<Pcb> aProcesoEjecutandose){
        return (aProcesoEjecutandose.get(0).getBurstTime()==0);
    }
    
    /**
     * Termina un proceso y/o en su defecto evalúa expropiar del procesador en caso lo
     * determine el algoritmo "RR"
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijo lista de procesos hijos
     * @param aProcesoEjecutandose lista de proceso ejecutándose
     * @param aProcesosListos lsita de procesos listos
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aProcesosTerminados lista de procesos terminados
     * @param aRecursosHardware lista de los recursos hardware
     * @param aTiempo tiempo
     * @param aAlgoritmo nombre del algoritmo de ordenamiento
     * @param aCuantoRR cuanto, para aplicar agoritmo "RR"
     */
    public static void TerminarProceso(ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijo,
            ArrayList<Pcb> aProcesoEjecutandose,ArrayList<Pcb> aProcesosListos,
            ArrayList<Pcb> aProcesosBloqueados,ArrayList<Pcb> aProcesosTerminados,
            ArrayList<RecursoHardware> aRecursosHardware,int aTiempo,String aAlgoritmo, int aCuantoRR){
        if(!aProcesoEjecutandose.isEmpty()){
            if(EsFinProceso(aProcesoEjecutandose)){
                if(HerramientasUtiles.EsPadre(aProcesoEjecutandose)){
                    TerminarProcesoPadre(aProcesoEjecutandose, aProcesosPadres,
                            aProcesosTerminados,aRecursosHardware,aTiempo);
                }else{
                    TerminarProcesoHijo(aProcesoEjecutandose, aProcesosPadres, 
                            aProcesosHijo, aProcesosTerminados,aProcesosBloqueados,aRecursosHardware, 
                            aProcesosListos, aTiempo);
                }
            }else if(aAlgoritmo=="RR"){//Solo se expropia (aquí) si es algoritmo RoundRobin
                EvaluarQuitarProcesadorRR(aProcesosPadres, aProcesosHijo, 
                        aProcesoEjecutandose, aProcesosListos, aRecursosHardware,aTiempo,aCuantoRR);
            }
        }
    }
    
    /**
     * Termina un proceso padre
     * @param aProcesoEjecutandose lista de proceso ejecutándose
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosTerminados lista de procesos hijos
     * @param aRecursosHardware lista de los recursos hardware
     * @param aTiempo tiempo
     */
    public static void TerminarProcesoPadre(ArrayList<Pcb> aProcesoEjecutandose,ArrayList<Pcb> aProcesosPadres,
            ArrayList<Pcb> aProcesosTerminados,
            ArrayList<RecursoHardware> aRecursosHardware,int aTiempo){
        int codigoProcesoEjecutandose =aProcesoEjecutandose.get(0).getCodigo();
        for(Pcb proceso:aProcesosPadres){
            if(proceso.getCodigo()==codigoProcesoEjecutandose){
                proceso.setEstado("Terminado");
                //aProcesoEjecutandose.remove(0);// IMPORTANTE: no debe ir dentro porque genera error
                proceso.setTiempoTermino(aTiempo+1);
                aProcesosTerminados.add(proceso);
                break;// ya que los códigos son únicos
            }
        }
        aRecursosHardware.get(0).setEstaLibre(true);// libera recurso
        aProcesoEjecutandose.remove(0);// limpia lista de proceso ejecutándose
    }
    
    /**
     * Termina un proceso hijo
     * @param aProcesoEjecutandose lista de proceso ejecutándose
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosTerminados lista de procesos hijos
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aRecursosHardware lista de los recursos hardware
     * @param aProcesosListos lista de procesos listos
     * @param aTiempo tiempo
     */
    public static void TerminarProcesoHijo(ArrayList<Pcb> aProcesoEjecutandose,
            ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesosTerminados, ArrayList<Pcb> aProcesosBloqueados
            ,ArrayList<RecursoHardware> aRecursosHardware,ArrayList<Pcb> aProcesosListos,int aTiempo){
        DesbloquearPadrexHijo(aProcesoEjecutandose, aProcesosPadres, aProcesosBloqueados, 
                aProcesosListos, aTiempo);
        //Una vez desbloqueado el padre, se termina el hijo como si fuera un proceso padre
        TerminarProcesoPadre(aProcesoEjecutandose, aProcesosHijos, aProcesosTerminados, 
                aRecursosHardware, aTiempo); 
    }
    

    /**
     * Evalúa expropiar del procesador en caso lo determine el algoritmo "RR"
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesoEjecutandose lista de proceso ejecutándose
     * @param aProcesosListos lista de procesos listos
     * @param aListaRecursosHardware lista de los recursos hardware
     * @param aTiempo tiempo
     * @param aCuantoRR cuantum
     */
    public static void EvaluarQuitarProcesadorRR(ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesoEjecutandose,ArrayList<Pcb> aProcesosListos,
            ArrayList<RecursoHardware> aListaRecursosHardware,int aTiempo, int aCuantoRR){
        if(aProcesoEjecutandose.get(0).getRafagasConsecutivas()==aCuantoRR){
            int rafagasConsecutivas = aProcesoEjecutandose.get(0).getRafagasConsecutivas();
            System.out.println("Ráfagas consecutivas = "+rafagasConsecutivas);
            System.out.println("Ocurre expropiación por RR");
            PlanificadorMedioPlazo.ExpropiarProcesador(aProcesosPadres,aProcesosHijos,
                    aProcesosListos,aProcesoEjecutandose,aListaRecursosHardware,aTiempo);
        }
    }
    
    /**
     * Disminuye en 1 unidad el tiempo de requerimiento de IO del procesoIO en ejecución
     * @param aRecursosHardware lista de los recursos hardware
     * @param aListasProcesosIOs todas las listas de procesosIOs
     */
    public static void ActualizarPCIO(ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs){
        ArrayList<PcbIO> ProcesoIOEjecucion = new ArrayList<>();
        int m;
        for(int n=1;n<=aRecursosHardware.size()-1;n++){
            m=n-1;//IMPORTANTE: las listas del procesador se manejan fuera del arreglo aListasProcesosIOs
            ProcesoIOEjecucion = aListasProcesosIOs.get(m).get(2);
            if(!ProcesoIOEjecucion.isEmpty()){
                if(ProcesoIOEjecucion.get(0).getTiempoRequerimiento()!=0){
                    int tiempoRqto=ProcesoIOEjecucion.get(0).getTiempoRequerimiento();
                    ProcesoIOEjecucion.get(0).setTiempoRequerimiento(tiempoRqto-1);
                    // TODO Esto es removible 
                    System.out.println("En ejecucion (BT-1) recuroso "+(n+1)+" : "
                            +ProcesoIOEjecucion.get(0).toString());
                }
            }
        }
    }

    /**
     * Recorre todos los dispositivos IO y sus procesosIO en ejecución, evalúa si deben
     * ser terminados o no. Si deben ser terminados, los termina.
     * @param aRecursosHardware lista de los recursos hardware
     * @param aListasProcesosIOs todas las listas de procesosIOs
     * @param aTiempo tiempo
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aProcesosListos lista de procesos listos
     */
    public static void EvaluarTerminarProcesosIO(ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs,int aTiempo,
            ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesosBloqueados,ArrayList<Pcb> aProcesosListos){
        ArrayList<PcbIO> ProcesosGeneralIO = new ArrayList<>();
        ArrayList<PcbIO> ProcesoEjecutandoseIO = new ArrayList<>();
        ArrayList<PcbIO> ProcesosTerminadosIO = new ArrayList<>();
        int m;
        for(int n=1;n<=aRecursosHardware.size()-1;n++){
            m=n-1;//IMPORTANTE: las listas del procesador se manejan fuera del arreglo aListasProcesosIOs
            ProcesosGeneralIO = aListasProcesosIOs.get(m).get(0);
            ProcesoEjecutandoseIO = aListasProcesosIOs.get(m).get(2);
            ProcesosTerminadosIO = aListasProcesosIOs.get(m).get(3);
            EvaluarTerminarProcesoIO(n, ProcesoEjecutandoseIO, ProcesosGeneralIO, 
                    ProcesosTerminadosIO, aProcesosPadres, aProcesosHijos, aProcesosBloqueados, 
                    aProcesosListos, aRecursosHardware, aTiempo);
        }
    }
    
    /**
     * Evalúa solamente en un dispositivo IO si el procesoIO en ejecución debe
     * ser terminado. Si debe ser terminado, lo termina.
     * @param aN posición del recurso hardware en la lista de recursos hardware
     * @param ProcesoEjecutandoseIO lista de procesoIO ejecutándose, del dispositivo IO
     * @param ProcesosGeneralIO lista general de procesosIO, del dispositivo IO
     * @param ProcesosTerminadosIO lista de procesosIO terminados, del dispositivo IO
     * @param aProcesosPadres lista de procesos padres
     * @param aProcesosHijos lista de procesos hijos
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aProcesosListos lista de procesos listos
     * @param aRecursosHardware lista de los recursos hardware
     * @param aTiempo tiempo
     */
    public static void EvaluarTerminarProcesoIO(int aN, ArrayList<PcbIO> ProcesoEjecutandoseIO,
            ArrayList<PcbIO> ProcesosGeneralIO, ArrayList<PcbIO> ProcesosTerminadosIO,
            ArrayList<Pcb> aProcesosPadres, ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesosBloqueados, ArrayList<Pcb> aProcesosListos,
            ArrayList<RecursoHardware> aRecursosHardware, int aTiempo){
        int codProcesoADesbloquear;
        if(!ProcesoEjecutandoseIO.isEmpty()){
            if(ProcesoEjecutandoseIO.get(0).getTiempoRequerimiento()==0){//si es fin de proceso
                codProcesoADesbloquear=TerminarProcesoIO(ProcesoEjecutandoseIO, 
                        ProcesosGeneralIO, ProcesosTerminadosIO);
                DesbloquearProcesoxIO(aProcesosHijos, codProcesoADesbloquear, aProcesosListos, 
                        aProcesosBloqueados, aTiempo);
                DesbloquearProcesoxIO(aProcesosPadres, codProcesoADesbloquear, aProcesosListos, 
                        aProcesosBloqueados, aTiempo);
                aRecursosHardware.get(aN).setEstaLibre(true);// libera recurso
                ProcesoEjecutandoseIO.remove(0);// limpia lista de proceso ejecutándose
            }
        }
    }
    
    /**
     * Termina un procesoIO, de un determinado dispositivo IO. Devolviendo el código
     * del proceso que debe ser desbloqueado por cumplimiento de requerimiento de IO.
     * @param ProcesoEjecutandoseIO lista de procesoIO ejecutándose, del dispositivo IO
     * @param ProcesosGeneralIO lista general de procesosIO, del dispositivo IO
     * @param ProcesosTerminadosIO lista de procesosIO terminados, del dispositivo IO
     * @return código del proceso a desbloquear
     */
    public static int TerminarProcesoIO(ArrayList<PcbIO> ProcesoEjecutandoseIO,
            ArrayList<PcbIO> ProcesosGeneralIO,ArrayList<PcbIO> ProcesosTerminadosIO){
        int codigoProcesoIOEjecutandose;
        int codProcesoADesbloquear=0;
        System.out.println("Terminó IO ¡¡¡");
        codigoProcesoIOEjecutandose=ProcesoEjecutandoseIO.get(0).getCodigoIO();
        for(PcbIO proceso:ProcesosGeneralIO){
            if(proceso.getCodigoIO()==codigoProcesoIOEjecutandose){
                codProcesoADesbloquear=proceso.getCodigo();
                proceso.setEstado("Terminado");
                ProcesosTerminadosIO.add(proceso);
                return codProcesoADesbloquear;
            }
        }
        return codProcesoADesbloquear;
    }
    
    /**
     * Desbloquea un proceso luego de cumplir su requerimiento de IO.
     * @param aProcesosFuente lista de procesos (padres o hijos)
     * @param aCodProcADesbloquear código del proceso a desbloquear
     * @param aProcesosListos lista de procesos listos
     * @param aProcesosBloqueados lista de procesos bloqueados
     * @param aTiempo tiempo
     */
    public static void DesbloquearProcesoxIO(ArrayList<Pcb> aProcesosFuente,int aCodProcADesbloquear,
            ArrayList<Pcb> aProcesosListos,ArrayList<Pcb> aProcesosBloqueados, int aTiempo){
        if(!aProcesosFuente.isEmpty()&&aCodProcADesbloquear!=0){
            for(Pcb proceso:aProcesosFuente){
                if(proceso.getCodigo()==aCodProcADesbloquear){
                    proceso.setEstado("Listo");
                    proceso.setTiempoLlegadaEvento(aTiempo);
                    proceso.setTiempoBloqueado();
                    aProcesosListos.add(proceso);
                    HerramientasUtiles.EliminarProcesoEn(aCodProcADesbloquear, aProcesosBloqueados);
                    break;// ya que los códigos son únicos
                }
            }
        }
    }
    
}

