package net.codejava.swing.jbutton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.Statement;
import java.util.ArrayList;

import static net.codejava.swing.jbutton.SkiJumping.addField;
import static net.codejava.swing.jbutton.SkiJumping.addTextField;

public class AppJFrameAddToBase extends AppJFrame{
    ArrayList<AddField> fieldNames;
    ArrayList<JComponent> fields;
    String tableName;
    JFrame me;
    ArrayList<AppJComboBox> combos;
    AppJFrameAddToBase(ArrayList<AddField> fieldnames, String tableName){
        this.tableName = tableName;
        this.fieldNames = fieldnames;
        fields = new ArrayList<>();
        combos = new ArrayList<>();
        me = this;
    }
    @Override public void prepareToShow () {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //JFrame frame = new JFrame(FrameName);
                //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JPanel panel = new JPanel();

                panel.setLayout((LayoutManager) new BoxLayout(panel, BoxLayout.Y_AXIS));
                for (AddField af : fieldNames){
                    if (af.getType() == 'a'){
                        fields.add(null);
                        combos.add(null);
                    }
                    else if (af.getType() != 'c') {
                        fields.add(addTextField(null, panel, af.getFieldName()));
                        combos.add(null);
                    }
                    else{
                        AppJComboBox appJComboBox = new AppJComboBox(c, af.select);
                        JComponent jComponent = appJComboBox.getComponent();
                        fields.add(addField(null, panel, af.getFieldName(), jComponent));
                        combos.add(appJComboBox);
                    }
                }

                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                JButton button = new JButton("OK");
                inputpanel.add(button);
                panel.add(inputpanel);
                getContentPane().add(BorderLayout.WEST, panel);
                pack();
                //setLocationByPlatform(true);
                setVisible(true);
                setLocationRelativeTo(null);

                button.addActionListener(e -> {
                    try {
                        Statement stmt = c.createStatement();
                        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
                        //StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
                        for (int i = 0 ; i < fieldNames.size(); i++){
                            StringBuilder pom = new StringBuilder();
                            /*if (fieldNames.get(i).getType() == 's' || fieldNames.get(i).getType() == 'e'){
                                pom.append("'" + fields.get(i).getText() + "'");
                            }
                            else{
                                pom.append(fields.get(i).getText());
                            }*/
                            pom.append(fieldNames.get(i).getColumnName());
                            if (i != fieldNames.size() - 1){
                                pom.append(", ");
                            }
                            else {
                                pom.append(") VALUES (");
                            }
                            sql.append(pom);
                        }

                        for (int i = 0 ; i < fieldNames.size(); i++){
                            StringBuilder pom = new StringBuilder();
                            if (fieldNames.get(i).getType() == 'a'){
                                pom.append("'" + fieldNames.get(i).getDefaultValue() + "'");
                            }
                            else if (fieldNames.get(i).getType() == 's' || fieldNames.get(i).getType() == 'e'){
                                pom.append("'" + ((JTextField)(fields.get(i))).getText() + "'");
                            }
                            else if (fieldNames.get(i).getType() != 'c'){
                                pom.append(((JTextField)fields.get(i)).getText());
                            }
                            else{
                                pom.append(combos.get(i).getSelectedID());
                            }
                            if (i != fieldNames.size() - 1){
                                pom.append(", ");
                            }
                            else {
                                pom.append(");");
                            }
                            sql.append(pom);
                        }

                        System.out.println(sql);
                        stmt.executeUpdate(sql.toString());
                        stmt.close();
                        dispatchEvent(new WindowEvent(me, WindowEvent.WINDOW_CLOSING));
                        //c.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        System.err.println(e1.getClass().getName()+": "+e1.getMessage());
                        System.exit(0);
                    }

                });

            }
        });
    }
}
