
package cr.ac.una.relojuna.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para consultarMarcas complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="consultarMarcas"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="desde" type="{http://ws.relojservidor.una.ac.cr/}localDate" minOccurs="0"/&gt;
 *         &lt;element name="hasta" type="{http://ws.relojservidor.una.ac.cr/}localDate" minOccurs="0"/&gt;
 *         &lt;element name="empleadoId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarMarcas", propOrder = {
    "desde",
    "hasta",
    "empleadoId"
})
public class ConsultarMarcas {

    protected LocalDate desde;
    protected LocalDate hasta;
    protected Long empleadoId;

    /**
     * Obtiene el valor de la propiedad desde.
     * 
     * @return
     *     possible object is
     *     {@link LocalDate }
     *     
     */
    public LocalDate getDesde() {
        return desde;
    }

    /**
     * Define el valor de la propiedad desde.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalDate }
     *     
     */
    public void setDesde(LocalDate value) {
        this.desde = value;
    }

    /**
     * Obtiene el valor de la propiedad hasta.
     * 
     * @return
     *     possible object is
     *     {@link LocalDate }
     *     
     */
    public LocalDate getHasta() {
        return hasta;
    }

    /**
     * Define el valor de la propiedad hasta.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalDate }
     *     
     */
    public void setHasta(LocalDate value) {
        this.hasta = value;
    }

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

}
