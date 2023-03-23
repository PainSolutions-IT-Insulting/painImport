package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.model.ImportDTO;
import org.example.model.ImportDTOList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private Container con;
    private JPanel panel_eingaben = new JPanel(new BorderLayout());
    private JPanel panel_eingabengrid = new JPanel(new GridLayout(3,2));
    private JPanel panel_eingabenEast = new JPanel(new GridLayout(3,1));
    private JPanel panel_bottom = new JPanel(new GridLayout(2,2));
    private JLabel lbl_ueberschrift = new JLabel("Datenimport Preise");
    private JLabel lbl_lieferant = new JLabel("Lieferant");
    private JLabel lbl_importPfad = new JLabel("Importdatei");
    private JLabel lbl_exportPath = new JLabel("Speicherort");
    private JLabel lbl_exportPathValue = new JLabel("test");
    private JLabel lbl_importPathValue = new JLabel("Test2");
    private JButton btn_setImportPath = new JButton("Durchsuchen");
    private JButton btn_setExportPath = new JButton("Durchsuchen");
    private JButton btn_cancel = new JButton("abbrechen");
    private JButton btn_startImport = new JButton("Import starten");
    private JComboBox comBox_Lieferanten = new JComboBox();
    private String[] sizeArrInput = {/*Amerikanische Größen*/"2XS", "XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL", "5XL", "6XL", "7XL", "8XL", "9XL",
                                     /*Kurzgrößen */ "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
                                     /*Konfektionsgrößen*/"32", "34", "36", "38", "40", "42", "44", "46" , "48", "50", "52", "54", "56", "58", "60", "62", "64", "66", "68", "70", "72",
                                     /*Kindergrößen*/  "116", "122", "128", "134", "140", "146", "152", "158", "164", "170", "176",
                                     /*sonstiges?*/ "1", "2", "3","UNI",  ""};
    private String[] sizeArrOutput={ /*Amerikanische Größen*/"2XS", "XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL", "5XL", "6XL", "7XL", "8XL", "9XL",
                                     /*Kurzgrößen */ "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
                                     /*Konfektionsgrößen*/"32", "34", "36", "38", "40", "42", "44", "46" , "48", "50", "52", "54", "56", "58", "60", "62", "64", "66", "68", "70", "72",
                                     /*Kindergrößen*/  "116", "122", "128", "134", "140", "146", "152", "158", "164", "170", "176",
                                     /*sonstiges?*/ "1", "2", "3","UNI",  ""};

    protected final static String[] groessenAufbereitet =   {/*Amerikanische Größen*/"2XS", "XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL", "5XL", "6XL", "7XL", "8XL", "9XL",/*Kurzgrößen */ "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",  /*Konfektionsgrößen*/"32", "34", "36", "38", "40", "42", "44", "46" , "48", "50", "52", "54", "56", "58", "60", "62", "64", "66", "68", "70", "72",/*Kindergrößen*/  "116", "122", "128", "134", "140", "146", "152", "158", "164", "170", "176",/*sonstiges?*/ "1", "2", "3","UNI",  ""};
    private ArrayList<String> erg = new ArrayList<>();
    private Path importPath;
    private Path exportPath;
    private ImportDTOList importDTOList = new ImportDTOList();


    public static void main(String[] args){
        setFonts();
        FlatDarkLaf.setup();
        MainFrame mf = new MainFrame();
    }
    public MainFrame(){
        super("Datenimport Staufer");
        con = getContentPane();
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setupUI();
        addActionlistener();

        importPath = Paths.get("C:/Users/Patrick.stehle/Desktop/Hakro10032023.csv");
        exportPath = Paths.get("C:/Users/Patrick.stehle/Desktop/Hallihallo.csv");
        setVisible(true);
    }

    public void setupUI(){
        panel_bottom.add(new JLabel());
        panel_bottom.add(new JLabel());
        panel_bottom.add(btn_startImport);
        panel_bottom.add(btn_cancel);
        panel_eingaben.add(new JLabel("Datenimport"),BorderLayout.CENTER);
        panel_eingabengrid.add(lbl_lieferant);
        panel_eingabengrid.add(new JLabel());
        panel_eingabenEast.add(comBox_Lieferanten);
        panel_eingabenEast.add(btn_setImportPath);
        panel_eingabenEast.add(btn_setExportPath);
        panel_eingabengrid.add(lbl_importPfad);
        panel_eingabengrid.add(lbl_importPathValue);
        panel_eingabengrid.add(lbl_exportPath);
        panel_eingabengrid.add(lbl_exportPathValue);
        con.add(panel_eingaben,BorderLayout.NORTH);
        con.add(panel_bottom,BorderLayout.SOUTH);
        con.add(panel_eingabengrid,BorderLayout.CENTER);
        con.add(panel_eingabenEast,BorderLayout.EAST);
    }

    public void addActionlistener(){
        btn_setImportPath.addActionListener(e -> getImportPath());
        btn_setExportPath.addActionListener(e -> getExportPath());
        btn_cancel.addActionListener(e -> MainFrame.this.dispose());
        btn_startImport.addActionListener(e-> startImport());
    }

    public void getImportPath(){
        JFileChooser getImp = new JFileChooser();
        getImp.setFileFilter(new FileNameExtensionFilter("csv","csv"));

        if(getImp.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
            System.out.println(getImp.getSelectedFile());
            importPath = Paths.get(getImp.getSelectedFile().getAbsolutePath());
        }
    }
    public void getExportPath(){
        JFileChooser getExp = new JFileChooser();
        getExp.setFileFilter(new FileNameExtensionFilter("csv","csv"));

        if(getExp.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
            System.out.println(getExp.getSelectedFile());
            exportPath = Paths.get(getExp.getSelectedFile().getAbsolutePath());
        }
    }



    public void startImport(){

        readData();
        writeExport();
    }

    public void readData(){
        String reading ="";
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(importPath.toFile())));

            while((reading = br.readLine()) != null){
                createImportDTO(reading);

            }



            br.close();
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void writeExport(){


        String filename = "output.csv";

        try {
            FileWriter fw = new FileWriter(new File(exportPath.toUri()));
            PrintWriter pw = new PrintWriter(fw);

            StringBuilder sb = new StringBuilder();
            for (String value : erg) {
                pw.println(value);
            }



            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getSizeSpan(String sizeSpan){
        String[] sizeSpanAr;

        if (sizeSpan.contains("-")){
            String[] sizes = sizeSpan.split("-");
            String target1 = sizes[0];
            String target2 = sizes[1];
            int index1 = -1, index2 = -1;
            for (int i = 0; i < sizeArrInput.length; i++) {
                if (sizeArrInput[i].equals(target1)) {
                    index1 = i;
                }
                if (sizeArrInput[i].equals(target2)) {
                    index2 = i;
                    break;
                }
            }
            if (index1 == -1 || index2 == -1) {
                return new String[0];
            }

            int startIndex = Math.min(index1, index2);
            int endIndex = Math.max(index1, index2);

            String[] result = new String[endIndex - startIndex + 1];
            for (int i = startIndex; i <= endIndex; i++) {
                result[i - startIndex] = sizeArrOutput[i];
            }
            return result;
        }else if(sizeSpan.contains("+")){
            return sizeSpan.split("\\+");
        }

        sizeSpanAr = new String[]{sizeSpan};

        return sizeSpanAr;
    }

    public void createImportDTO(String reading){
        String[] values = reading.split(";");
        String[] farben = values[2].split(",");
        String supplierCode;
        ImportDTO imp = new ImportDTO();
        for (String f: farben){
            imp.setArtikelCodeL( builtSupplierCode(values[0],f));
            imp.setArtikelBezeichnungL(values[1]);
            imp.setArtikelGroesseL(values[3]);
            imp.setArtikelPreisL(convertToDouble(values[4]));
            importDTOList.getImportDTOList().add(imp);

        }
    }

    public double convertToDouble(String preis){
        NumberFormat nf = NumberFormat.getNumberInstance();
        preis = preis.replaceAll("[^\\d,]+", "");
// parse the string and convert it into a double value
        double d;
        try {
            d = nf.parse(preis).doubleValue();
            return d;
        } catch (ParseException e) {
            // handle the exception if the string cannot be parsed
            e.printStackTrace();
        }

        return -1;
    }
    public String builtSupplierCode(String artikelCode,String farbCode){
        farbCode = removeAlpha(farbCode);
        String result = artikelCode.substring(1)+ " " + (farbCode.length() < 3 ? "0" + farbCode : farbCode);
        return result ;
    }
    public String removeAlpha(String str){
        if (str == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                result.append(ch);
            }
        }

        return result.toString();
    }
    public String getHakroSatz(String[] values, String farben, String size){
        String hakro = "";
        hakro = "70016;" +
                "X;"+
                values[0].substring(1)+ " " + (farben.length() < 3 ? "0" + farben : farben) + ";"+
                size + ";"+
                "0;"+
                "X;"+
                "N;"+
                "0;"+
                "0;"+
                "0;"+
                "0;"+
                "0;";
        return hakro;
    }
    public static void setFonts(){
        Font font = new Font("Tahoma", Font.PLAIN, 20);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("TitledBorder.font", font);
    }
}
