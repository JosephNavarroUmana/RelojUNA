
package cr.ac.una.relojuna.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para guardarPlanilla complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="guardarPlanilla"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="planilla" type="{http://ws.relojservidor.una.ac.cr/}planillaDto" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guardarPlanilla", propOrder = {
    "planilla"
})
public class GuardarPlanilla {

    protected PlanillaDto planilla;

    /**
     * Obtiene el valor de la propiedad planilla.
     * 
     * @return
     *     possible object is
     *     {@link PlanillaDto }
     *     
     */
    public PlanillaDto getPlanilla() {
        return planilla;
    }

    /**
     * Define el valor de la propiedad planilla.
     * 
     * @param value
     *     allowed object is
     *     {@link PlanillaDto }
     *     
     */
    public void setPlanilla(PlanillaDto value) {
        this.planilla = value;
    }

}
