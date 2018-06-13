package eduskunta.avoin.data;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Apache lisenssi (https://code.google.com/archive/p/json-simple/)
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

// Apache lisenssi (https://pdfbox.apache.org/)
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author tainalep
 */
public class ReadApi {

    //public Long getNextSetFromVaski(Long currentId, Set<String> asTyypit) {
    public Long getNextSetFromVaski(Long currentId) {

        try {

            StringBuilder result = new StringBuilder();
            URL url = new URL("http://avoindata.eduskunta.fi/api/v1/tables/VaskiData/batch?pkName=Id&pkStartValue=" + currentId + "&perPage=100");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            JSONParser parser = new JSONParser();
            JSONObject a = (JSONObject) parser.parse(result.toString());
            JSONArray rows = (JSONArray) a.get("rowData");
            Long pkLastValue = (Long) a.get("pkLastValue");

            //System.out.println(rows.toString());
            //JSONArray a = (JSONArray) parser.parse(result.toString());
            for (Object o : rows) {
                JSONArray row = (JSONArray) o;

                //System.out.println(o.toString());
                String eduskuntaTunnus = (String) row.get(4);
                //System.out.println("eduskuntaTunnus: " + eduskuntaTunnus);

                String id = (String) row.get(0);
                String attachmentGroupId = (String) row.get(5);

                if (eduskuntaTunnus.contains("PeVL")) {
                    downloadAttachemts(attachmentGroupId, "Valiokunnan lausunto");
                } else if (eduskuntaTunnus.contains("PeVM")) {
                    downloadAttachemts(attachmentGroupId, "Valiokunnan mietintö");
                } else {

                    String xmlData = (String) row.get(1);
                    //System.out.println("XmlData: " + xmlData);

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse(new InputSource(new StringReader(xmlData)));

                    String asTyyppi = "";
                    try {
                        asTyyppi = document.getElementsByTagName("met1:AsiakirjatyyppiNimi").item(0).getTextContent();
                        //System.out.println("asTyyppi: " + asTyyppi);
                        //asTyypit.add(asTyyppi);
                    } catch (Exception e) {
                        try {
                            asTyyppi = document.getElementsByTagName("ns:AsiakirjatyyppiNimi").item(0).getTextContent();
                            //System.out.println("asTyyppi: " + asTyyppi);
                            //asTyypit.add(asTyyppi);
                        } catch (Exception ex) {
                            System.out.println("Virhe: asiakirjatyyppiä ei voitu päätellä ,Id: " + id);
                        }
                    }

                    //System.out.println("Id: " + id);
                    //System.out.println("AttachmentGroupId: " + attachmentGroupId);
                    //System.out.println("asTyyppi: " + asTyyppi);
                    //if (eduskuntaTunnus.contains("PeV")) {
                    if (asTyyppi.equals("Asiantuntijalausunto")) {
                        //System.out.println("Löydettiin asiantuntijalausunto");
                        downloadAttachemts(attachmentGroupId, asTyyppi);

                    }

                }

            }

            return pkLastValue;

        } catch (Exception e) {
            System.out.println("Virhe aineiston haussa. ");
            return currentId;
        }

    }

    public void downloadAttachemts(String AttachmentGroupId, String asTyyppi) {

        try {

            if (AttachmentGroupId == null || AttachmentGroupId.isEmpty()) {
                //System.out.println("Virhe liitteen lataamisessa, koska AttachmentGroupId on NULL ");
                return;
            }

            StringBuilder result = new StringBuilder();
            URL url = new URL("http://avoindata.eduskunta.fi/api/v1/tables/Attachment/rows?columnName=AttachmentGroupId&columnValue=" + AttachmentGroupId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            JSONParser parser = new JSONParser();
            JSONObject a = (JSONObject) parser.parse(result.toString());
            JSONArray rows = (JSONArray) a.get("rowData");

            for (Object o : rows) {
                JSONArray row = (JSONArray) o;

                //System.out.println(o.toString());
                String liiteUrl = (String) row.get(2);
                liiteUrl = liiteUrl.replace("\"", "");
                String liiteNimi = liiteUrl.substring(liiteUrl.lastIndexOf("/") + 1, liiteUrl.length());
                //System.out.println("liiteUrl: " + liiteUrl);
                //System.out.println("liiteNimi: " + liiteNimi);

                URL url2 = new URL(liiteUrl);
                InputStream in = url2.openStream();

                Path liiteKohde;
                if (asTyyppi.equals("Valiokunnan lausunto")) {
                    liiteKohde = FileSystems.getDefault().getPath(System.getProperty("user.dir") + File.separator + "lausunto" + File.separator + liiteNimi);
                    //System.out.println("liiteKohde: " + liiteKohde.toString());
                    Files.copy(in, liiteKohde, StandardCopyOption.REPLACE_EXISTING);
                    in.close();
                } else if (asTyyppi.equals("Valiokunnan mietintö")) {
                    liiteKohde = FileSystems.getDefault().getPath(System.getProperty("user.dir") + File.separator + "mietinto" + File.separator + liiteNimi);
                    //System.out.println("liiteKohde: " + liiteKohde.toString());
                    Files.copy(in, liiteKohde, StandardCopyOption.REPLACE_EXISTING);
                    in.close();
                } else {
                    liiteKohde = FileSystems.getDefault().getPath(System.getProperty("user.dir") + File.separator + "asiantuntija" + File.separator + liiteNimi);

                    try {
                        PDDocument document = PDDocument.load(in);
                        String text = "";
                        if (!document.isEncrypted()) {
                            PDFTextStripper stripper = new PDFTextStripper();
                            text = stripper.getText(document);
                            //System.out.println("Text:" + text);
                        }
                        document.close();
                        in.close();

                        if (text.toLowerCase().contains("perustuslakivaliokunta")
                                || text.toLowerCase().contains("perustuslakivaliokunnan")
                                || text.toLowerCase().contains("perustuslakivaliokunnalle")) {

                            InputStream in2 = url2.openStream();

                            //System.out.println("liitelähde: " + liiteUrl);
                            //System.out.println("liiteKohde: " + liiteKohde.toString());
                            Files.copy(in2, liiteKohde, StandardCopyOption.REPLACE_EXISTING);
                            in2.close();
                        }
                    } catch (Exception e) {
                        System.out.println("Virhe liitteen lataamisessa: " + e.toString());
                    }

                }

            }

        } catch (Exception e) {
            System.out.println("Virhe liitteen lataamisessa: " + e.toString());
        }

    }

}
