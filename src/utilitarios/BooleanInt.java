/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarios;

/**
 * Clase utilizada para retornar valores múltiples dentro del método 
 * proyectosistemasoperativos.PlanificadorLargoPlazo.CapturarProcesosNuevosCompleto()
 * @author EGARAMENDI
 */
public class BooleanInt {
    private boolean seCreoHijo;
    private int totalBurstTimeHijos;
    
    public void setSeCreoHijo(boolean aSeCreoHijo){
        this.seCreoHijo=aSeCreoHijo;
    }
    public boolean getSeCreoHijo(){
        return this.seCreoHijo;
    }
    
    public void setTotalBurstTimeHijos(int aTotalBurstTimeHijos){
        this.totalBurstTimeHijos=aTotalBurstTimeHijos;
    }
    public int getTotalBurstTimeHijos(){
        return this.totalBurstTimeHijos;
    }
    
    /**
     * Método básico de creación, objeto vacío
     */
    public BooleanInt(){ 
    }
    
    /**
     * Asigna valores a un objeto BooleanInt
     * @param aSeCreoHijo true: se creó proceso hijo - false: no se creó
     * @param aTotalBurstTimeHijos 
     */
    public void AsignarValores(boolean aSeCreoHijo,int aTotalBurstTimeHijos){
        this.setSeCreoHijo(aSeCreoHijo);
        this.setTotalBurstTimeHijos(aTotalBurstTimeHijos);
    }
}
