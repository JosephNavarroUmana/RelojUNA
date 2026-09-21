
package cr.ac.una.relojuna.ws;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cr.ac.una.relojuna.ws package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _ArchivoDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "archivoDto");
    private static final QName _ConsultaFilaDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "consultaFilaDto");
    private static final QName _EmpleadoDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "empleadoDto");
    private static final QName _ExportarConsultaExcel_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "exportarConsultaExcel");
    private static final QName _ExportarConsultaExcelResponse_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "exportarConsultaExcelResponse");
    private static final QName _ExportarPlanillaExcel_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "exportarPlanillaExcel");
    private static final QName _ExportarPlanillaExcelResponse_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "exportarPlanillaExcelResponse");
    private static final QName _GenerarReporteEmpleadosPdf_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "generarReporteEmpleadosPdf");
    private static final QName _GenerarReporteEmpleadosPdfResponse_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "generarReporteEmpleadosPdfResponse");
    private static final QName _GenerarReporteMarcasPdf_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "generarReporteMarcasPdf");
    private static final QName _GenerarReporteMarcasPdfResponse_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "generarReporteMarcasPdfResponse");
    private static final QName _ListaConsultaFilaDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "listaConsultaFilaDto");
    private static final QName _ListaEmpleadoDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "listaEmpleadoDto");
    private static final QName _ListaPlanillaFilaDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "listaPlanillaFilaDto");
    private static final QName _PlanillaFilaDto_QNAME = new QName("http://ws.relojservidor.una.ac.cr/", "planillaFilaDto");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cr.ac.una.relojuna.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArchivoDto }
     * 
     * @return
     *     the new instance of {@link ArchivoDto }
     */
    public ArchivoDto createArchivoDto() {
        return new ArchivoDto();
    }

    /**
     * Create an instance of {@link ConsultaFilaDto }
     * 
     * @return
     *     the new instance of {@link ConsultaFilaDto }
     */
    public ConsultaFilaDto createConsultaFilaDto() {
        return new ConsultaFilaDto();
    }

    /**
     * Create an instance of {@link EmpleadoDto }
     * 
     * @return
     *     the new instance of {@link EmpleadoDto }
     */
    public EmpleadoDto createEmpleadoDto() {
        return new EmpleadoDto();
    }

    /**
     * Create an instance of {@link ExportarConsultaExcel }
     * 
     * @return
     *     the new instance of {@link ExportarConsultaExcel }
     */
    public ExportarConsultaExcel createExportarConsultaExcel() {
        return new ExportarConsultaExcel();
    }

    /**
     * Create an instance of {@link ExportarConsultaExcelResponse }
     * 
     * @return
     *     the new instance of {@link ExportarConsultaExcelResponse }
     */
    public ExportarConsultaExcelResponse createExportarConsultaExcelResponse() {
        return new ExportarConsultaExcelResponse();
    }

    /**
     * Create an instance of {@link ExportarPlanillaExcel }
     * 
     * @return
     *     the new instance of {@link ExportarPlanillaExcel }
     */
    public ExportarPlanillaExcel createExportarPlanillaExcel() {
        return new ExportarPlanillaExcel();
    }

    /**
     * Create an instance of {@link ExportarPlanillaExcelResponse }
     * 
     * @return
     *     the new instance of {@link ExportarPlanillaExcelResponse }
     */
    public ExportarPlanillaExcelResponse createExportarPlanillaExcelResponse() {
        return new ExportarPlanillaExcelResponse();
    }

    /**
     * Create an instance of {@link GenerarReporteEmpleadosPdf }
     * 
     * @return
     *     the new instance of {@link GenerarReporteEmpleadosPdf }
     */
    public GenerarReporteEmpleadosPdf createGenerarReporteEmpleadosPdf() {
        return new GenerarReporteEmpleadosPdf();
    }

    /**
     * Create an instance of {@link GenerarReporteEmpleadosPdfResponse }
     * 
     * @return
     *     the new instance of {@link GenerarReporteEmpleadosPdfResponse }
     */
    public GenerarReporteEmpleadosPdfResponse createGenerarReporteEmpleadosPdfResponse() {
        return new GenerarReporteEmpleadosPdfResponse();
    }

    /**
     * Create an instance of {@link GenerarReporteMarcasPdf }
     * 
     * @return
     *     the new instance of {@link GenerarReporteMarcasPdf }
     */
    public GenerarReporteMarcasPdf createGenerarReporteMarcasPdf() {
        return new GenerarReporteMarcasPdf();
    }

    /**
     * Create an instance of {@link GenerarReporteMarcasPdfResponse }
     * 
     * @return
     *     the new instance of {@link GenerarReporteMarcasPdfResponse }
     */
    public GenerarReporteMarcasPdfResponse createGenerarReporteMarcasPdfResponse() {
        return new GenerarReporteMarcasPdfResponse();
    }

    /**
     * Create an instance of {@link ListaConsultaFilaDto }
     * 
     * @return
     *     the new instance of {@link ListaConsultaFilaDto }
     */
    public ListaConsultaFilaDto createListaConsultaFilaDto() {
        return new ListaConsultaFilaDto();
    }

    /**
     * Create an instance of {@link ListaEmpleadoDto }
     * 
     * @return
     *     the new instance of {@link ListaEmpleadoDto }
     */
    public ListaEmpleadoDto createListaEmpleadoDto() {
        return new ListaEmpleadoDto();
    }

    /**
     * Create an instance of {@link ListaPlanillaFilaDto }
     * 
     * @return
     *     the new instance of {@link ListaPlanillaFilaDto }
     */
    public ListaPlanillaFilaDto createListaPlanillaFilaDto() {
        return new ListaPlanillaFilaDto();
    }

    /**
     * Create an instance of {@link PlanillaFilaDto }
     * 
     * @return
     *     the new instance of {@link PlanillaFilaDto }
     */
    public PlanillaFilaDto createPlanillaFilaDto() {
        return new PlanillaFilaDto();
    }

    /**
     * Create an instance of {@link Respuesta }
     * 
     * @return
     *     the new instance of {@link Respuesta }
     */
    public Respuesta createRespuesta() {
        return new Respuesta();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivoDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivoDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "archivoDto")
    public JAXBElement<ArchivoDto> createArchivoDto(ArchivoDto value) {
        return new JAXBElement<>(_ArchivoDto_QNAME, ArchivoDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaFilaDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultaFilaDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "consultaFilaDto")
    public JAXBElement<ConsultaFilaDto> createConsultaFilaDto(ConsultaFilaDto value) {
        return new JAXBElement<>(_ConsultaFilaDto_QNAME, ConsultaFilaDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmpleadoDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EmpleadoDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "empleadoDto")
    public JAXBElement<EmpleadoDto> createEmpleadoDto(EmpleadoDto value) {
        return new JAXBElement<>(_EmpleadoDto_QNAME, EmpleadoDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportarConsultaExcel }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExportarConsultaExcel }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "exportarConsultaExcel")
    public JAXBElement<ExportarConsultaExcel> createExportarConsultaExcel(ExportarConsultaExcel value) {
        return new JAXBElement<>(_ExportarConsultaExcel_QNAME, ExportarConsultaExcel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportarConsultaExcelResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExportarConsultaExcelResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "exportarConsultaExcelResponse")
    public JAXBElement<ExportarConsultaExcelResponse> createExportarConsultaExcelResponse(ExportarConsultaExcelResponse value) {
        return new JAXBElement<>(_ExportarConsultaExcelResponse_QNAME, ExportarConsultaExcelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportarPlanillaExcel }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExportarPlanillaExcel }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "exportarPlanillaExcel")
    public JAXBElement<ExportarPlanillaExcel> createExportarPlanillaExcel(ExportarPlanillaExcel value) {
        return new JAXBElement<>(_ExportarPlanillaExcel_QNAME, ExportarPlanillaExcel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExportarPlanillaExcelResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExportarPlanillaExcelResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "exportarPlanillaExcelResponse")
    public JAXBElement<ExportarPlanillaExcelResponse> createExportarPlanillaExcelResponse(ExportarPlanillaExcelResponse value) {
        return new JAXBElement<>(_ExportarPlanillaExcelResponse_QNAME, ExportarPlanillaExcelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarReporteEmpleadosPdf }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GenerarReporteEmpleadosPdf }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "generarReporteEmpleadosPdf")
    public JAXBElement<GenerarReporteEmpleadosPdf> createGenerarReporteEmpleadosPdf(GenerarReporteEmpleadosPdf value) {
        return new JAXBElement<>(_GenerarReporteEmpleadosPdf_QNAME, GenerarReporteEmpleadosPdf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarReporteEmpleadosPdfResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GenerarReporteEmpleadosPdfResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "generarReporteEmpleadosPdfResponse")
    public JAXBElement<GenerarReporteEmpleadosPdfResponse> createGenerarReporteEmpleadosPdfResponse(GenerarReporteEmpleadosPdfResponse value) {
        return new JAXBElement<>(_GenerarReporteEmpleadosPdfResponse_QNAME, GenerarReporteEmpleadosPdfResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarReporteMarcasPdf }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GenerarReporteMarcasPdf }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "generarReporteMarcasPdf")
    public JAXBElement<GenerarReporteMarcasPdf> createGenerarReporteMarcasPdf(GenerarReporteMarcasPdf value) {
        return new JAXBElement<>(_GenerarReporteMarcasPdf_QNAME, GenerarReporteMarcasPdf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarReporteMarcasPdfResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GenerarReporteMarcasPdfResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "generarReporteMarcasPdfResponse")
    public JAXBElement<GenerarReporteMarcasPdfResponse> createGenerarReporteMarcasPdfResponse(GenerarReporteMarcasPdfResponse value) {
        return new JAXBElement<>(_GenerarReporteMarcasPdfResponse_QNAME, GenerarReporteMarcasPdfResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaConsultaFilaDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListaConsultaFilaDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "listaConsultaFilaDto")
    public JAXBElement<ListaConsultaFilaDto> createListaConsultaFilaDto(ListaConsultaFilaDto value) {
        return new JAXBElement<>(_ListaConsultaFilaDto_QNAME, ListaConsultaFilaDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaEmpleadoDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListaEmpleadoDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "listaEmpleadoDto")
    public JAXBElement<ListaEmpleadoDto> createListaEmpleadoDto(ListaEmpleadoDto value) {
        return new JAXBElement<>(_ListaEmpleadoDto_QNAME, ListaEmpleadoDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaPlanillaFilaDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListaPlanillaFilaDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "listaPlanillaFilaDto")
    public JAXBElement<ListaPlanillaFilaDto> createListaPlanillaFilaDto(ListaPlanillaFilaDto value) {
        return new JAXBElement<>(_ListaPlanillaFilaDto_QNAME, ListaPlanillaFilaDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PlanillaFilaDto }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PlanillaFilaDto }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.relojservidor.una.ac.cr/", name = "planillaFilaDto")
    public JAXBElement<PlanillaFilaDto> createPlanillaFilaDto(PlanillaFilaDto value) {
        return new JAXBElement<>(_PlanillaFilaDto_QNAME, PlanillaFilaDto.class, null, value);
    }

}
