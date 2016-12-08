package com.daniel.semarbeit.ui;

import com.daniel.semarbeit.user.Category;
import com.daniel.utils.Dialogs;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import com.daniel.semarbeit.user.NoteSet;
import com.daniel.utils.Transitions;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Daniel
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button btnLoadNoteSet;
    @FXML
    private BarChart crtInstruments;
    @FXML
    private BarChart crtCategories;
    @FXML
    private FlowPane flpCategoryButtons;
    
    private NoteSet noteSet;

    private void updateInstrumentChart(int selectedCategory) {
        crtInstruments.getData().clear();
        crtInstruments.getData().add(noteSet.getInstrumentsChartDataset(selectedCategory));
        Transitions.playFadeTransition(crtInstruments, 400, 0, 1);
    }
    
    private void updateCategoryChart() {
        crtCategories.getData().clear();
        crtCategories.getData().add(noteSet.getCategoriesChartDataset());
        Transitions.playFadeTransition(crtInstruments, 400, 0, 1);
    }
    
    private void updateCategoryButtons() {
        flpCategoryButtons.getChildren().clear();
        for(Integer i : noteSet.getCategories().keySet()) {
            Button btn = new Button(Category.getCategoryName(i));
            btn.setPrefHeight(40);
            btn.setPrefWidth((flpCategoryButtons.getPrefWidth()-noteSet.getCategories().keySet().size()*3)/10);
            btn.setOnAction(a -> updateInstrumentChart(i));
            flpCategoryButtons.getChildren().add(btn);
        }
    }
    
    private void update() {
        updateCategoryChart();
        updateInstrumentChart(1);
        updateCategoryButtons();
    }
    
    @FXML
    public void btnLoadNoteSetAction(ActionEvent event) {
        try {           
            String path = Dialogs.chooseFileDialog("NoteSet Datei auswählen");            
            noteSet.deserialize(path);
            update();
        } catch (IOException ex) {
            Dialogs.alert("Alert", "Something went wrong", "Die Datei konnte nicht eingelesen werden");
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @FXML
    public void windowClosingAction(ActionEvent event) {
        try {
            noteSet.serialize("I:\\Informatik\\semArbeit\\SemArbeit\\src\\com\\daniel\\semarbeit\\notes\\saved_notes.mc");
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noteSet = new NoteSet(); 
        try {
            noteSet.deserialize("I:\\Informatik\\semArbeit\\SemArbeit\\src\\com\\daniel\\semarbeit\\notes\\saved_notes.mc"); 
            update();
        } catch (IOException ex) {
            Dialogs.alert("Alert", "Something went wrong", "Die gespeicherten Noten konnten nicht eingelesen werden");
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }    
    
}
