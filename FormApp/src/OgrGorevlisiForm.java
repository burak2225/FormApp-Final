import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OgrGorevlisiForm extends JFrame {
    private JLabel labelGorevliAd;
    private JTextField textFieldGorevliAd;
    private JLabel labelGorevliNo;
    private JTextField textFieldGorevliNo;
    private JLabel labelGorevliDersler;
    private JLabel labelGorevliBolum;
    private JList<String> listGorevliDersler;
    private JRadioButton radioButton2BIP;
    private JRadioButton radioButton2ascilik;
    private JButton buttonGorevliekle;
    private JLabel labelGorevliKontrol;
    private JPanel JPanelGorevli;
    private JButton buttonmenu;
    private JTextField textFieldgorevliarama;
    private JList listgorevliarama;

    private List<OgrGorevlisi> ogrGorevliList;

    private DefaultListModel<String> searchListModel;



    public class OgrGorevlisi{
        String gorevliNo;
        String gorevliAd;
        String gorevliBolum;
        String[] gorevliDersler;

        public OgrGorevlisi(String gorevliNo, String gorevliAd, String gorevliBolum, String[] gorevliDersler){
            this.gorevliNo = gorevliNo;
            this.gorevliAd = gorevliAd;
            this.gorevliBolum = gorevliBolum;
            this.gorevliDersler = gorevliDersler;
        }
    }
    public OgrGorevlisiForm(){
        setTitle("Öğretmen Formu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(JPanelGorevli);
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

        listGorevliDersler.setModel(dersListModel);


        buttonGorevliekle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gorevliNo = textFieldGorevliNo.getText();
                String gorevliAd = textFieldGorevliAd.getText();
                String gorevliBolum;
                if (radioButton2BIP.isSelected()){
                    gorevliBolum = radioButton2BIP.getText();
                }
                else{
                    gorevliBolum = radioButton2ascilik.getText();
                }
                Object[] selected = listGorevliDersler.getSelectedValues();
                String[] gorevliDersler = new String[selected.length];
                for(int i=0; i<selected.length;i++){
                    gorevliDersler[i] = selected[i].toString();
                }
                OgrGorevlisi Gorevli = new OgrGorevlisi(gorevliNo, gorevliAd, gorevliBolum, gorevliDersler);
                labelGorevliKontrol.setText(String.format("%s Sisteme Kaydedildi.", gorevliAd));

                writeGorevliToCSV(Gorevli, "ogrgorevliler.csv");
            }
        });

        buttonmenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Menu();
                dispose();
            }
        });


        //Gorevli arama fonksiyonu
        searchListModel = new DefaultListModel<>();
        listgorevliarama.setModel(searchListModel);
        textFieldgorevliarama.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchGorevli(textFieldgorevliarama.getText());
            }
        });
    }
    private void writeGorevliToCSV(OgrGorevlisi gorevli, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Append the new record to the CSV file
            writer.write(String.format("%s,%s,%s,%s\n",
                    gorevli.gorevliNo, gorevli.gorevliAd, gorevli.gorevliBolum, String.join(",", gorevli.gorevliDersler)));
            System.out.println("CSV file updated successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void searchGorevli(String searchName) {
        searchListModel.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("ogrgorevliler.csv"))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] columns = line.split(",");
                String gorevliAd = columns[1].trim(); // Assuming the second column is the name
                if (gorevliAd.toLowerCase().contains(searchName.toLowerCase())) {
                    searchListModel.addElement(gorevliAd);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new OgrGorevlisiForm();
    }
}
