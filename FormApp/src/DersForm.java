import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class DersForm extends JFrame {
    private JLabel labelDersisim;
    private JTextField textFieldisim;
    private JLabel labelDersbolum;
    private JRadioButton radioButtonBIP;
    private JRadioButton radioButtonasci;
    private JLabel labelDerskod;
    private JTextField textFieldders;
    private JLabel labelDersdonem;
    private JLabel labeldonemaciklama;
    private JComboBox comboBoxDersdonem;
    private JLabel labelDersgorevli;
    private JComboBox<String> comboBoxDersgorevli;
    private JLabel labelDerskodaciklama;
    private JButton buttonDersekle;
    private JLabel labelDerseklendikontrol;
    private JPanel JPanelDers;
    private JButton buttonmenu;
    private JTextField textFielddersarama;
    private JList listdersarama;
    private ButtonGroup buttonGroup1;

    private DefaultListModel<String> searchListModel;



    public class Ders {
        public String ad;
        public String bolum;
        public String kod;
        public String donem;
        public String gorevli;

        public Ders(String ad, String bolum, String kod, String donem, String gorevli){
            this.ad = ad;
            this.bolum = bolum;
            this.kod = kod;
            this.donem = donem;
            this.gorevli = gorevli;
        }
    }


    private void writeDersToCSV(DersForm.Ders ders, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Append the new record to the CSV file
            writer.write(String.format("%s,%s,%s,%s\n",
                    ders.ad, ders.bolum, ders.kod, String.join(",", ders.gorevli)));
            System.out.println("CSV file updated successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public DersForm() {


        setTitle("Ders Formu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(JPanelDers);
        DefaultComboBoxModel<String> gorevliModel = new DefaultComboBoxModel<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("ogrgorevliler.csv"))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] columns = line.split(",");
                String gorevliAdi = columns[1];
                gorevliModel.addElement(gorevliAdi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        comboBoxDersgorevli.setModel(gorevliModel);


        buttonDersekle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dersadi = textFieldisim.getText();
                String dersbolumu;
                if (radioButtonBIP.isSelected()){
                    dersbolumu = radioButtonBIP.getText();
                }
                else{
                    dersbolumu = radioButtonasci.getText();
                }
                String derskodu = textFieldders.getText();
                String dersdonemi = comboBoxDersdonem.getSelectedItem().toString();
                String dersgorevli = comboBoxDersgorevli.getSelectedItem().toString();
                Ders ders = new Ders(dersadi, dersbolumu, derskodu, dersdonemi, dersgorevli);



                writeDersToCSV(ders, "dersler.csv");
                labelDerseklendikontrol.setText(String.format("%s Dersi Sisteme Kaydedildi.", dersadi));
            }
        });

        buttonmenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Menu();
                dispose();
            }
        });
        //ders arama
        searchListModel = new DefaultListModel<>();
        listdersarama.setModel(searchListModel);
        textFielddersarama.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDers(textFielddersarama.getText());
            }
        });
    }
    private void searchDers(String searchName) {
        searchListModel.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("dersler.csv"))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] columns = line.split(",");
                String dersAd = columns[0].trim();
                if (dersAd.toLowerCase().contains(searchName.toLowerCase())) {
                    searchListModel.addElement(dersAd);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new DersForm();

    }
}