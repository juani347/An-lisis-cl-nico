package Modelo;

import Controlador.Comandos;

import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Iterator;

public class Almacen
{
    //private ObjectInputStream input;
    private ArrayList<Analisis> analisis = new ArrayList<Analisis>();
    private String nombre;

    public Almacen(){}
    
    public Almacen(String nombre)
    {
        this.nombre = nombre;
    }

    public void agregarAnalisis(Analisis analisis) throws Exception
    {
        if (!this.analisis.contains(analisis))
            this.analisis.add(analisis);
        else
            throw new Exception("Error007");
    }

    public void eliminarAnalisis(String id) throws Exception
    {
        int i = 0;
        while (i < this.analisis.size() && this.analisis.get(i).getId().equalsIgnoreCase(id))
            i++;
        if (i < this.analisis.size())
            this.analisis.remove(i);
        else
            throw new Exception("Error005");
    }

    public ArrayList<String> consulta(String op1, String op2, String op3, String op5) throws Exception
    {
        op1=op1.replace(" ","");
        op2=op2.replace(" ","");
        op3=op3.replace(" ","");
        op5=op5.replace(" ","");
        ArrayList<String> estudios = new ArrayList<String>();
        Analisis aux;
        int i = 0;
        boolean ok = false;
        double valor;
        while (i < this.analisis.size() && !ok)
        {
            if (analisis.get(i).getEstudios().containsKey(op1))
                ok = true;
            i++;
        }
        if (!ok)
            throw new Exception("Error005");
        else
        {
            try
            {
                valor = Double.parseDouble(op3);
            }
            catch (NumberFormatException e)
            {
                throw new Exception("Error002");
            }
            if (!(op2.equals("==") || op2.equals("!=") || op2.equals(">") || op2.equals("<") || op2.equals(">=") ||
                  op2.equals("<=")))
                throw new Exception("Error002");
            Iterator<Analisis> it = this.getIterator();
            while (it.hasNext())
            {
                aux = it.next();
                if (aux.getEstudios().containsKey(op1))
                    switch (op2)
                    {
                        case "==":
                            if (aux.getEstudios().get(op1) == valor)
                                estudios.add(aux.informacion());
                            break;
                        case "!=":
                            if (aux.getEstudios().get(op1) != valor)
                                estudios.add(aux.informacion());
                            break;
                        case ">":
                            if (aux.getEstudios().get(op1) > valor)
                                estudios.add(aux.informacion());
                            break;
                        case "<":
                            if (aux.getEstudios().get(op1) < valor)
                                estudios.add(aux.informacion());
                            break;
                        case ">=":
                            if (aux.getEstudios().get(op1) >= valor)
                                estudios.add(aux.informacion());
                            break;
                        case "<=":
                            if (aux.getEstudios().get(op1) <= valor)
                                estudios.add(aux.informacion());
                            break;
                    }
            }
        }
        if (!op5.equals(""))
        {
            FileWriter toFile= new FileWriter(Comandos.getName()+op5+".txt"); 
            Iterator<String> it=estudios.iterator();
            while(it.hasNext())
                toFile.write(it.next()+"\n");
            toFile.close();
        }
        return estudios;
    }

    public void setAnalisis(ArrayList<Analisis> analisis)
    {
        this.analisis = analisis;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getNombre()
    {
        return nombre;
    }

    public Iterator<Analisis> getIterator()
    {
        return this.analisis.iterator();
    }

    public ArrayList<Analisis> getAnalisis()
    {
        return analisis;
    }
}
