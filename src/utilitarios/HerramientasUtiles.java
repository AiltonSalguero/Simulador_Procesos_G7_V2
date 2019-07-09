/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarios;

import java.util.*;
import proyectosistemasoperativos.*;

/**
 * Clase con métodos a utilizarse frecuentemente en el proyecto principal
 * @author EGARAMENDI
 */
public class HerramientasUtiles {
    /**
     * Genera un valor aleatorio en un rango inclusivo
     * @param aMin cota inferior
     * @param aMax cota superior
     * @return valor aleatorio generado
     */
    public static int GenerarRandomEntre(int aMin, int aMax) {
        //Código extraído de: https://www.mkyong.com/java/java-generate-random-integers-in-a-range/
	if (aMin > aMax) {
            throw new IllegalArgumentException("El valor máximo es menor que el mínimo");
	}
        Random r = new Random();
	return r.nextInt((aMax - aMin) + 1) + aMin;
    }
    
    /**
     * Genera un valor aleatorio entre 0 y 100 inclusivo
     * @return valor aleatorio generado
     */
    public static int GenerarProbabilidad(){
        Random r = new Random();
        return r.nextInt(101);
    }
    
    /**
     * Establece un delay para mostrar los procesos que realiza el programa
     * Además incrementa el tiempo en 1 cada vez que se ejecuta, simulando el tiempo total del programa
     * @param aTiempo tiempo antes de ejecutarse el método
     * @param aTiempoDelay tiempo que se mostrará el cambio realizado
     * @return tiempo antes de ejecución adicionado en 1 unidad
     */
    public static int IncrementarTiempo(int aTiempo,int aTiempoDelay){
        //Código extraído de: https://it.toolbox.com/question/java-code-a-counter-which-increments-by-1-every-second-040109
        try {
            Thread.sleep(aTiempoDelay);
            aTiempo++;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return aTiempo++;
    }
    
    
    /**
     * Copia en su totalidad los valores de una lista de process padres a otra.
     * @param aListaCopia lista donde se copiarán los valores
     * @param aListaOriginal lista de donde se obtendrán los valores a copiar
     */
    public static void copiarListaPadres(ArrayList<Pcb> aListaCopia, ArrayList<Pcb> aListaOriginal){
        int burstTime;
        int tiempoLlegada;
        aListaCopia.clear();
        for(int n = 0; n < aListaOriginal.size(); n++){
            burstTime=aListaOriginal.get(n).getBurstTime();
            tiempoLlegada=aListaOriginal.get(n).getTiempoLlegadaCrea();
            aListaCopia.add(new Pcb());//Para no referenciar al mismo objeto
            aListaCopia.get(n).AsignarValoresPadre(burstTime,tiempoLlegada);
        }
    }
    
    /**
     * Cambia el estado de un proceso en una lista de procesos
     * @param aCodigoProceso codigo del proceso a modificar
     * @param aListaActualizar lista de procesos a actualizar
     * @param aEstado estado deseado: "Listo" - "Ejecutándose" - "Bloqueado" - "Terminado"
     */
    public static void CambiarEstadoEn(int aCodigoProceso, ArrayList<Pcb>aListaActualizar, String aEstado){
        for(Pcb proceso:aListaActualizar){
            if(aCodigoProceso==proceso.getCodigo()){
                proceso.setEstado(aEstado);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Agrega un proceso a una lista de procesos 
     * @param aCodigoProceso código del proceso a agregar
     * @param aListaFuente lista fuente de procesos
     * @param aListaActualizar lista de procesos a actualizar
     */
    public static void AgregarProcesoEn(int aCodigoProceso, ArrayList<Pcb>aListaFuente,
            ArrayList<Pcb>aListaActualizar){
        for(Pcb proceso:aListaFuente){
            if(aCodigoProceso==proceso.getCodigo()){
                aListaActualizar.add(proceso);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Elimina un proceso de una lista de procesos 
     * @param aCodigoProceso código del proceso a eliminar
     * @param aListaActualizar lista de procesos a actualizar
     */
    public static void EliminarProcesoEn(int aCodigoProceso, ArrayList<Pcb>aListaActualizar){
        for(int n=0; n<aListaActualizar.size();n++){
            if(aListaActualizar.get(n).getCodigo()==aCodigoProceso){
                aListaActualizar.remove(n);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Evalúa si el primer elemento de una lista de procesos es padre o no
     * @param aListaProcesos lista de procesos del cual se evaluará solo el primer elemento
     * @return true: es padre - false: es hijo
     */
    public static boolean EsPadre(ArrayList<Pcb> aListaProcesos){
        return (aListaProcesos.get(0).getTipoParentezco()!="Padre");
    }

    /**
     * Evalúa si hay procesos sin terminar dentro de una lista de procesos
     * @param aListaFuente lista de procesos en donde se buscará
     * @return true: hay procesos sin terminar - falso: se terminaron todos los procesos
     */
    public static boolean HayProcesosSinTerminar(ArrayList<Pcb> aListaFuente){
        for(Pcb proceso: aListaFuente){
            if(proceso.getEstado()!="Terminado"){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cambia el estado de un procesoIO en una lista de procesosIO
     * @param aCodigoIO codigo del procesoIO a modificar
     * @param aListaActualizar lista de procesosIO a actualizar
     * @param aEstado estado deseado: "Listo" - "Ejecutándose" - "Terminado"
     */
    public static void IOCambiarEstadoEn(int aCodigoIO, ArrayList<PcbIO>aListaActualizar, String aEstado){
        for(PcbIO procesoIO:aListaActualizar){
            if(aCodigoIO==procesoIO.getCodigoIO()){
                procesoIO.setEstado(aEstado);
                break;// ya que los códigos son únicos
            }
        }
    }
     
    /**
     * Agrega un procesoIO a una lista de procesosIO
     * @param aCodigoIO código del procesoIO a agregar
     * @param aListaFuente lista fuente de procesosIO
     * @param aListaActualizar lista de procesosIO a actualizar
     */
    public static void IOAgregarProcesoEn(int aCodigoIO, ArrayList<PcbIO>aListaFuente,
            ArrayList<PcbIO>aListaActualizar){
        for(PcbIO procesoIO:aListaFuente){
            if(aCodigoIO==procesoIO.getCodigoIO()){
                aListaActualizar.add(procesoIO);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Elimina un procesoIO en un lista de procesosIO
     * @param aCodigoIO código del procesoIO IO a agregar
     * @param aListaActualizar lista de procesosIO a actualizar
     */
    public static void IOEliminarProcesoEn(int aCodigoIO, ArrayList<PcbIO>aListaActualizar){
        for(int n=0; n<aListaActualizar.size();n++){
            if(aListaActualizar.get(n).getCodigoIO()==aCodigoIO){
                aListaActualizar.remove(n);
                break;// ya que los códigos son únicos
            }
        }
    }

    /**
     * Hace 0 (cero) el número de ráfagas consecutivas para un proceso, cuando este ocupa
     * el procesador. Utilizado en algoritmo "RR".
     * @param aCodigoProceso código del proceso
     * @param aListaFuente lista fuente de procesos donde actualizar
     */
    public static void InicializarRafagasConsecutivas(int aCodigoProceso,ArrayList<Pcb> aListaFuente){
        for(Pcb proceso:aListaFuente){
            if(proceso.getCodigo()==aCodigoProceso){
                proceso.setRafagasConsecutivas(0);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Incrementa en 1 el número de ráfagas consecutivas que un proceso mientras utiliza 
     * el procesador. Utilizado en el algoritmo "RR".
     * @param aListaEjecutandose lista de proceso en ejecución
     */
    public static void IncrementarRafagasConsecutivas(ArrayList<Pcb> aListaEjecutandose){
        int rafagasConsecutivas=aListaEjecutandose.get(0).getRafagasConsecutivas();
        aListaEjecutandose.get(0).setRafagasConsecutivas(rafagasConsecutivas+1);
    }
    
    /**
     * Asigna el tiempo de llegada después de terminarse un evento interruptor a un proceso
     * en una lista fuente de procesos
     * @param aCodigoProceso código del proceso
     * @param aListaFuente lista fuente de procesos donde actualizar
     * @param aTiempo tiempo en el cual termina el evento interuptor
     */
    public static void AsignarTiempoEventoEn(int aCodigoProceso,ArrayList<Pcb> aListaFuente, int aTiempo){
        for(Pcb proceso:aListaFuente){
            if(proceso.getCodigo()==aCodigoProceso){
                proceso.setTiempoLlegadaEvento(aTiempo);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Cuando un proceso toma el procesador, actualiza el tiempo que llevó esperando en cola de listos
     * @param aCodigoProceso código del proceso
     * @param aListaFuente lista fuente de procesos donde actualizar
     * @param aTiempo tiempo
     */
    public static void ActualizarTiempoEsperaEn(int aCodigoProceso,ArrayList<Pcb> aListaFuente, int aTiempo){
        for(Pcb proceso:aListaFuente){
            if(proceso.getCodigo()==aCodigoProceso){
                proceso.setTiempoEspera(aTiempo);
                break;// ya que los códigos son únicos
            }
        }
    }
    
    /**
     * Función propuesta por el grupo para elaborar una métrica de prioridad y obtener
     * mejores resultados que con las políticas vistas a lo largo del curso
     * @param aBurstTime burst time del proceso
     * @param aTiempoLlegada tiempo que el proceso llegó (o volvió) a cola de lisos
     * @param aTiempo tiempo
     * @return 
     */
    public static int FuncionPrioridad(int aBurstTime, int aTiempoLlegada, int aTiempo){
        int prioridad;
        int tiempoEspera = aTiempo-aTiempoLlegada;//lo que va esperando desde la última vez que llegó a cola
        if(tiempoEspera<=aBurstTime){
            prioridad = aBurstTime;
        }else{
            prioridad=Math.round(aBurstTime*aBurstTime/tiempoEspera);
            /*prioridad=aBurstTime*aBurstTime/tiempoEspera;
            if((aBurstTime*aBurstTime)%tiempoEspera>0){
                prioridad=prioridad+1;
            }*/
        }
        return prioridad;
    }
    
    /**
     * Asigna prioridades a cada uno de los elementos de la cola de procesos listos
     * @param aProcesosListos lita de procesos listos
     * @param aTiempo tiempo
     */
    public static void AsignarPrioridades(ArrayList<Pcb> aProcesosListos, int aTiempo){
        int burstTime;
        int tiempoLlegada;
        for(Pcb proceso:aProcesosListos){
            burstTime=proceso.getBurstTime();
            tiempoLlegada=proceso.getTiempoLlegada();
            proceso.setPrioridad(FuncionPrioridad(burstTime,tiempoLlegada,aTiempo));
        }
    }
    
    /**
     * Método que prepara las listas de los porcesosIO para que puedan almacenar objetos procesosIO
     * @param aRecursosHardware lista de los recursos hardware
     * @param aListasProcesosIOs todas las listas de procesosIOs
     * aListasProcesosIOs.get(0) = lista general de procesosIO por dispositivo IO
     * aListasProcesosIOs.get(1) = lista de procesosIO listos por dispositivo IO
     * aListasProcesosIOs.get(2) = lista procesoIO ejecutándose por dispositivo IO
     * aListasProcesosIOs.get(3) = lista de procesosIO terminados por dispositivo IO
     * @return todas las listas de procesosIO preparadas
     */
    public static ArrayList<ArrayList<ArrayList<PcbIO>>> CrearListaDeColasIO(
            ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs){
        int cantidadIOs = aRecursosHardware.size()-1;
        for(int i=0;i<cantidadIOs;i++){
            ArrayList<ArrayList<PcbIO>> ListaPcbIO2Dimensiones = new ArrayList<ArrayList<PcbIO>>(4);
            for(int j=0;j<4;j++){
                ArrayList<PcbIO> listaPcbIO = new ArrayList<PcbIO>();
                ListaPcbIO2Dimensiones.add(listaPcbIO);
            }
            aListasProcesosIOs.add(ListaPcbIO2Dimensiones);
        }
        return aListasProcesosIOs;
    }
    
    /**
     * Limpia la lista entera de listas de procesosIO
    * @param aRecursosHardware lista de los recursos hardware
     * @param aListasProcesosIOs todas las listas de procesosIOs
     */
    public static void LimpiarListaDeColasIO(
            ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs){
        int cantidadIOs = aRecursosHardware.size()-1;
        for(int n=1;n<=cantidadIOs;n++){
            for(int j=0;j<4;j++){
                aListasProcesosIOs.get(n-1).get(j).clear();
            }
        }
    }
        
}

