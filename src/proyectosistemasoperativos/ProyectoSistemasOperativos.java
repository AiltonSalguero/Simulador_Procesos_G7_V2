/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosistemasoperativos;

import utilitarios.*;
import java.util.*;

/**
 *
 * @author EGARAMENDI
 */
public class ProyectoSistemasOperativos {

    //Lista para almacenar procesos hijos durante la simulación (lista)
    public static ArrayList<Pcb> procesosHijos = new ArrayList<>();
    //Lista para almacenar los procesos del sistema - visualizar los estados de los procesos (lista)
    public static ArrayList<Pcb> procesosSistema = new ArrayList<>();
    //Lista para almacenar los procesos nuevos durante la simulación (lista temporal)
    public static ArrayList<Pcb> procesosNuevos = new ArrayList<>();
    //Lista para almacenar los procesos en cola de listos - estado: "Listo" (cola)
    public static ArrayList<Pcb> procesosListos = new ArrayList<>();
    //Lista para almacenar los procesos bloqueados - estado: "Bloqueado" (cola)
    public static ArrayList<Pcb> procesosBloqueados = new ArrayList<>();
    //Lista para almacenar los procesos terminados - estado: "Terminado" (lista)
    public static ArrayList<Pcb> procesosTerminados = new ArrayList<>();
    //ArrayList de un elemento (un procesador): almacena el proceso en ejecución - estado: "Ejecutándose"
    public static ArrayList<Pcb> procesoEjecutandose = new ArrayList<>();

    // Lista1: para almacenar los valores originales de creación de los procesos padres
    public static ArrayList<Pcb> procesosPadresOriginal = new ArrayList<>();
    // Lista2: la cual será trabajada (modificada) en cada política
    public static ArrayList<Pcb> procesosPadres = new ArrayList<>();

    //Parámetros para generación de procesos de usuario (Procesos padre)
    public static int cantidadProcesos = 5; //Cantidad de procesos padres (de usuario) a simular
    public static int burstTimeMinimo = 50; //Cota mínima para asignar burst time a un proceso padre
    public static int burstTimeMaximo = 80; //Cota máxima para asignar burst time a un proceso padre
    public static int TiempoLlegadaMinimo = 0; //Cota mínima para asignar tiempo de llegada de un proceso padre
    public static int TiempoLlegadaMaximo = 10; //Cota máxima para asignar tiempo de llegada de un proceso padre

    //Parámetros para generación de procesos hijo
    public static int probabilidadCrearHijo = 5; //Probabilidad máxima que permite crear un proceso hijo
    public static int burstTimeMinimoHijo = 10; //Cota mínima para asignar burst time a un proceso hijo
    public static int burstTimeMaximoHijo = 15; //Cota máxima para asignar burst time a un proceso hijo

    //Parámetros para reloj de ejecución
    public static int tiempoDelayReloj = 1; //Simula el tiempo del compilador por ciclo de ejecución en el programa (unidad: ns)
    public static int tiempo = 0; //inicia tiempo de simulación en 0 (unidad: s)

    //Variable de control
    //int procesosSinTerminar=1;// La simulación terminará cuando sea 0, inidicialmente 1 para que se inicie
    public static boolean procesosSinTerminar = true;// La simulación terminará cuando sea false
    public static int cuantoRR = 20;

    //Variable de eventos
    public static boolean seCreoHijo = false;// Variable que indica si ocurre evento "crear hijo"

    //Variable de cuadre
    public static int totalBurstTimePadres = 0;// Variable para contabilizar el total de burst time de todos los padres
    public static int totalBurstTimeHijos = 0;// Variable para contabilizar el total de burst time de todos los hijos

    //Variables de retorno múltimple
    public static BooleanInt seCreoHijoTotalBurstTimeHijo = new BooleanInt();
    
    public static ArrayList<RecursoHardware> recursosHardware = new ArrayList<>();
    public static ArrayList<Politica> politicasSistema = new ArrayList<>();
    
