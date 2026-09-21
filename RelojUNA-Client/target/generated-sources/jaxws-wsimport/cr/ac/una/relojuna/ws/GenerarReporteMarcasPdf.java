
package cr.ac.una.relojuna.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para generarReporteMarcasPdf complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="generarReporteMarcasPdf"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="filas" type="{http://ws.relojservidor.una.ac.cr/}listaConsultaFilaDto" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generarReporteMarcasPdf", propOrder = {
    "filas"
})
public class GenerarReporteMarcasPdf {

    protected ListaConsultaFilaDto filas;

    /**
     * Obtiene el valor de la propiedad filas.
     * 
     * @return
     *     possible object is
     *     {@link ListaConsultaFilaDto }
     *     
     */
    public ListaConsultaFilaDto getFilas() {
        return filas;
    }

    /**
     * Define el valor de la propiedad filas.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaConsultaFilaDto }
     *     
     */
    public void setFilas(ListaConsultaFilaDto value) {
        this.filas = value;
    }

}
