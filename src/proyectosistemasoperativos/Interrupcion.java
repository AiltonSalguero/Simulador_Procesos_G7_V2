/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import java.util.*;
import utilitarios.HerramientasUtiles;

/**
 *
 * @author EGARAMENDI
 */
public class Interrupcion {
    
    public static void EvaluarRquerimientoIO(ArrayList<Pcb> aProcesosPadres,ArrayList<Pcb> aProcesosHijos,
            ArrayList<Pcb> aProcesoEjecutandose, ArrayList<Pcb> aProcesosBloqueados,
            ArrayList<RecursoHardware> aRecursosHardware,
            ArrayList<ArrayList<ArrayList<PcbIO>>> aListasProcesosIOs, int aTiempo){
        
        ArrayList<PcbIO> listaProcesosGeneralIO = new ArrayList<>();
        ArrayList<PcbIO> listaProcesosListosIO = new ArrayList<>();
        
        int probabilidadExitosaEventoIO;
        int probabilidadEventoIO;
        int tiempoMinimoRqto;
        int tiempoMaximoRqto;
        int tiempoRequerimiento;
        
        int codigoProceso=0;
        int codigoDispositivoIO;
        
        boolean ocurrioRqtoIO=false;

        int m;

        //Empieza en n=1 porque no se considera al procesador y -2 porque la memoria no procesa
        //for(int n=1;n<=aRecursosHardware.size()-2;n++){ TODO UNA VEZ SE IMPLEMENTE LA MEMORIA
        for(int n=1;n<=aRecursosHardware.size()-1;n++){
            m=n-1;// importante ya qe las listas del procesador se manejan fuera del arreglo de dispositivos IO
            
            //Lista a procesos listos en cada dispositivo IO - TODO , ANTES DEBEN CAPTURAR SUS VALORES
            listaProcesosGeneralIO = aListasProcesosIOs.get(m).get(0);
            listaProcesosListosIO = aListasProcesosIOs.get(m).get(1);
            
            probabilidadExitosaEventoIO=aRecursosHardware.get(n).getProbabilidadExito();
            probabilidadEventoIO = HerramientasUtiles.GenerarProbabilidad();
            
            System.out.println("probabilidad obtenida "+probabilidadEventoIO+" - probabilidad exito del recurso: "+probabilidadExitosaEventoIO);
            if(probabilidadEventoIO<=probabilidadExitosaEventoIO){
                
                System.out.println("ocurrio IO");
                
                ocurrioRqtoIO=true;
                
                tiempoMinimoRqto=aRecursosHardware.get(n).getTiempoRequerimientoMinimo();
                tiempoMaximoRqto=aRecursosHardware.get(n).getTiempoRequerimientoMaximo();
                
                PcbIO procesoIO = new PcbIO();//Crea procesoIO en blanco
                tiempoRequerimiento = HerramientasUtiles.GenerarRandomEntre(tiempoMinimoRqto, 
                    tiempoMaximoRqto);//Genera tiempo de requerimiento
                
                codigoProceso=aProcesoEjecutandose.get(0).getCodigo();
                codigoDispositivoIO=aRecursosHardware.get(n).getCodigo();
                
                procesoIO.AsignarValoresPcbIO(codigoProceso, codigoDispositivoIO, tiempoRequerimiento, 
                    aTiempo);//Asigna valores a procesoIO creado
                
                System.out.println("proceso IO: "+procesoIO.toString());
            
                listaProcesosGeneralIO.add(procesoIO);
                listaProcesosListosIO.add(procesoIO);
                
                System.out.println("tamaño de lista general: "+listaProcesosGeneralIO.size());
                System.out.println("tamaño de lista de listos: "+listaProcesosListosIO.size());
                
                if(!listaProcesosListosIO.isEmpty()){
                    System.out.println("lista de listos PcbIO del recurso \""+codigoDispositivoIO+"\"");
                    for(PcbIO procesoIOs:listaProcesosListosIO){
                        System.out.println(procesoIOs.toString());
                    }
                }
                break;
            }
        }
        
        if(ocurrioRqtoIO){
            if(HerramientasUtiles.EsPadre(aProcesoEjecutandose)){
                for(Pcb proceso:aProcesosPadres){
                    if(proceso.getCodigo()==codigoProceso){
                        proceso.setEstado("Bloqueado");
                        proceso.setTiempoOcurrioBloqueo(aTiempo);
                        aProcesosBloqueados.add(proceso);
                        // TODO Esto es removible
                        System.out.println("Proceso padre bloqueado por IO: "+proceso.toString());
                        break;// ya que los códigos son únicos
                    }
                }
            }else{
                for(Pcb proceso:aProcesosHijos){
                    if(proceso.getCodigo()==codigoProceso){
                        proceso.setEstado("Bloqueado");
                        proceso.setTiempoOcurrioBloqueo(aTiempo);
                        aProcesosBloqueados.add(proceso);
                        // TODO Esto es removible
                        System.out.println("Proceso hijo bloqueado por IO: "+proceso.toString());
                        break;// ya que los códigos son únicos
                    }
                }  
            }
            aRecursosHardware.get(0).setEstaLibre(true);//Liberar procesador
            aProcesoEjecutandose.clear();// Limpiar lista de proceso en ejecución
        }
    }
    
}
