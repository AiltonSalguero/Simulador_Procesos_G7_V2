/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

/**
 * Program Counter Block de un proceso llevado a cabo por un dispositivo de entrada o
 * salida (IO) - denominado en la simulación procesoIO para diferenciarlo de un proceso,
 * ejecutado por el procesador. Clase que almacenará toda la información referente a un
 * procesoIO. Para efectos de la simulación PcbIO equivale procesoIO.
 * @author EGARAMENDI
 */
public class PcbIO {
    private static int contador=0;//Para autogenerar los códigos de los procesosIO
    private int codigoIO;//Código identificador del procesoIO
    private int codigo;//Código identificador del proceso que requirió el dispositivo IO
    private int codigoDispotivoIO;//Código identificador del dispositivo IO
    private int tiempoRequerimiento;//Tiempo que se requiere usar el dispositivo IO
    private int tiempoLlegada;//Tiempo de llegada a cola de listos del dispositivo IO
    private int tiempoLlegadaCrea;//Tiempo en el que se admitió el procesoIO en controlador IO
    private int tiempoLlegadaEvento;//Tiempo en el que finalizó un evento interruptor del procesoIO
    private String estado;// "Listo" - "Ejecutándose" - "Terminado"
    /*OBS: tiempoLlegadaCrea, tiempoLlegadaEvento - No se han implementado, Han sido considerados
    para en un futuro se pueda soportar interrupciones de evento denominados "Bloqueantes"*/

    public int getContador(){
        return contador;
    }
    public static void setContador(int aContador){
        contador=aContador;
    }
    
    public int getCodigoIO() {
        return codigoIO;
    }
    public void setCodigoIO(int aCodigoIO) {
        this.codigoIO = aCodigoIO;
    }
    
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int aCodigoPcb) {
        this.codigo = aCodigoPcb;
    }
    
    public int getCodigoDispotivoIO(){
        return codigoDispotivoIO;
    }
    public void setCodigoDispotivoIO(int aCodigoDispotivoIO){
        this.codigoDispotivoIO = aCodigoDispotivoIO;
    }

    public int getTiempoRequerimiento() {
        return tiempoRequerimiento;
    }
    public void setTiempoRequerimiento(int atiempoRequerimiento) {
        this.tiempoRequerimiento = atiempoRequerimiento;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }
    public void setTiempoLlegada(int aTiempoLlegadaCrea,int aTiempoLlegadaEvento) {
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
    //Aplicable cuando llega un proceso "Bloqueante"
    public void setTiempoLlegadaEvento(int aTiempoLlegadaEvento) {
        this.tiempoLlegadaEvento = aTiempoLlegadaEvento;
        this.setTiempoLlegada(getTiempoLlegadaCrea(), aTiempoLlegadaEvento);
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String aEstado) {
        this.estado = aEstado;
    }
    
    /**
     * Método básico de creación de un objeto PcbIO (de un procesoIO). 
     * Asigna el código identificador de manera automática 
     */
    public PcbIO(){
        contador++;
        setCodigoIO(contador);
    }
    
    /**
     * Asigna valores a un procesoIO
     * @param aCodigoPcb código identificador del proceso que requirió el dispositivo IO
     * @param aCodigoDispositivoIO código identificador del dispositivo IO
     * @param atiempoRequerimiento tiempo que se requiere usar el dispositivo IO
     * @param aTiempoLlegadaCrea tiempo
     */
    public void AsignarValoresPcbIO(int aCodigoPcb, int aCodigoDispositivoIO, int atiempoRequerimiento,
            int aTiempoLlegadaCrea){
        this.setCodigo(aCodigoPcb);
        this.setCodigoDispotivoIO(aCodigoDispositivoIO);
        this.setTiempoRequerimiento(atiempoRequerimiento);
        this.setTiempoLlegadaCrea(aTiempoLlegadaCrea);
        this.setTiempoLlegada(aTiempoLlegadaCrea, 0);
        this.setEstado("Listo");
    }
    
    /**
     * Facilita la impresión de los valores de un objeto PcbIO
     * @return Cadena con los datos del procesoIO
     */
    @Override
    public String toString(){
        return ("ProcesoIO: "+" CodPcbIO "+getCodigoIO()+" - CodPcb "+getCodigo()
                +" - CodDispIO "+getCodigoDispotivoIO()+" - TmpReq "+getTiempoRequerimiento()
                +" - TmpLl "+getTiempoLlegada()+" - Est "+getEstado()
                //+" - TmpCreo "+getTiempoLlegadaCrea()+" - TmpAcbEvtInt "+getTiempoLlegadaEvento()
                );
    }
    
    /**
     * NO SE UTILIZA, pero permitiría manejar eventos bloqueantes en la simulación
     * @param aTiempoLlegadaEvento 
     */
    public void BloquearPcbIO(int aTiempoLlegadaEvento){
        this.setTiempoLlegadaEvento(aTiempoLlegadaEvento);
        this.setTiempoLlegada(getTiempoLlegadaCrea(), aTiempoLlegadaEvento);//Posible error 
        this.setEstado("Bloqueado");
    }
}
