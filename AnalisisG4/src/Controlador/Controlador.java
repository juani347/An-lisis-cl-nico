package Controlador;

import Modelo.Almacen;

import Vista.Ventana;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class Controlador implements ActionListener,Observer
{
    public static final String ENVIAR = "ENVIAR";
    public static final String ACTUALIZARRES = "ACTUALIZARRES";
    private ArrayList<Observable> observables= new ArrayList<Observable>(); 
    private Ventana ventana = new Ventana();
    private Comandos cmd;//Deberia tener nombre en otro lado
    private ArrayList<String>comandos;
    
    public Controlador()
    {
        this.ventana.addActionListener(this);
        this.ventana.setVisible(true);
        this.cmd=new Comandos();
        this.agregarObservable(cmd);
    }
       

        public void agregarObservable(Observable o)
    {
        if (!this.observables.contains(o))
            this.observables.add(o);
        o.addObserver(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if (actionEvent.getActionCommand().equals(Controlador.ENVIAR))
        {
            System.out.println("enviar");
            this.comandos = this.cmd.SepararCodigo(this.ventana.darComandos());
            Iterator it = this.comandos.iterator();
            System.out.println();
            while (it.hasNext())
            {
                String aux2 = (String) it.next();
                System.out.print(aux2 + " ");
            }
            System.out.println();
            this.EjecutarSec();

        } //del if
    } //del actionPerformed

    @Override
    public void update(Observable observable, Object object)
    {
        if(this.observables.contains(observable))
        {
            if(Controlador.ACTUALIZARRES.equalsIgnoreCase((String)object))
                this.ventana.mostraResultados(this.cmd.getResultado());
        }
        
    }
    
    public void EjecutarSec(){
        int i=0;
        while(i<this.comandos.size()){
            try {
                this.cmd.queComando(this.comandos.get(i));
                System.out.println("Funciono");
                i++;
            } catch (Exception e) {
                this.ventana.mostraError(e.getMessage(),i,this.comandos.get(i));
                System.out.println("Salto una execption en la linea ");
                i=this.comandos.size();
            }
            
        }
    }
}//de la clase
