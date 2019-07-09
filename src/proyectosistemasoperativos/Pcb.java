/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

/**
 * Program Counter Block, clase que almacenará toda la información referente a un
 * proceso. Para efectos de la simulación Pcb equivale proceso.
 * @author EGARAMENDI
 */
public class Pcb {
    private static int contador=0;//Para autogenerar los códigos de los procesos
    private int codigo;//Código identificador del proceso
    private int burstTime;//Burst time del proceso
    private int tiempoLlegada;//Tiempo de llegada a cola de listos
    private int tiempoLlegadaCrea;//Tiempo en el que se admitió el proceso en el sistema
    private int tiempoLlegadaEvento;//Tiempo en el que finalizó un evento interruptor del proceso
    private int rafagasConsecutivas;//Contabilizador de ráfagas consecutivas - para el algoritmo "RR"
    private int tiempoEspera;//Tiempo total que el proceso pasó en la cola de listos (Estado "Listo")
    private int tiempoTermino;//Tiempo en el que terminó el proceso
    private int tiempoRetorno;//Intervalo de tiempo desde que se admitió el proceso hasta su término
    private int tiempoBloqueado;//Tiempo total que el proceso estuvo en estado "Bloqueado"
    private int tiempoOcurrioBloqueo;//Tiempo en el que ocurrió un evento bloqueante 
    private int prioridad;//Campo a utilizar en el algoritmo propuesto por el grupo
    private int codigoPcbParentezco;//Código que referencia a su proceso padre - Solo aplicable a "procesos hijos"
    private String tipoParentezco;//Única relación: "Padre" - Solo aplicable a "procesos hijos"
    private String estado;// "Listo" - "Ejecutándose" - "Bloqueado" - "Terminado"

