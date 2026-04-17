package com.menmar.gestionTienda.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.menmar.gestionTienda.persistence.entity.Ticket;
import com.menmar.gestionTienda.persistence.entity.TicketCalzadoLinea;
import com.menmar.gestionTienda.persistence.entity.TicketCosturaLinea;
import com.menmar.gestionTienda.persistence.entity.TicketLlaveLinea;
import com.menmar.gestionTienda.persistence.repository.TicketRepository;
import com.menmar.gestionTienda.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final Color COLOR_CABECERA  = new Color(30, 30, 30);
    private static final Color COLOR_SECCION   = new Color(60, 60, 60);
    private static final Color COLOR_FONDO_TBL = new Color(240, 240, 240);
    private static final Color COLOR_BORDE     = new Color(180, 180, 180);

    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public byte[] generarResguardoTicket(Long ticketId) {
        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket no encontrado: " + ticketId));

        try (var out = new ByteArrayOutputStream()) {
            var documento = new Document(PageSize.A5, 30, 30, 30, 30);
            PdfWriter.getInstance(documento, out);
            documento.open();

            agregarCabecera(documento, ticket);
            agregarSeparador(documento);
            agregarDatosTicket(documento, ticket);
            agregarSeparador(documento);
            agregarDetalleLineas(documento, ticket);
            agregarSeparador(documento);
            agregarTotal(documento, ticket);
            if (ticket.getObservaciones() != null && !ticket.getObservaciones().isBlank()) {
                agregarObservaciones(documento, ticket.getObservaciones());
            }
            agregarPie(documento);

            documento.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Error generando el PDF del ticket", e);
        }
    }

    private void agregarCabecera(Document doc, Ticket ticket) throws DocumentException {
        var fuenteTienda = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLOR_CABECERA);
        var fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 9, COLOR_SECCION);

        var titulo = new Paragraph("Reparaciones & Llaves", fuenteTienda);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);

        var subtitulo = new Paragraph("Reparación de calzado · Duplicado de llaves", fuenteSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(4);
        doc.add(subtitulo);

        var fuenteCodigo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COLOR_CABECERA);
        var codigo = new Paragraph(ticket.getNumeroTicket(), fuenteCodigo);
        codigo.setAlignment(Element.ALIGN_CENTER);
        codigo.setSpacingBefore(6);
        doc.add(codigo);
    }

    private void agregarDatosTicket(Document doc, Ticket ticket) throws DocumentException {
        var tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{40, 60});
        tabla.setSpacingBefore(6);
        tabla.setSpacingAfter(6);

        var tipo = switch (ticket.getTipo()) {
            case CALZADO -> "Reparación calzado";
            case COSTURA -> "Costura";
            case LLAVE   -> "Duplicado de llaves";
        };

        agregarFilaTabla(tabla, "Tipo", tipo);
        agregarFilaTabla(tabla, "Cliente",
                ticket.getCliente().getNombre() + " " +
                (ticket.getCliente().getApellidos() != null ? ticket.getCliente().getApellidos() : ""));
        agregarFilaTabla(tabla, "Teléfono", ticket.getCliente().getTelefono());
        agregarFilaTabla(tabla, "Fecha entrada",
                ticket.getFechaEntrada().format(FECHA_FMT));
        agregarFilaTabla(tabla, "Fecha prevista entrega",
                ticket.getFechaPrevista() != null
                        ? ticket.getFechaPrevista().format(FECHA_FMT)
                        : "—");
        agregarFilaTabla(tabla, "Estado", ticket.getEstado().name());

        doc.add(tabla);
    }

    private void agregarDetalleLineas(Document doc, Ticket ticket) throws DocumentException {
        var fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_SECCION);
        var tituloDetalle = new Paragraph("DETALLE DE LA REPARACIÓN", fuenteTitulo);
        tituloDetalle.setSpacingBefore(4);
        tituloDetalle.setSpacingAfter(4);
        doc.add(tituloDetalle);

        var tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{40, 20, 20, 20});

        agregarEncabezadoTabla(tabla, "Concepto", "Cant.", "Precio", "Subtotal");

        for (TicketCalzadoLinea l : ticket.getLineasCalzado()) {
            var subtotal = l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad()));
            agregarLineaTabla(tabla,
                    l.getTipoReparacion().getNombre(),
                    String.valueOf(l.getCantidad()),
                    formatEuros(l.getPrecioUnitario()),
                    formatEuros(subtotal));
        }

        for (TicketCosturaLinea l : ticket.getLineasCostura()) {
            var subtotal = l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad()));
            agregarLineaTabla(tabla,
                    l.getTipoCostura().getNombre(),
                    String.valueOf(l.getCantidad()),
                    formatEuros(l.getPrecioUnitario()),
                    formatEuros(subtotal));
        }

        for (TicketLlaveLinea l : ticket.getLineasLlave()) {
            var subtotal = l.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(l.getCantidad()));
            agregarLineaTabla(tabla,
                    l.getTipoLlave().getNombre(),
                    String.valueOf(l.getCantidad()),
                    formatEuros(l.getPrecioUnitario()),
                    formatEuros(subtotal));
        }

        doc.add(tabla);
    }

    private void agregarTotal(Document doc, Ticket ticket) throws DocumentException {
        var fuenteTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, COLOR_CABECERA);
        var total = new Paragraph(
                "TOTAL:  " + formatEuros(ticket.getPrecioTotal()), fuenteTotal);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(4);
        doc.add(total);
    }

    private void agregarObservaciones(Document doc, String observaciones) throws DocumentException {
        var fuenteLbl = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, COLOR_SECCION);
        var fuenteTxt = FontFactory.getFont(FontFactory.HELVETICA, 9, COLOR_SECCION);
        var p = new Paragraph();
        p.setSpacingBefore(6);
        p.add(new Chunk("Observaciones: ", fuenteLbl));
        p.add(new Chunk(observaciones, fuenteTxt));
        doc.add(p);
    }

    private void agregarPie(Document doc) throws DocumentException {
        var fuente = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);
        var pie = new Paragraph("Conserve este resguardo para recoger su pedido.", fuente);
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(10);
        doc.add(pie);
    }

    private void agregarSeparador(Document doc) throws DocumentException {
        var ls = new com.lowagie.text.pdf.draw.LineSeparator(0.5f, 100, COLOR_BORDE,
                Element.ALIGN_CENTER, -2);
        var p = new Paragraph(new Chunk(ls));
        p.setSpacingBefore(2);
        p.setSpacingAfter(2);
        doc.add(p);
    }

    private static void agregarFilaTabla(PdfPTable tabla, String etiqueta, String valor) {
        var fuenteEtiqueta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, COLOR_SECCION);
        var fuenteValor    = FontFactory.getFont(FontFactory.HELVETICA, 9);

        var celEtiqueta = new PdfPCell(new Phrase(etiqueta, fuenteEtiqueta));
        celEtiqueta.setBackgroundColor(COLOR_FONDO_TBL);
        celEtiqueta.setBorderColor(COLOR_BORDE);
        celEtiqueta.setPadding(4);
        tabla.addCell(celEtiqueta);

        var celValor = new PdfPCell(new Phrase(valor, fuenteValor));
        celValor.setBorderColor(COLOR_BORDE);
        celValor.setPadding(4);
        tabla.addCell(celValor);
    }

    private static void agregarEncabezadoTabla(PdfPTable tabla, String... cabeceras) {
        var fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.WHITE);
        for (var cab : cabeceras) {
            var cel = new PdfPCell(new Phrase(cab, fuente));
            cel.setBackgroundColor(COLOR_CABECERA);
            cel.setBorderColor(COLOR_BORDE);
            cel.setPadding(4);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cel);
        }
    }

    private static void agregarLineaTabla(PdfPTable tabla, String concepto,
                                          String cantidad, String precio, String subtotal) {
        var fuente = FontFactory.getFont(FontFactory.HELVETICA, 8);
        var fuenteDer = FontFactory.getFont(FontFactory.HELVETICA, 8);

        var celConcepto = new PdfPCell(new Phrase(concepto, fuente));
        celConcepto.setBorderColor(COLOR_BORDE);
        celConcepto.setPadding(3);
        tabla.addCell(celConcepto);

        for (var valor : new String[]{cantidad, precio, subtotal}) {
            var cel = new PdfPCell(new Phrase(valor, fuenteDer));
            cel.setBorderColor(COLOR_BORDE);
            cel.setPadding(3);
            cel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cel);
        }
    }

    private static String formatEuros(java.math.BigDecimal importe) {
        if (importe == null) return "0,00 €";
        return String.format("%.2f €", importe).replace('.', ',');
    }
}
