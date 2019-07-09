/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import java.util.*;
import utilitarios.*;

/**
 * 
 * @author EGARAMENDI
 */
public class RecursoHardware {
    private static int contador=0;//Para autogenerar los códigos de los recursos hardware
    private int codigo;//Código identificador del recurso hardware
    private String nombreRecurso;//Nombre descriptor del recurso hardware
    private int TiempoRequerimientoMinimo;//Tiempo mínimo que se usará el recurso IO por proceso IO
    private int TiempoRequerimientoMaximo;//Tiempo máximo que se usará el recurso IO por proceso IO
    private int ProbabilidadExito;//Probabildad que un proceso requiera el uso del recurso IO
    private boolean estaLibre;// true: está libre - false: está ocupado

    public static int getContador() {
        return contador;
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int aCodigo) {
        this.codigo = aCodigo;
    }
   
    public String getNombreRecurso() {
        return nombreRecurso;
    }
    public void setNombreRecurso(String aNombreRecurso) {
        this.nombreRecurso = aNombreRecurso;
    }
    
    public int getTiempoRequerimientoMinimo() {
        return TiempoRequerimientoMinimo;
    }
    public void setTiempoRequerimientoMinimo(int aTiempoRequerimientoMinimo){
        this.TiempoRequerimientoMinimo=aTiempoRequerimientoMinimo;
    }
    
    public int getTiempoRequerimientoMaximo() {
        return TiempoRequerimientoMaximo;
    }
    public void setTiempoRequerimientoMaximo(int aTiempoRequerimientoMaximo){
        this.TiempoRequerimientoMaximo=aTiempoRequerimientoMaximo;
    }

    public int getProbabilidadExito(){
        return ProbabilidadExito;
    }
    public void setProbabilidadExito(int aProbabilidadExito){
        this.ProbabilidadExito = aProbabilidadExito;
    }
    
    public boolean getEstaLibre() {
        return estaLibre;
    }
    public void setEstaLibre(boolean aEstaLibre) {
        this.estaLibre = aEstaLibre;
    }
    
    /**
     * Método 1 de creación de un objeto recurso hardware
     * @param aNombreRecurso nombre descriptor del recurso hardware
     */
    public RecursoHardware(String aNombreRecurso){
        contador++;
        this.setCodigo(contador);
        this.setNombreRecurso(aNombreRecurso);
        this.setEstaLibre(true);
    }
    
    /**
     * Método 2 de creación de un objeto recurso hardware
     * @param aNombreRecurso nombre descriptor del recurso hardware
     * @param aTiempoRequerimientoMinimo tiempo mínimo que se usará el recurso IO por proceso IO
     * @param aTiempoRequerimientoMaximo tiempo máximo que se usará el recurso IO por proceso IO
     * @param aProbabilidadExito probabildad que un proceso requiera el uso del recurso IO
     */
    public RecursoHardware(String aNombreRecurso,int aTiempoRequerimientoMinimo,
            int aTiempoRequerimientoMaximo,int aProbabilidadExito){
        contador++;
        this.setCodigo(contador);
        this.setNombreRecurso(aNombreRecurso);
        this.setTiempoRequerimientoMinimo(aTiempoRequerimientoMinimo);
        this.setTiempoRequerimientoMaximo(aTiempoRequerimientoMaximo);
        this.setProbabilidadExito(aProbabilidadExito);
        this.setEstaLibre(true);
    }
    
    /**
     * Facilita la impresión de los valores de un recurso hardware
     * @return Cadena con los datos del recurso hardware
     */
    @Override
    public String toString(){
        return ("Recurso: "+getCodigo()+" - "+getNombreRecurso()+" - Está libre: "+getEstaLibre());
    }
}