    public int getContadorPcb(){
        return contador;
    }
    public static void setContador(int aContador){
        contador=aContador;
    }
    
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int aCodigoPcb) {
        this.codigo = aCodigoPcb;
    }

    public int getBurstTime() {
        return burstTime;
    }
    public void setBurstTime(int aBurstTime) {
        this.burstTime = aBurstTime;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }
    /**
     * Asigna el tiempo en que el proceso llegó (o volvió) a cola de listos.
     * @param aTiempoLlegadaCrea Tiempo en el que se admitió el proceso en el sistema
     * @param aTiempoLlegadaEvento Tiempo en el que finalizó un evento interruptor del proceso.
     */
    private void setTiempoLlegada(int aTiempoLlegadaCrea,int aTiempoLlegadaEvento) {
        this.tiempoLlegada = Math.max(aTiempoLlegadaCrea, aTiempoLlegadaEvento);
    }
    
    public int getTiempoLlegadaCrea() {
        return tiempoLlegadaCrea;
    }
    public void setTiempoLlegadaCrea(int aTiempoLlegadaCrea) {
        this.tiempoLlegadaCrea = aTiempoLlegadaCrea;
    }
    
    public int getTiempoLlegadaEvento() {
        return tiempoLlegadaEvento;
    }
    
    /**
     * Tiempo que se le asigna a un proceso cuando es expropiado del procesador en el algoritmo "RR" 
     * o cuando el proceso vuelve a estado "Listo" después de que se finalizó su evento bloqueante
     * @param aTiempoLlegadaEvento tiempo
     */
    public void setTiempoLlegadaEvento(int aTiempoLlegadaEvento) {
        this.tiempoLlegadaEvento = aTiempoLlegadaEvento;
        this.setTiempoLlegada(getTiempoLlegadaCrea(), aTiempoLlegadaEvento);
        //this.setTiempoBloqueado();
    }

    public int getRafagasConsecutivas() {
        return rafagasConsecutivas;
    }
    public void setRafagasConsecutivas(int aRafagasConsecutivas) {
        this.rafagasConsecutivas = aRafagasConsecutivas;
    }
    
    public int getTiempoEspera(){
        return this.tiempoEspera;
    }
    /**
     * Acumula el tiempo total que el proceso estuvo en la cola de listos.
     * @param aTiempoAsignaProcesador tiempo en el que el proceso se apropia del procesador
     */
    public void setTiempoEspera(int aTiempoAsignaProcesador){
        int intervaloTiempoEspera=aTiempoAsignaProcesador-getTiempoLlegada();
        this.tiempoEspera = getTiempoEspera()+intervaloTiempoEspera;
    }
    
    public int getTiempoTermino(){
        return this.tiempoTermino;
    }
    public void setTiempoTermino(int aTiempoTermino){
        this.tiempoTermino=aTiempoTermino;
        setTiempoRetorno(aTiempoTermino,getTiempoLlegadaCrea());
    }
    
    public int getTiempoRetorno(){
        return this.tiempoRetorno;
    }
    public void setTiempoRetorno(int aTiempoTermino, int aTiempoLlegadaCrea){
        this.tiempoRetorno=aTiempoTermino-aTiempoLlegadaCrea;
    }
     
    public int getTiempoBloqueado(){
        return this.tiempoBloqueado;
    }
    /**
     * Acumula el tiempo total que el proceso estuvo en estado "Bloqueado";es decir, los
     * intervalos desde que ocurrió el evento bloqueante hasta volver a cola de listos.
     */
    public void setTiempoBloqueado(){
        int intervaloBloqueado=getTiempoLlegadaEvento()-getTiempoOcurrioBloqueo();
        this.tiempoBloqueado=getTiempoBloqueado()+(intervaloBloqueado);
    }
    
    public int getTiempoOcurrioBloqueo(){
        return this.tiempoOcurrioBloqueo;
    }
    public void setTiempoOcurrioBloqueo(int aTiempoOcurrioBloqueo){
        this.tiempoOcurrioBloqueo=aTiempoOcurrioBloqueo;
    }
    
    public int getPrioridad(){
        return this.prioridad;
    }
    public void setPrioridad(int aPrioridad){
        this.prioridad=aPrioridad;
    }
    
    public int getCodigoPcbParentezco() {
        return codigoPcbParentezco;
    }
    public void setCodigoPcbParentezco(int aCodigoPcbParentezco) {
        this.codigoPcbParentezco = aCodigoPcbParentezco;
    }

    public String getTipoParentezco() {
        return tipoParentezco;
    }
    public void setTipoParentezco(String aTipoParentezco) {
        this.tipoParentezco = aTipoParentezco;
    }
    
    public String getEstado() {
        return estado;
    }
    public void setEstado(String aEstado) {
        this.estado = aEstado;
    }
    
    /**
     * Método básico de creación de un objeto Pcb (de un proceso). Asigna el código
     * identificador de manera automática 
     */
    public Pcb(){
        contador++;
        this.setCodigo(contador);
    }
    
    /**
     * Asigna valores a un proceso "padre"
     * @param aBurstTime burst time del proceso
     * @param aTiempoLlegadaCrea tiempo en el que se adminitirá el proceso en el sistema
     */
    public void AsignarValoresPadre(int aBurstTime, int aTiempoLlegadaCrea){
        this.setBurstTime(aBurstTime);
        this.setTiempoLlegadaCrea(aTiempoLlegadaCrea);
        // SUPUESTO todo programa creado tendrá estado inicial Listo
        this.setTiempoLlegada(aTiempoLlegadaCrea, 0);//0: ya que aún no ha tenido evento interurptor
        this.setEstado("Listo");
    }    
    
    /**
     * Asigna valores a un proceso "hijo"
     * @param aCodigoPcbPadre codigo de su "padre"
     * @param aBurstTime burst time del proceso "hijo"
     * @param aTiempoLlegadaCrea tiempo en el que se adminitirá el proceso en el sistema
     */
    public void AsignarValoresHijo(int aCodigoPcbPadre, int aBurstTime,int aTiempoLlegadaCrea){
        this.setBurstTime(aBurstTime);
        this.setTiempoLlegadaCrea(aTiempoLlegadaCrea);
        this.setTiempoLlegada(aTiempoLlegadaCrea,0);//0: ya que aún no ha tenido evento interurptor
        this.setCodigoPcbParentezco(aCodigoPcbPadre);
        // SUPUESTO solo se crearán dos generaciones de procesos
        this.setTipoParentezco("Padre");
        // SUPUESTO todo programa creado tendrá estado inicial Listo
        this.setEstado("Listo");
    }
    
    /**
     * Facilita la impresión de los valores de un objeto Pcb
     * @return Cadena con los datos del proceso
     */
    @Override
    public String toString(){
        return ("Proceso: Cod "+getCodigo()+" - Bt "+getBurstTime()+" - TmpLl "+getTiempoLlegada()
                +" - TmpCreo "+getTiempoLlegadaCrea()+" - TmpAcbEvtInt "+getTiempoLlegadaEvento()
                +" - RafCsc "+getRafagasConsecutivas()+" - TmpEsp "+getTiempoEspera()
                +" - TmpTer "+getTiempoTermino()+" - TmpRet "+getTiempoRetorno()
                +" - TmpBlo "+getTiempoBloqueado()+" - TmpEvtInt "+getTiempoOcurrioBloqueo()
                +" - Prioridad "+getPrioridad()+" - CodPadre "+getCodigoPcbParentezco()
                +" - Rel "+getTipoParentezco()+" - Est "+getEstado());
    } 
}