    public static ArrayList<ArrayList<ArrayList<PcbIO>>> ListaColasIO = new ArrayList<>(3);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        iniciar();
    }

    public static void iniciar() {
        //Creación de los recursos de hardware del computador
        utilitarios.Configuracion.CrearRecursos(recursosHardware);
        //Creación de políticas del sistema
        utilitarios.Configuracion.CrearPoliticas(politicasSistema);
        //Creación de los procesos padres
        totalBurstTimePadres = utilitarios.Configuracion.CrearProcesos(cantidadProcesos, burstTimeMinimo,
                burstTimeMaximo, TiempoLlegadaMinimo, TiempoLlegadaMaximo, procesosPadresOriginal, totalBurstTimePadres);
        HerramientasUtiles.copiarListaPadres(procesosPadres, procesosPadresOriginal);

//        //Preparación de las listas manejadas por el programa: listas y colas
//        procesosHijos = new ArrayList<>();
//        procesosSistema = new ArrayList<>();
//        procesosNuevos = new ArrayList<>();
//        procesosListos = new ArrayList<>();
//        procesosBloqueados = new ArrayList<>();
//        procesosTerminados = new ArrayList<>();
//        procesoEjecutandose = new ArrayList<>();
        //Lista para almacenar las listas y colas de los dispositivos IO
        ListaColasIO = HerramientasUtiles.CrearListaDeColasIO(recursosHardware, ListaColasIO);

        // (0) INICIO DEL PROGRAMA
        // TODO Esto es removible - facilita visualizacón en ventana de comando
        System.out.println("************ INICIO ************");
        tiempo = 0; // Iniciar tiempo de la simulación en 0

        // TODO Se adiciono para cuadre de procesos
        System.out.println("tiempo de burst time de padres: " + totalBurstTimePadres);

        for (Politica politica : politicasSistema) {
            //Información de la política
            String algoritmo = politica.getPolitica();
            boolean EsquemaExpropiativo = politica.getEsExpropiativo();
            //Limipar toda la información generada por la política anterior
            tiempo = 0;
            procesosSinTerminar = true;
            int tiempoEsperaPolitica = 0;
            int tiempoRetornoPolitica = 0;
            HerramientasUtiles.LimpiarListaDeColasIO(recursosHardware, ListaColasIO);
            PcbIO.setContador(0);
            procesosPadres.clear();
            procesosHijos.clear();
            procesosSistema.clear();
            procesosNuevos.clear();
            procesosListos.clear();
            procesosBloqueados.clear();
            procesosTerminados.clear();
            procesoEjecutandose.clear();
            Pcb.setContador(0);
            totalBurstTimeHijos = 0;
            //Cargar la lista de procesos padres original, insumo único para todas las políticas
            HerramientasUtiles.copiarListaPadres(procesosPadres, procesosPadresOriginal);

            //(0) Inicio de la simulación de la política
            System.out.println("******* INICIO - Algoritmo : " + algoritmo + " - Esquema Expropiativo: " + EsquemaExpropiativo + " *******");
            System.out.println("**** Valores pa"
                    + "dres *******");
            for (Pcb proceso : procesosPadres) {
                System.out.println(proceso.toString());
            }
            // TODO Se adiciono para cuadre de procesos
            System.out.println("tiempo de burst time de padres: " + totalBurstTimePadres);

            while (procesosSinTerminar) {
                // TODO Debería ir al último 
                procesosSinTerminar = HerramientasUtiles.HayProcesosSinTerminar(procesosPadres);

                //(1) Evaluar si llegan procesos nuevos al sistema (padres y/o hijos)
                seCreoHijoTotalBurstTimeHijo = PlanificadorLargoPlazo.CapturarProcesosNuevos(procesosPadres, procesosHijos, procesosNuevos, procesosSistema, tiempo,
                        totalBurstTimeHijos, seCreoHijo);
                seCreoHijo = seCreoHijoTotalBurstTimeHijo.getSeCreoHijo();//badera de control
                totalBurstTimeHijos = seCreoHijoTotalBurstTimeHijo.getTotalBurstTimeHijos();//acumulador de cuadre

                //(2) Agregar procesos nuevo a cola de listos - TODO Capturar los procesos Listos de IO
                PlanificadorLargoPlazo.CargarProcesosListosCompleto(procesosNuevos, procesosListos);

                //(3) Ordenar según algoritmo
                PlanificadorMedioPlazo.OrdenarPocesosListos(algoritmo, EsquemaExpropiativo,
                        procesosPadres, procesosHijos, procesosListos, procesoEjecutandose, ListaColasIO,
                        recursosHardware, tiempo);

                //(4) Evaluar si se debe asignar el procesador - en esta instancia la fuente serán procesosPadres
                PlanificadorMedioPlazo.AsignarRecurso(recursosHardware, procesosListos, procesosPadres,
                        procesosHijos, procesoEjecutandose, ListaColasIO, tiempo);

                // TODO Esto es removible - facilita visualizacón, en ventana de comando, del estado del recrurso
                System.out.println("recurso: " + recursosHardware.get(0).toString());
                System.out.println("recurso: " + recursosHardware.get(1).toString());
                System.out.println("recurso: " + recursosHardware.get(2).toString());
                System.out.println("recurso: " + recursosHardware.get(3).toString());

                //(5) Ejecutar una ráfaga de los procesos en ejecución
                //(5.1) Actualizar PC del proceso en ejecución
                PlanificadorCortoPlazo.ActualizarPC(procesoEjecutandose);
                //(5.2) Actualizar PC de los procesosIO en ejecución
                PlanificadorCortoPlazo.ActualizarPCIO(recursosHardware, ListaColasIO);

                //(6) Evaluar si ocurre una interrupción
                if (!procesoEjecutandose.isEmpty()) {
                    //(6.1) Evaluar ocurrencia de interrupción por hijo
                    if (HerramientasUtiles.EsPadre(procesoEjecutandose)) {
                        //(6.1) Evalúa crear hijo, si la probabilidad es exitosa crea hijo: agrandar array ProcesosHijo
                        seCreoHijo = PlanificadorCortoPlazo.EvaluarCrearHijo(procesosHijos,
                                seCreoHijo, burstTimeMinimoHijo, burstTimeMaximoHijo, tiempo,
                                probabilidadCrearHijo, procesosPadres, recursosHardware, procesoEjecutandose,
                                procesosBloqueados);
                    }
                    //(6.1) Evaluar ocurrencia de interrupción por requerimiento de IO
                    if (!seCreoHijo) {
                        Interrupcion.EvaluarRquerimientoIO(procesosPadres, procesosHijos,
                                procesoEjecutandose, procesosBloqueados, recursosHardware, ListaColasIO, tiempo);
                    }
                }

                //(7) Evaluar terminar procesos en ejecución
                //(7.1) Evaluar terminar un proceso o expropiarlo
                PlanificadorCortoPlazo.TerminarProceso(procesosPadres, procesosHijos, procesoEjecutandose,
                        procesosListos, procesosBloqueados, procesosTerminados, recursosHardware, tiempo,
                        algoritmo, cuantoRR);
                //(7.2) Evaluar terminar los procesosIO
                PlanificadorCortoPlazo.EvaluarTerminarProcesosIO(recursosHardware, ListaColasIO, tiempo,
                        procesosPadres, procesosHijos, procesosBloqueados, procesosListos);

                //(8) Incrementar el tiempo de simulación en 1
                tiempo = HerramientasUtiles.IncrementarTiempo(tiempo, tiempoDelayReloj);

                // TODO Esto es removible
                System.out.println("*********************************************");
            }

            // --- DE AQUI EN ADELANTE SOLO ES VER LA EJECUCIÓN EN VENTANA OUTPUT
            // TODO Esto es removible - se puede crear metodo (impresión de resultados)
            System.out.println("*********************************************");
            System.out.println("Se terminó la ejecución de los procesos");
            for (Pcb proceso : procesosSistema) {
                System.out.println(proceso.toString());
            }
            System.out.println("tiempo total: " + tiempo);

            // TODO Esto es removible
            System.out.println("tiempo total (reloj): " + tiempo);

            // TODO Se adiciono para cuadre de procesos
            System.out.println("tiempo de burst time de padres: " + totalBurstTimePadres);

            // TODO Se adiciono para cuadre de procesos
            System.out.println("tiempo de burst time de hijos: " + totalBurstTimeHijos);

            /* TODO Cuadre de tiempos: se puede hacer método - se acepta resultado entre 1,2 y 3 como válidos
            (tiempo-1) : porque la simulación termina un ciclo después de ejecutarse el último proceso
            procesosPadres.get(0).getTiempoLlegadaCrea() : tiempo que llega al sistema el primer proceso
            totalBurstTimePadres : total de tiempo utilizado en los procesos padres
            totalBurstTimeHijos : total de tiempo utilzado en los procesos hijos
             */
            System.out.println("Tiempo descuadre: " + (tiempo - 1 - procesosPadres.get(0).getTiempoLlegadaCrea()
                    - totalBurstTimePadres - totalBurstTimeHijos));

            System.out.println("***************************************************");
            System.out.println("procesos nuevos");
            for (Pcb proceso : procesosNuevos) {
                System.out.println(proceso.toString());
            }
            System.out.println("***************************************************");
            System.out.println("procesos listos");
            for (Pcb proceso : procesosListos) {
                System.out.println(proceso.toString());
            }
            System.out.println("***************************************************");
            System.out.println("proceso ejecutandose");
            for (Pcb proceso : procesoEjecutandose) {
                System.out.println(proceso.toString());
            }
            System.out.println("***************************************************");
            System.out.println("procesos bloqueados");
            for (Pcb proceso : procesosBloqueados) {
                System.out.println(proceso.toString());
            }
            System.out.println("***************************************************");
            System.out.println("procesos terminados");
            for (Pcb proceso : procesosTerminados) {
                System.out.println(proceso.toString());
            }

            System.out.println("***************************************************");
            System.out.println("procesos IO atendidos");

            for (int n = 1; n <= recursosHardware.size() - 1; n++) {
                System.out.println("procesos IO del recurso :" + recursosHardware.get(n).getNombreRecurso());
                for (PcbIO procesoio : ListaColasIO.get(n - 1).get(0)) {
                    System.out.println(procesoio.toString());
                }
            }

            System.out.println("***************************************************");

            for (Pcb proceso : procesosPadres) {
                tiempoEsperaPolitica = tiempoEsperaPolitica + proceso.getTiempoEspera();
                tiempoRetornoPolitica = tiempoRetornoPolitica + proceso.getTiempoRetorno();
            }
            politica.setTiempoEsperaPromedio(tiempoEsperaPolitica / procesosPadres.size());
            politica.setTiempoRetornoPromedio(tiempoRetornoPolitica / procesosPadres.size());
            politica.setTiempoTotal(tiempo - 1);
            politica.setCantidadHijos(procesosHijos.size());
            politica.setYaTermino(true);
        }

        System.out.println("***** RESULTADOS *****");
        for (Politica politica : politicasSistema) {
            System.out.println(politica.toString());
        }
    }
}
