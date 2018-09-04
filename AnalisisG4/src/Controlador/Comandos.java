package Controlador;

import Modelo.Almacen;
import Modelo.Analisis;
import Modelo.ParserXML;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Comandos extends Observable
{
    private static final String CREAR="crear";
    private static final String CARGAR="cargar";
    private static final String GUARDAR="guardar";
    private static final String INSERTAR="insertar";
    private static final String ELIMINAR="eliminar";
    private static final String CONSULTAR="consultar";
    private static final String name = "Dato\\";
    private String nombreXML="";
    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private Almacen almacen;
    private String resultado; 

    public Comandos(){}

    public String darResultados(ArrayList<String> estudios)
    {
        String res="";
        Iterator it=estudios.iterator();
        while(it.hasNext())
        {
            String aux=(String)it.next();
            res+=aux+"\n";
        }
        return res;
    }
    
    public void queComando(String comand) throws Exception
    {
        int i = 0, k;
        String aux = "", com2 = "", op1 = "", op2 = "", op3 = "", op5 = "", aux1 = "";
        ArrayList<String> com, estudios;
        String palabras[] = this.getPalabras(comand);
        com = this.mejorarPalabra(palabras);
        Iterator<String> it = com.iterator();
        System.out.println();
        while (it.hasNext())
        {
            String aux2 = it.next();
            System.out.print(aux2 + " ");
        }
        System.out.println();
        if (com.get(0).equalsIgnoreCase(GUARDAR))
            if (this.almacen != null)
                this.guardoAlmacen();
            else
                throw new Exception("Error006");
        else
        {
            if (com.get(0).equalsIgnoreCase(CONSULTAR))
            {
                if (this.almacen != null)
                    if (this.verifica(comand))
                    {
                        k = 1;
                        i = 1;
                        while (i < com.size() && k <= 5)
                        {
                            switch (k)
                            {
                                case 1: op1 = com.get(i);
                                break;
                                case 2: op2 = com.get(i);
                                break;
                                case 3: op3 = com.get(i);
                                break;
                                case 5: op5 = com.get(i);
                                break;
                            }
                            k++;
                            i++;
                        }
                        if ((com.size() == 6 && !com.get(4).equalsIgnoreCase("toFile")) || !(com.size() == 4 || com.size() == 6))
                            throw new Exception("Error002");
                        else
                            estudios = this.almacen.consulta(op1, op2, op3, op5);
                        this.resultado = this.darResultados(estudios);
                        this.setChanged();
                        this.notifyObservers(Controlador.ACTUALIZARRES);
                    } else
                        throw new Exception("Error000");
                else
                    throw new Exception("Error006");

            } else
            {
                if (com.get(0).equalsIgnoreCase(CREAR))
                    if (com.size() != 2 || !this.verifica(comand))
                        throw new Exception("Error000");
                    else
                        this.almacen = new Almacen(com.get(1));
                else 
                    if (com.get(0).equalsIgnoreCase(CARGAR))
                        if (com.size() != 2 || !this.verifica(comand))
                            throw new Exception("Error000");
                        else
                            this.cargoAlmacen(com.get(1));
                    else 
                        if(com.get(0).equalsIgnoreCase(INSERTAR))//Este es diferente al resto
                        //ver el tema de verificar ya que adentro hay mas < y >
                            if (this.almacen != null)
                                if (com.size() != 2 || !this.verifica(comand))
                                    throw new Exception("Error000");
                                else
                                {
                                    //this.almacen.agregarAnalisis(com.get(1));
                                    this.almacen.agregarAnalisis(this.generoAnalisis(this.xmlToJava(com.get(1))));   
                                }
                            else
                                throw new Exception("Error006");
                        else
                            if (aux.equalsIgnoreCase(ELIMINAR))
                                if (com.size() != 2 || !this.verifica(comand))
                                    throw new Exception("Error000");
                            else
                                if (this.almacen != null)
                                    this.almacen.eliminarAnalisis(com.get(1));
                                else
                                    throw new Exception("Error006");
                            else
                                throw new Exception("Error004");

            }
        }
    }
    
    public boolean verifica(String s)
    {
        boolean respuesta = false;
        String[] res = s.split(" ");
        if ((res.length > 1) && (res[1].charAt(0) == '<') &&
            (res[res.length - 1].charAt(res[res.length - 1].length() - 1) == '>'))
            respuesta = true;
        return respuesta;
    }

    public void setAlamacen(Almacen alamacen)
    {
        this.almacen = alamacen;
    }

    public Almacen getAlamacen()
    {
        return almacen;
    }
    
    public void abrirIn(String ruta) throws IOException
    {
        fileIn=new FileInputStream(name+ruta+".xml");
    }
    
    public void abrirOut(String ruta) throws IOException
    {
        fileOut=new FileOutputStream(name+ruta+".xml");
    }
    
    public void cargoAlmacen(String ruta) throws Exception
    {
        System.out.println("entro al metodo almacen");
        fileIn=null;
        try
        {
            abrirIn(ruta);
        } catch (IOException e){}
        this.nombreXML=ruta;
        if(fileIn!=null)
        {
            XMLDecoder decoder=new XMLDecoder(new BufferedInputStream(fileIn));
            this.almacen=(Almacen)decoder.readObject();   
            decoder.close();
        }
        else
           throw new Exception("Error003");
        System.out.println("Cargo exitosamente");
    }
    
    public void guardoAlmacen() throws Exception
    {
        if(this.almacen!=null)
        {
            abrirOut(this.almacen.getNombre());
            XMLEncoder encoder=new XMLEncoder(new BufferedOutputStream(fileOut));
            encoder.writeObject(this.almacen);
            this.almacen=null;
            encoder.close();   
        }
        else
            throw new Exception("Error006");
    }

    public String getResultado()
    {
        return resultado;
    }

    public void setNombreXML(String nombreXML)
    {
        this.nombreXML = nombreXML;
    }

    public String getNombreXML()
    {
        return nombreXML;
    }
    public ArrayList<String> mejorarPalabra(String[] s)
    {
        int j = 0;
        for (int i = 0; s.length > i; i++)
        {
            if (!s[i].equalsIgnoreCase(""))
            {
                j++;
            }
        }
        ArrayList<String> palabras = new ArrayList<String>();
        j = 0;
        for (int i = 0; s.length > i; i++)
        {
            if (!s[i].equalsIgnoreCase(""))
            {
                palabras.add(s[i]);
            }
        }
        if(!palabras.get(0).equalsIgnoreCase("INSERTAR")){
        if (palabras.size() > 1)
            palabras.set(1, palabras.get(1).replace("<",""));
        if (palabras.size() > 1)
            palabras.set(palabras.size() - 1, palabras.get(palabras.size() - 1).replace(">",""));
        }
        else
            {
             String palabraAux="";
            for(j=0;j<(palabras.get(1).length()-2);j++)
                palabraAux+=palabras.get(1).charAt(j+1);
            palabras.remove(1);
                palabras.add(palabraAux);
            }
        return palabras;                                                   
    }
    
    public String[] getPalabras(String string)
    {
    String res[];
    res = string.split(" ");
    return res;
    }
    
    public ArrayList<String> SepararCodigo(String string)
    {
    String res[];
    res = string.split("\n");
    ArrayList<String>listadoCodigo=new ArrayList<String>();
    for(int i=0;res.length>i;i++){
        listadoCodigo.add(res[i]);
    }
    return listadoCodigo;
    }
    
    public static String getName()
    {
        return name;
    }
    
    public String[][] xmlToJava(String nombre)
    {
        BufferedReader br=null;
        try
        {
            br =new BufferedReader(new FileReader(name+nombre+".txt"));
        } catch (FileNotFoundException e){}
        String LineaXML ="<Analisis>" +
                              "<Campo>" +
                                  "<Nombre>id</Nombre>" +
                                  "<Tipo>String</Tipo>" +
                                  "<Valor>0001</Valor>" +
                              "</Campo>" +
                              "<Campo>" +
                                  "<Nombre>fecha</Nombre>" +
                                  "<Tipo>String</Tipo>" +
                                  "<Valor>01-01-1990</Valor>" +
                              "</Campo>" +
                              "<Campo>" +
                                  "<Nombre>apNombre</Nombre>" +
                                  "<Tipo>String</Tipo>" +
                                  "<Valor>Juan Perez</Valor>" +
                              "</Campo>" +
                              "<Campo>" +
                                  "<Nombre>domicilio</Nombre>" +
                                  "<Tipo>String</Tipo>" +
                                  "<Valor>Reconquista 5004</Valor>" +
                              "</Campo>" +
                              "<Campo>" +
                                  "<Nombre>medico</Nombre>" +
                                  "<Tipo>String</Tipo>" +
                                  "<Valor>Pilar</Valor>" +
                              "</Campo>" + 
                              "<Campo>" +
                                  "<Nombre>estudios</Nombre>" +
                                  "<Tipo>ArrayList</Tipo>" +
                                  "<Value>2</Value>" +
                                      "<Valores_ArrayList>" +
                                          "<estudios>" +
                                              "<Campo>" +
                                                  "<Nombre>nombreEstudio</Nombre>" +
                                                  "<Tipo>String</Tipo>" +
                                                  "<Valores>Colesterol</Valores>" +
                                              "</Campo>" +
                                              "<Campo>" +
                                                  "<Nombre>valor</Nombre>" +
                                                  "<Tipo>Double</Tipo>" +
                                                  "<Valor>609.5</Valor>" +
                                              "</Campo>" +
                                          "</estudios>" +
                                          "<estudios>" +
                                              "<Campo>" +
                                                  "<Nombre>nombreEstudio</Nombre>" +
                                                  "<Tipo>String</Tipo>" +
                                                  "<Valores>ElectroCardiograma</Valores>" +
                                              "</Campo>" +
                                              "<Campo>" +
                                                  "<Nombre>valor</Nombre>" +
                                                  "<Tipo>Double</Tipo>" +
                                                  "<Valor>345678.5</Valor>" +
                                              "</Campo>" +
                                          "</estudios>" +
                                      "</Valores_ArrayList>" +
                                  "</Campo>" +
                              "</Analisis>";
        //String line = null;
        /*StringBuilder sb = new StringBuilder();
        try
        {
            while ((LineaXML = br.readLine()) != null)
            {
                sb.append(LineaXML);
                //sb.append("\n");
            }
        } catch (IOException e){}
        String text = sb.toString();
        System.out.println(text);*/
        ParserXML p=new ParserXML();
        String[][] Lista=null;
        try
        {
            Lista = p.leoArgumento(LineaXML);
        } catch (IOException | ParserConfigurationException | SAXException e){}
        /* for (int i=0; i<Lista.length; i++)
        {
              System.out.print(Lista[i][0]);
              System.out.print("/ ");
              System.out.print(Lista[i][1]);
              System.out.print("/ ");
              System.out.println(Lista[i][2]);
            }
        } */
        return Lista;
    }
    
    public Analisis generoAnalisis(String[][] analisis)
    {
        String nombre="";
        double valor;
        Analisis ana=new Analisis();
        int i,j=0;
        for(i=0;i<6;i++)
        {
            switch(i)
            {
                case 0: ana.setId(analisis[i][2]);
                break;
                case 1: ana.setFecha(analisis[i][2]);
                break;
                case 2: ana.setApNombre(analisis[i][2]);
                break;
                case 3: ana.setDomicilio(analisis[i][2]);
                break;
                case 4: ana.setMedico(analisis[i][2]);
                break;
                case 5: j=(Integer.parseInt(analisis[i][2]))*2;
                break;
            }
        }
        i+=j;
        for(j=6;j<i;j++)
        if(j%2==0)
            nombre=analisis[j][2];
        else
        {
            valor=Double.parseDouble(analisis[j][2]);
            ana.getEstudios().put(nombre,valor);
        }
        return ana;
    }
}

