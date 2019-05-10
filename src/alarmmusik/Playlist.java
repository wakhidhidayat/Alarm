/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmmusik;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


/**
 *
 * @author Wahid
 */
public class Playlist {
    JFileChooser fc = new JFileChooser();
    ArrayList ls = new ArrayList();
    
    void add(JFrame frame){
        fc.setMultiSelectionEnabled(true);
        int fileValid = fc.showOpenDialog(frame);
        if (fileValid == javax.swing.JFileChooser.CANCEL_OPTION){
            return;
        } else if (fileValid == javax.swing.JFileChooser.APPROVE_OPTION){
            File[] file = fc.getSelectedFiles();
            ls.addAll(Arrays.asList(file));
        }
    }
    
    ArrayList getListSong(){
        return ls;
    }
}
