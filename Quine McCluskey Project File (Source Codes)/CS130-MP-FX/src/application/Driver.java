package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Driver {

	@FXML
    private Button submit, back;

	@FXML
	private Text solution;

	@FXML
	private TextField numbers, numbers2;
	
	@FXML
	private ScrollPane scrollpane;
	
	@FXML
	public void submit(ActionEvent event) {
		
		String input = numbers.getText();
		input = input.replaceAll("\\s","");
		
		String dc = numbers2.getText();
		dc = dc.replaceAll("\\s","");
		
		ArrayList<String> mintermsRaw = new ArrayList<String>();
		Collections.addAll(mintermsRaw, input.split(","));
		ArrayList<String> dcRaw = new ArrayList<String>();
		if(!dc.isEmpty())
			Collections.addAll(dcRaw, dc.split(","));
		
		try {
			openSolution();
			InputHandler handler = new InputHandler();
			handler.generateInput(mintermsRaw, dcRaw);
			QuineMcCluskey qm = new QuineMcCluskey(handler);
			qm.solve();
			solution = new Text(qm.toPrint.toString());
			solution.setFill(Color.WHITE);
			solution.setWrappingWidth(500);;
			scrollpane.setContent(solution);
            
		} catch (Exception e) {
			System.out.print("Invalid input!");
			solution = new Text("ERROR! There must be a problem with your input.");
			solution.setFill(Color.RED);
			scrollpane.setContent(solution);
		}
		
		
	}
	
	@FXML
	public void openSolution() throws IOException {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("solution.fxml"));
            loader.setController(this);
            Parent parent = loader.load();
            ((Stage)submit.getScene().getWindow()).setScene(new Scene(parent, 600,680));
            
            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                	try {
            			FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("main1.fxml"));
                        Parent parent = loader.load();
                        ((Stage)back.getScene().getWindow()).setScene(new Scene(parent, 600,680));
                        
                    } catch (IOException eox) {
                        eox.printStackTrace();
                    }
                }
            });
          
        } catch (IOException eox) {
            eox.printStackTrace();
        }
	}
}

//6,9,13,18,19,25,27,29,41,45,57,61
//20,28,52,60
//0,1,2,5,6,7,8,9,10,14
//1,3,5,7,9,10,11,13,17,19,21,23,24,25,26,27,28,29
//0,2,5,6,7,8,10,12,13,14,15
