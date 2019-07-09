/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import java.util.*;
import utilitarios.*;

/**
 * Política de asignación de reursos.
 * @author EGARAMENDI
 */
public class Politica {
    private static int contador;//Para autogenerar los códigos de las políticas
    private int codigo;//Código identificador de la política
    private String politica;//Nombre descriptor de la política de ordenamiento
    private boolean esExpropiativo;//Esquema expropiativo o no expropiativo
    private int tiempoEsperaPromedio;//Promedio de los tiempos de espera de todos los procesos
    private int tiempoRetornoPromedio;//Promedio de los tiempos de retorno de todos los procesos
    private int tiempoTotal;//Tiempo total de ejecución de todos los procesos
    private int cantidadHijos;//Cantidad de hijos generados
    private boolean yaTermino;//Estado de finalización
    /* OBS1: tiempoEsperaPromedio y tiempoRetornoPromedio son criterios de comparación 
    del performance de las distintas políticas.*/
    /* OBS2: cantidadHijos Para verificar si existe un grado de correlación con el performance*/
    
    public int getCodigo(){
        return this.codigo;
    }
    public void setCodigo(int aCodigo){
        this.codigo=aCodigo;
    }
    
    public String getPolitica(){
        return this.politica;
    }
    public void setPolitica(String aPolitica){
        this.politica=aPolitica;
    }
    
    public boolean getEsExpropiativo(){
        return this.esExpropiativo;
    }
    public void setEsExpropiativo(boolean EsExpropiativo){
        this.esExpropiativo=EsExpropiativo;
    }
    
    public int getTiempoEsperaPromedio(){
        return this.tiempoEsperaPromedio;
    }
    public void setTiempoEsperaPromedio(int aTiempoEsperaPromedio){
        this.tiempoEsperaPromedio=aTiempoEsperaPromedio;
    }
    
    public int getTiempoRetornoPromedio(){
        return this.tiempoRetornoPromedio;
    }
    public void setTiempoRetornoPromedio(int aTiempoRespuestaPromedio){
        this.tiempoRetornoPromedio=aTiempoRespuestaPromedio;
    }
    
    public int getTiempoTotal(){
        return this.tiempoTotal;
    }
    public void setTiempoTotal(int aTiempoTotal){
        this.tiempoTotal=aTiempoTotal;
    }
    
    public int getCantidadHijos(){
        return this.cantidadHijos;
    }
    public void setCantidadHijos(int aCantidadHijos){
        this.cantidadHijos=aCantidadHijos;
    }
    
    public boolean getYaTermino(){
        return this.yaTermino;
    }
    public void setYaTermino(boolean aYaTermino){
        this.yaTermino=aYaTermino;
    }
    
    
    /**
     * Método creador de una política
     * @param aPolitica nombre de la política
     * @param aEsExpropiativo true: esquema Expropiativo - false: esquema No Expropiativo
     */
    public Politica(String aPolitica,boolean aEsExpropiativo){
        contador++;
        this.setCodigo(contador);
        this.setPolitica(aPolitica);
        this.setEsExpropiativo(aEsExpropiativo);
        this.setYaTermino(false);
    }
    
    /**
     * Facilita la impresión de los valores de una politica
     * @return Cadena con los datos de la politica
     */
    @Override
    public String toString(){
        return ("Política: "+getCodigo()+" - "+getPolitica()+" - EsqExpr "+getEsExpropiativo()
                +" - TmpTot "+getTiempoTotal()+" - TmpEspProm "+getTiempoEsperaPromedio()
                +" - TmpRetProm "+getTiempoRetornoPromedio()+" - CantHij "+getCantidadHijos()
                +" - YaTerm "+getYaTermino());
    }
}
