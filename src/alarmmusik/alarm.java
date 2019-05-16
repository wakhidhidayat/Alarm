/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmmusik;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author user
 */
public class alarm extends javax.swing.JFrame {
    Playlist pl = new Playlist();
    
    ArrayList updateList = new ArrayList();

    private static File filename;
    private static Player player;
    /**
     * Creates new form alarm
     */
    public alarm() {
        initComponents();
        alarmmusik();
    }
    
    void updateList() {
        updateList = pl.getListSong();
        DefaultListModel model = new DefaultListModel();
        for(int i=0; i<updateList.size(); i++) {
            int j=i+1;
            model.add(i, ((File) updateList.get(i)).getName());
        }
        playlist.setModel(model);
    }
    
    void add() {
        pl.add(this);
        pl.fc.setMultiSelectionEnabled(true);
        updateList();
    }
    
    void tunda() {
        if(player!=null){
        player.close();
        
        btstop.setEnabled(false);
        reset.setEnabled(true);
        submit.setEnabled(false);
        btntunda.setEnabled(true);
            
        lbltanda.setText(null);
        lberror.setText("Alarm di tunda 5 menit");
            
        java.util.Date dateTime = new java.util.Date();
        
        int a, b, tunda , jam, menit;
        a = dateTime.getMinutes();
        b = dateTime.getHours();
        tunda = a + 5;
            if(tunda>59){
                jam = b + 1;
                menit = tunda - 60;
                if(jam<=9){
                    lbjam.setText("0"+ Integer.toString(jam));
                }
                else{
                    lbjam.setText(Integer.toString(jam));
                }
                if(menit<=9){
                    lbmenit.setText("0"+Integer.toString(menit));
                }
                else{
                    lbmenit.setText(Integer.toString(menit));
                }
            }
            else{
                if(tunda<=9){
                    lbmenit.setText("0"+Integer.toString(tunda));
                }
                else{
                    lbmenit.setText(Integer.toString(tunda));
                }
                if(b<=9){
                    lbjam.setText("0"+Integer.toString(b));
                }
                else{
                    lbjam.setText(Integer.toString(b));
                }
                
            }
        }
    }
    
    void playlagu(){
        try {            
            FileInputStream lagu1 = new FileInputStream(filename);

            BufferedInputStream bis = new BufferedInputStream(lagu1);
            
            player = new Player(bis);
            
        }
        
       catch (FileNotFoundException | JavaLayerException e){
           System.out.print(e);
       } 
        new Thread(){
                @Override
                public void run(){
                    try {
                        player.play();
                    } catch (JavaLayerException ex) {
                        JOptionPane.showMessageDialog(null, "error");
                    }
                            
                }
        }.start();
    }
    
    static int a = 0;
    
    void select() {
        if(a == 0) {
            try{
                int pl = playlist.getSelectedIndex();
                filename = (File) this.updateList.get(pl);
                FileInputStream fis = new FileInputStream(filename);
                BufferedInputStream bis = new BufferedInputStream(fis);
                player = new javazoom.jl.player.Player(bis);
                a = 1;
            }catch (Exception e) {
                System.out.println("Problem");
                System.out.println(e);
            }
            
            new Thread() {
                @Override
                public void run() {
                    try{
                        player.play();
                    }catch(Exception e) {
                        
                    }
                }
            }.start();
        }else{
            player.close();
            a = 0;
            select();
        }
    }
    
