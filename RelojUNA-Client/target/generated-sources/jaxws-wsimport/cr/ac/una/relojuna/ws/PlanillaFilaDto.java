
package cr.ac.una.relojuna.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para planillaFilaDto complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="planillaFilaDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="folioEmpleado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nombreEmpleado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="horasOrdinarias" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="horasExtras" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="horasDobles" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="salarioMensual" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "planillaFilaDto", propOrder = {
    "folioEmpleado",
    "nombreEmpleado",
    "horasOrdinarias",
    "horasExtras",
    "horasDobles",
    "salarioMensual"
})
public class PlanillaFilaDto {

    protected String folioEmpleado;
    protected String nombreEmpleado;
    protected Double horasOrdinarias;
    protected Double horasExtras;
    protected Double horasDobles;
    protected Double salarioMensual;

    /**
     * Obtiene el valor de la propiedad folioEmpleado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolioEmpleado() {
        return folioEmpleado;
    }

    /**
     * Define el valor de la propiedad folioEmpleado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolioEmpleado(String value) {
        this.folioEmpleado = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreEmpleado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    /**
     * Define el valor de la propiedad nombreEmpleado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreEmpleado(String value) {
        this.nombreEmpleado = value;
    }

    /**
     * Obtiene el valor de la propiedad horasOrdinarias.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHorasOrdinarias() {
        return horasOrdinarias;
    }

    /**
     * Define el valor de la propiedad horasOrdinarias.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHorasOrdinarias(Double value) {
        this.horasOrdinarias = value;
    }

    /**
     * Obtiene el valor de la propiedad horasExtras.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHorasExtras() {
        return horasExtras;
    }

    /**
     * Define el valor de la propiedad horasExtras.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHorasExtras(Double value) {
        this.horasExtras = value;
    }

    /**
     * Obtiene el valor de la propiedad horasDobles.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHorasDobles() {
        return horasDobles;
    }

    /**
     * Define el valor de la propiedad horasDobles.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHorasDobles(Double value) {
        this.horasDobles = value;
    }

    /**
     * Obtiene el valor de la propiedad salarioMensual.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSalarioMensual() {
        return salarioMensual;
    }

    /**
     * Define el valor de la propiedad salarioMensual.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSalarioMensual(Double value) {
        this.salarioMensual = value;
    }

}
