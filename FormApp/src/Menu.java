import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame{
    private JButton buttonDersForm;
    private JButton buttonOgrenciForm;
    private JButton buttonGorevliForm;
    private JPanel JPanelMenu;

    public Menu() {
        setTitle("Menu Formu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(JPanelMenu);


        buttonDersForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new DersForm();
                dispose();
            }
        });
        buttonOgrenciForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OgrenciForm();
                dispose();
            }
        });
        buttonGorevliForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OgrGorevlisiForm();
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        new Menu();

    }
}
