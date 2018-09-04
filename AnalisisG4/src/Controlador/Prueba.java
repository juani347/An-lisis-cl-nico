package Controlador;

import Modelo.Almacen;
import Modelo.Analisis;

import Vista.Ventana;

import java.util.HashMap;


public class Prueba
{
    public Prueba()
    {
        super();
    }

    public static void main(String[] args)
    {
        HashMap<String,Double> est=new HashMap<String,Double>();
        est.put("1",982.9);est.put("2",20.56);est.put("3",345.23);est.put("4",123.45);
        Analisis a1=new Analisis("5654","17/06/97","Rossi, Franco","Almafuerte 1827","Pilar Mutti",est);
        Comandos com=new Comandos();
        com.setAlamacen(new Almacen("Juani"));
        com.getAlamacen().getAnalisis().add(a1);
        com.setNombreXML("Juani.xml");
        try
        {
            com.guardoAlmacen();
        }
        catch (Exception e)
        {
        }
        Controlador c=new Controlador();
    }
}
