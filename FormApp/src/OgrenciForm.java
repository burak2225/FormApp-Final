import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class OgrenciForm extends JFrame{
    private JTextField textFieldOgrenciAd;
    private JTextField textFieldOgrenciNo;
    private JRadioButton radioButton3BIP;
    private JRadioButton radioButton3ASC;
    private JList<String> listOgrenciDers;
    private JButton buttonOgrenciekle;
    private JLabel labelOgrenciAd;
    private JLabel labelOgrenciNo;
    private JLabel labelOgrenciBolum;
    private JLabel labelOgrenciDersler;
    private JLabel labelOgrenciKontrol;
    private JPanel JPanelOgrenci;
    private JButton buttonmenu;
    private JTextField textFieldogrenciarama;
    private JList listogrenciarama;

    private DefaultListModel<String> searchListModel;
    public class Ogrenci{
        String ogrenciNo;
        String ogrenciAd;
        String ogrenciBolum;
        String[] ogrenciDersler;

        public Ogrenci(String ogrenciNo, String ogrenciAd, String ogrenciBolum, String[] ogrenciDersler){
            this.ogrenciNo = ogrenciNo;
            this.ogrenciAd = ogrenciAd;
            this.ogrenciBolum = ogrenciBolum;
            this.ogrenciDersler = ogrenciDersler;
        }
    }


    public OgrenciForm(){
        setTitle("Öğrenci Formu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(JPanelOgrenci);
        DefaultListModel<String> dersListModel = new DefaultListModel<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("dersler.csv"))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] columns = line.split(",");
                String dersAdi = columns[0];
                dersListModel.addElement(dersAdi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        listOgrenciDers.setModel(dersListModel);


        buttonOgrenciekle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String no = textFieldOgrenciNo.getText();
                String ad = textFieldOgrenciAd.getText();
                String bolum;
                if (radioButton3BIP.isSelected()){
                    bolum = radioButton3BIP.getText();
                }
                else{
                    bolum = radioButton3ASC.getText();
                }
                Object[] selected = listOgrenciDers.getSelectedValues();
                String[] ogrenciDersler = new String[selected.length];
                for(int i=0; i<selected.length;i++){
                    ogrenciDersler[i] = selected[i].toString();
                }
                Ogrenci ogrenci = new Ogrenci(no,ad,bolum,ogrenciDersler);
                labelOgrenciKontrol.setText(String.format("%s Sisteme Kaydedildi.", ad));

                writeOgrenciToCSV(ogrenci, "ogrenciler.csv");


            }
        });
        buttonmenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Menu();
                dispose();
            }
        });
        // öğrenci arama
        searchListModel = new DefaultListModel<>();
        listogrenciarama.setModel(searchListModel);

        textFieldogrenciarama.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOgrenci(textFieldogrenciarama.getText());
            }
        });
    }
    private void writeOgrenciToCSV(OgrenciForm.Ogrenci ogrenci, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Append the new record to the CSV file
            writer.write(String.format("%s,%s,%s,%s\n",
                    ogrenci.ogrenciNo, ogrenci.ogrenciAd, ogrenci.ogrenciBolum, String.join(",", ogrenci.ogrenciDersler)));
            System.out.println("CSV file updated successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void searchOgrenci(String searchName) {
        searchListModel.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("ogrenciler.csv"))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] columns = line.split(",");
                String ogrenciAd = columns[1].trim();
                if (ogrenciAd.toLowerCase().contains(searchName.toLowerCase())) {
                    searchListModel.addElement(ogrenciAd);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new OgrenciForm();

    }
}
