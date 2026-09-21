
package cr.ac.una.relojuna.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para detallePlanillaDto complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="detallePlanillaDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="empleadoId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="horasDobles" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="horasExtras" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="horasOrdinarias" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="planillaId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="salarioTotal" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detallePlanillaDto", propOrder = {
    "empleadoId",
    "horasDobles",
    "horasExtras",
    "horasOrdinarias",
    "id",
    "planillaId",
    "salarioTotal"
})
public class DetallePlanillaDto {

    protected Long empleadoId;
    protected Integer horasDobles;
    protected Integer horasExtras;
    protected Integer horasOrdinarias;
    protected Long id;
    protected Long planillaId;
    protected Double salarioTotal;

    /**
     * Obtiene el valor de la propiedad empleadoId.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEmpleadoId() {
        return empleadoId;
    }

    /**
     * Define el valor de la propiedad empleadoId.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEmpleadoId(Long value) {
        this.empleadoId = value;
    }

    /**
     * Obtiene el valor de la propiedad horasDobles.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHorasDobles() {
        return horasDobles;
    }

    /**
     * Define el valor de la propiedad horasDobles.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHorasDobles(Integer value) {
        this.horasDobles = value;
    }

    /**
     * Obtiene el valor de la propiedad horasExtras.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHorasExtras() {
        return horasExtras;
    }

    /**
     * Define el valor de la propiedad horasExtras.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHorasExtras(Integer value) {
        this.horasExtras = value;
    }

    /**
     * Obtiene el valor de la propiedad horasOrdinarias.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHorasOrdinarias() {
        return horasOrdinarias;
    }

    /**
     * Define el valor de la propiedad horasOrdinarias.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHorasOrdinarias(Integer value) {
        this.horasOrdinarias = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad planillaId.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPlanillaId() {
        return planillaId;
    }

    /**
     * Define el valor de la propiedad planillaId.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPlanillaId(Long value) {
        this.planillaId = value;
    }

    /**
     * Obtiene el valor de la propiedad salarioTotal.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSalarioTotal() {
        return salarioTotal;
    }

    /**
     * Define el valor de la propiedad salarioTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSalarioTotal(Double value) {
        this.salarioTotal = value;
    }

}
