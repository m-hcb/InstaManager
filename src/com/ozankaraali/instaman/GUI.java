package com.ozankaraali.instaman;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class GUI {
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JTabbedPane followManager;
    private JButton unfBtn;
    private JList unfList;
    public JPanel mainField;
    private JList fansList;
    private JList followingList;
    private JList followersList;
    private JButton fansBtn;
    private JButton followingBtn;
    private JLabel followingCtr;
    private JLabel followersCtr;
    private JButton followersBtn;
    private JTabbedPane mainTab;
    private JTextField ppUserName;
    private JButton ppQueryBtn;
    private JPanel ppField;
    private JLabel picLabel;
    private JButton downloadButton;
    private JButton aboutButton;
    private JTextField sneakPeek;
    private JCheckBox sneakCheck;
    private JPanel statusBar;
    private JLabel statusText;

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Instagram Manager");
        GUI gui = new GUI();
        Instaman instaman = new Instaman();

        gui.sneakCheck.addItemListener(l -> {
            if(gui.sneakCheck.isSelected()){
                gui.sneakPeek.setEnabled(true);
                      gui.unfBtn.setText("Follow from YOUR account");
                     gui.fansBtn.setText("Follow from YOUR account");
                gui.followingBtn.setText("Follow from YOUR account");
                gui.followersBtn.setText("Follow from YOUR account");
            }
            else{
                gui.sneakPeek.setEnabled(false);
                instaman.setSneakUsername(null);
                gui.sneakPeek.setText("");
                      gui.unfBtn.setText("Unfollow");
                     gui.fansBtn.setText("Follow");
                gui.followingBtn.setText("Unollow");
                gui.followersBtn.setText("Follow");
            }
        });

        gui.loginButton.addActionListener(e -> {
            gui.statusText.setText("Logging in and loading, please wait...");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try{
                //if((!instaman.isLoggedin())||(instaman.isLoggedin()==null)){
                    instaman.setPassword(new String(gui.password.getPassword()));
                    instaman.setUsername(gui.username.getText());
                    instaman.setSneakUsername(gui.sneakPeek.getText());
                    instaman.builder();
                //}
                /*else{
                    instaman.setSneakUsername(gui.sneakPeek.getText());
                    instaman.refreshResult();
                }*/


                DefaultListModel<String> followersModel = new DefaultListModel<>();
                for (Object val : instaman.getFollowersList().toArray())
                    followersModel.addElement((String) val);
                gui.followersList.setModel(followersModel);

                DefaultListModel<String> followingModel = new DefaultListModel<>();
                for (Object val : instaman.getFollowingList().toArray())
                    followingModel.addElement((String) val);
                gui.followingList.setModel(followingModel);

                DefaultListModel<String> unfModel = new DefaultListModel<>();
                for (Object val : instaman.getUnfList().toArray())
                    unfModel.addElement((String) val);
                gui.unfList.setModel(unfModel);

                DefaultListModel<String> fansModel = new DefaultListModel<>();
                for (Object val : instaman.getFansList().toArray())
                    fansModel.addElement((String) val);
                gui.fansList.setModel(fansModel);

                gui.statusText.setText("Status: OK!");

                gui.followersCtr.setText("Followers: " + Integer.toString(instaman.getFollowersNum()));
                gui.followingCtr.setText("Following: " + Integer.toString(instaman.getFollowingNum()));}

                catch (Exception npe){
                    gui.statusText.setText("An error occured. Please check your username/password and click login again.");
                }

            });

        gui.unfBtn.addActionListener(e ->{
            if(!gui.sneakCheck.isSelected()){
                instaman.unfollowRequest(gui.unfList.getSelectedValue().toString());
                DefaultListModel modify = (DefaultListModel) gui.unfList.getModel();
                modify.remove(gui.unfList.getSelectedIndex());
                gui.unfList.setModel(modify);
            }
            else {
                instaman.followRequest(gui.unfList.getSelectedValue().toString());
            }
        });
        gui.followersBtn.addActionListener(e -> {
            if(!gui.sneakCheck.isSelected()) {
                instaman.followRequest(gui.followersList.getSelectedValue().toString());
            }
            else {
                instaman.followRequest(gui.followersList.getSelectedValue().toString());
            }
        });
        gui.followingBtn.addActionListener(e -> {
            if(!gui.sneakCheck.isSelected()) {
                instaman.unfollowRequest(gui.followingList.getSelectedValue().toString());
                DefaultListModel modify = (DefaultListModel) gui.followingList.getModel();
                modify.remove(gui.unfList.getSelectedIndex());
                gui.followingList.setModel(modify);
            }
            else {
                instaman.followRequest(gui.followingList.getSelectedValue().toString());
            }
        });
        gui.fansBtn.addActionListener(e -> {
            if(!gui.sneakCheck.isSelected()){
                instaman.followRequest(gui.fansList.getSelectedValue().toString());
                DefaultListModel modify = (DefaultListModel) gui.fansList.getModel();
                modify.remove(gui.fansList.getSelectedIndex());
                gui.fansList.setModel(modify);
            }
            else{
                instaman.followRequest(gui.fansList.getSelectedValue().toString());
            }
        });

        gui.ppQueryBtn.addActionListener(e -> {
                try {
                    URL url = new URL(instaman.getProfilePic(gui.ppUserName.getText()));
                    BufferedImage image = ImageIO.read(url);
                    Image dimg = image.getScaledInstance(500, 500,
                            Image.SCALE_SMOOTH);
                    gui.picLabel.setIcon(new ImageIcon(dimg));
                    //gui.ppField.add(new JLabel(new ImageIcon(image)));
                } catch (Exception xc) {
                    xc.printStackTrace();
                }

        });

        gui.downloadButton.addActionListener(e -> {
                try {
                    //TODO: Downloader
                    URL url = new URL(instaman.getProfilePic(gui.ppUserName.getText()));
                    BufferedImage image = ImageIO.read(url);
                    JFileChooser savefile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    //savefile.showSaveDialog(image);
                } catch (Exception xc) {
                    xc.printStackTrace();
                }
        });

        gui.aboutButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "2017, Ozan Karaali. \n" +
                        "https://github.com/ozankaraali\n" +
                        "Uses Bruno Volpato's Instagram4J library. \n" +
                        "https://github.com/brunocvcunha/instagram4j");
        });

        frame.setContentPane(gui.mainField);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(580, 720);
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(gui.loginButton);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainField = new JPanel();
        mainField.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        username = new JTextField();
        mainField.add(username, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        loginButton = new JButton();
        loginButton.setText("Login");
        mainField.add(loginButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        password = new JPasswordField();
        mainField.add(password, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        followingCtr = new JLabel();
        followingCtr.setText("Following: N/A");
        mainField.add(followingCtr, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        followersCtr = new JLabel();
        followersCtr.setText("Followers: N/A");
        mainField.add(followersCtr, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainTab = new JTabbedPane();
        mainField.add(mainTab, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        followManager = new JTabbedPane();
        mainTab.addTab("Follow Manager", followManager);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        followManager.addTab("Users Not Following You", panel1);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setVerticalScrollBarPolicy(22);
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        unfList = new JList();
        scrollPane1.setViewportView(unfList);
        unfBtn = new JButton();
        unfBtn.setText("Unfollow");
        panel1.add(unfBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        followManager.addTab("Fans", panel2);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fansList = new JList();
        scrollPane2.setViewportView(fansList);
        fansBtn = new JButton();
        fansBtn.setText("Follow");
        panel2.add(fansBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        followManager.addTab("Following", panel3);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel3.add(scrollPane3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        followingList = new JList();
        scrollPane3.setViewportView(followingList);
        followingBtn = new JButton();
        followingBtn.setText("Unfollow");
        panel3.add(followingBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        followManager.addTab("Followers", panel4);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel4.add(scrollPane4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        followersList = new JList();
        scrollPane4.setViewportView(followersList);
        followersBtn = new JButton();
        followersBtn.setText("Follow");
        panel4.add(followersBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainTab.addTab("Profile Pic Viewer", panel5);
        ppUserName = new JTextField();
        panel5.add(ppUserName, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ppField = new JPanel();
        ppField.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(ppField, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        picLabel = new JLabel();
        picLabel.setText("");
        ppField.add(picLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadButton = new JButton();
        downloadButton.setText("Download");
        panel5.add(downloadButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ppQueryBtn = new JButton();
        ppQueryBtn.setText("Retrieve");
        panel5.add(ppQueryBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainField;
    }
}
