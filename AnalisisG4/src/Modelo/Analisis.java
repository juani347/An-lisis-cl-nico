
package Modelo;

import java.util.HashMap;

public class Analisis
{
    private String id;
    private String fecha;
    private String apNombre;
    private String domicilio;
    private String medico;
    private HashMap<String,Double> estudios=new HashMap<String,Double>();
    
    public Analisis(){}
    
    public Analisis(String id, String fecha, String apNombre, String domicilio, String medico, HashMap<String, Double> estudios)
    {
        this.id = id;
        this.fecha = fecha;
        this.apNombre=apNombre;
        this.domicilio = domicilio;
        this.medico = medico;
        this.estudios = estudios;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public void setApNombre(String apNombre)
    {
        this.apNombre = apNombre;
    }

    public void setDomicilio(String domicilio)
    {
        this.domicilio = domicilio;
    }

    public void setMedico(String medico)
    {
        this.medico = medico;
    }

    public void setEstudios(HashMap<String, Double> estudios)
    {
        this.estudios = estudios;
    }

    public double valorAtributo(String nombreEstudio) throws Exception
    {
        double res=0;
        if(this.estudios.containsKey(nombreEstudio))
            res=this.estudios.get(nombreEstudio);
        else
            throw new Exception("No se encuentra el estudio buscado\n");
        return res;
    }

    public String getId()
    {
        return id;
    }

    public String getFecha()
    {
        return fecha;
    }
    
    public String getApNombre()
    {
        return apNombre;
    }

    public String getDomicilio()
    {
        return domicilio;
    }

    public String informacion() {
        
        return (String) this.id+" "+this.fecha+" "+this.apNombre+" "+this.domicilio+" "+this.medico+" "+this.estudios;
    }

    public String getMedico()
    {
        return medico;
    }

    public HashMap<String, Double> getEstudios()
    {
        return estudios;
    }
}