    public final void alarmmusik(){
        ActionListener taskPerformer;
        taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                java.util.Date dateTime = new java.util.Date();
                int nilai_jam= dateTime.getHours();
                int nilai_menit = dateTime.getMinutes();
                int nilai_detik = dateTime.getSeconds();
                
                String nol_jam ="", nol_menit="",nol_detik="";
                if(nilai_jam<=9) nol_jam="0";
                if(nilai_menit<=9) nol_menit="0";
                if(nilai_detik<=9) nol_detik="0";
                
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                
                waktu.setText(jam+":"+menit+":"+detik+"");
                
                String a = lbjam.getText();
                String b = lbmenit.getText();
                String c = lbdetik.getText();
                
                if(jam.equals(a) && menit.equals(b) && detik.equals(c))
                {
                    lbltanda.setText("Alarm Hidup!");
                    playlagu();
                    lberror.setText(null);
                    btstop.setEnabled(true);
                    btntunda.setEnabled(true);
                }
            };
        };
        new Timer(1000, taskPerformer).start();
    }
    
    void submit() {
        
        if(a == 1) {
            player.close();
            a = 0;
        }
        submit.setEnabled(false);
        reset.setEnabled(true);
        btstop.setEnabled(false);
        btntunda.setEnabled(false);



            String a = (String) cbjam.getSelectedItem();
            String b = (String) cbmenit.getSelectedItem();
            String c = (String) cbdetik.getSelectedItem();

            lbjam.setText(a);
            lbmenit.setText(b);
            lbdetik.setText(c);

            int pl = playlist.getSelectedIndex();
            filename = (File) this.updateList.get(pl);

            JOptionPane.showMessageDialog(this, "Alarm berhasil di aktifkan");        
    }
    
    void reset() {
        stop();
        
        reset.setEnabled(false);
        submit.setEnabled(true);
        btstop.setEnabled(false);
        btntunda.setEnabled(false);

            cbjam.setSelectedItem("00");
            cbmenit.setSelectedItem("00");
            cbdetik.setSelectedItem("00");


            if(player != null){
            player.close();
            }        
    }
    
    void stop() {
        if(player != null){  
        player.close(); 
        }        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();
        submit = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        btntunda = new javax.swing.JButton();
        btstop = new javax.swing.JButton();
        lbltanda = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbjam = new javax.swing.JLabel();
        lbmenit = new javax.swing.JLabel();
        lbdetik = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbjam = new javax.swing.JComboBox<>();
        cbmenit = new javax.swing.JComboBox<>();
        cbdetik = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        lberror = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        playlist = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jam Digital");
        setBackground(new java.awt.Color(0, 204, 153));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Alarm :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Masukan waktu :");

        waktu.setFont(new java.awt.Font("Digital-7", 0, 35)); // NOI18N
        waktu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        waktu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        waktu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        submit.setBackground(new java.awt.Color(0, 0, 204));
        submit.setForeground(new java.awt.Color(255, 255, 255));
        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        reset.setBackground(new java.awt.Color(0, 153, 51));
        reset.setForeground(new java.awt.Color(255, 255, 255));
        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        btntunda.setBackground(new java.awt.Color(0, 102, 102));
        btntunda.setForeground(new java.awt.Color(255, 255, 255));
        btntunda.setText("Tunda");
        btntunda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntundaActionPerformed(evt);
            }
        });

        btstop.setBackground(new java.awt.Color(255, 0, 0));
        btstop.setForeground(new java.awt.Color(255, 255, 255));
        btstop.setText("Hentikan Alarm!");
        btstop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btstopActionPerformed(evt);
            }
        });

        lbltanda.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        lbltanda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltanda.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Detik");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Menit");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Jam");

        lbjam.setFont(new java.awt.Font("Open 24 Display St", 0, 16)); // NOI18N
        lbjam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbjam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbmenit.setFont(new java.awt.Font("Open 24 Display St", 0, 16)); // NOI18N
        lbmenit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbmenit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbdetik.setFont(new java.awt.Font("Open 24 Display St", 0, 16)); // NOI18N
        lbdetik.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbdetik.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText(":");

        jLabel10.setText(":");

        jLabel2.setFont(new java.awt.Font("NewsGoth Cn BT", 0, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("JAM DIGITAL");

        cbjam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cbjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbjamActionPerformed(evt);
            }
        });

        cbmenit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cbmenit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmenitActionPerformed(evt);
            }
        });

        cbdetik.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pilih Lagu :");

        lberror.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lberror.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        playlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playlistMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(playlist);

        jButton2.setText("Browse..");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lberror, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbltanda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(submit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(cbjam, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(cbmenit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cbdetik, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(btstop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btntunda, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 25, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbjam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbmenit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbdetik, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbjam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbmenit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbdetik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbjam, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbmenit, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbdetik, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(submit)
                            .addComponent(reset))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btstop, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btntunda))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(lberror, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltanda, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        submit();
    }//GEN-LAST:event_submitActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        reset();
    }//GEN-LAST:event_resetActionPerformed

    private void btstopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btstopActionPerformed
        stop();
    }//GEN-LAST:event_btstopActionPerformed

    private void cbjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbjamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbjamActionPerformed

    private void btntundaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntundaActionPerformed
        // TODO add your handling code here:
        tunda();
    }//GEN-LAST:event_btntundaActionPerformed

    private void cbmenitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmenitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbmenitActionPerformed

    private void playlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playlistMouseClicked
        // TODO add your handling code here:
        select();
    }//GEN-LAST:event_playlistMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        add();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new alarm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btntunda;
    private javax.swing.JButton btstop;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbdetik;
    private javax.swing.JComboBox<String> cbjam;
    private javax.swing.JComboBox<String> cbmenit;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbdetik;
    private javax.swing.JLabel lberror;
    private javax.swing.JLabel lbjam;
    private javax.swing.JLabel lbltanda;
    private javax.swing.JLabel lbmenit;
    private javax.swing.JList<String> playlist;
    private javax.swing.JButton reset;
    private javax.swing.JButton submit;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables

}
