
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class DiffusionIHMController implements Initializable {

    @FXML
    private BorderPane bp;
    
    @FXML
    private Label lHoraires;

    @FXML
    private Button bPleinEcran;

    @FXML
    private Button bQuitter;

    @FXML
    private MediaView mv;

    @FXML
    private ListView<Label> liste;
        
    private HashMap<String, MediaPlayer> listeMediaPlayers;
    
    private Scene scene_plein_ecran;
    private StackPane stackPane;
    int i =0;
    int tabl[]= new int[100];
  
  
    
    public void activerModePleinEcran() {
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        stackPane.getChildren().add(mv);
        Regub.stage.setScene(scene_plein_ecran);
        Regub.stage.setFullScreen(true);
    }
    
    public void desactiverModePleinEcran() {
        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();
        width.unbind();
        height.unbind();
        mv.fitHeightProperty().set(200);
        mv.fitWidthProperty().set(400);
        
        bp.setCenter(mv);
        Regub.stage.setScene(Regub.scene_principale);
        Regub.stage.setFullScreen(false);  
    }
    
    @FXML
    private void handleButtonPleinEcranAction(ActionEvent event) {
        activerModePleinEcran();
    }
    
    @FXML
    private void handleButtonQuitterAction(ActionEvent event) {
        try {
            Regub.terminer();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DiffusionIHMController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DiffusionIHMController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
          for(int t:tabl)tabl[t]=0;
        //afficher horaires du jour
        String[] days = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
        StringBuilder sb = new StringBuilder();
        sb.append(days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)].toUpperCase()).append(" : (");
        sb.append(String.format("%02d", Regub.horaires[0].get(Calendar.HOUR_OF_DAY))).append("h");
        sb.append(String.format("%02d", Regub.horaires[0].get(Calendar.MINUTE))).append(" - ");
        sb.append(String.format("%02d", Regub.horaires[1].get(Calendar.HOUR_OF_DAY))).append("h");
        sb.append(String.format("%02d", Regub.horaires[1].get(Calendar.MINUTE))).append(")");
        lHoraires.setText(sb.toString());
        
        //création de la liste de mediaplayer
        listeMediaPlayers = new HashMap<>();
        Media media = new Media(new File("videos/pause.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        listeMediaPlayers.put("pause", mediaPlayer);
        Regub.playlist.getListeContrats().stream().forEach((c) -> {
            Media m = new Media(new File("videos/" + c.getIdVideo() + ".mp4").toURI().toString());
            MediaPlayer mp = new MediaPlayer(m);
            listeMediaPlayers.put(Integer.toString(c.getIdVideo()), mp);
        });
        
        //initialiser la scene de plein ecran
        stackPane = new StackPane();
        scene_plein_ecran = new Scene(stackPane);
        scene_plein_ecran.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                desactiverModePleinEcran();
            }
        });
        
        ObservableList<Label> items = FXCollections.observableArrayList();
                
        for (Diffusion d : Regub.playlist.getListeDiffusions()) {
            sb = new StringBuilder();
            sb.append("(").append(String.format("%02d", d.getHeureDiffusion().get(Calendar.HOUR_OF_DAY))).append(":");
            sb.append(String.format("%02d", d.getHeureDiffusion().get(Calendar.MINUTE))).append(":");
            sb.append(String.format("%02d", d.getHeureDiffusion().get(Calendar.SECOND))).append(") ");
            sb.append(d.getContrat().getTitre());
            Label label = new Label(sb.toString());
            items.add(label);
         
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    listeMediaPlayers.get("pause").stop();
                    MediaPlayer mediaPlayer = listeMediaPlayers.get(Integer.toString(d.getContrat().getIdVideo()));
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.setOnEndOfMedia(() -> {
                        try {
                            FichierController.loguer_diffusion(d);
                        } catch (IOException ex) {
                            Logger.getLogger(DiffusionIHMController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        d.getContrat().setFrequence(d.getContrat().getFrequence()-1);
                        try {
                            FichierController.getInstance().sauverContratsADiffuser(Regub.playlist.getListeContrats());
                        } catch (RegubException ex) {
                            Logger.getLogger(DiffusionIHMController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        tabl[d.getContrat().getIdVideo()]++;
                        
//                   int nomb1 = 0;  
//          int nomb2 = 0;  
//          int nomb3 = 0;  
//          int nomb4 = 0;  
//          int nomb5 = 0;  
//                        if(d.getContrat().getIdVideo() == 0){
//                             nomb1++ ;
//                             label.setText(label.getText() + " : Diffusée "+nomb1);
//                        }
//                        
                        
                        label.setText(label.getText() + " : Diffusée "+tabl[d.getContrat().getIdVideo()]+" fois");
                        MediaPlayer pauseMediaPlayer = listeMediaPlayers.get("pause");
                        pauseMediaPlayer.seek(Duration.ZERO);
                        mv.setMediaPlayer(pauseMediaPlayer);
                        pauseMediaPlayer.play();
                    });
                    mv.setMediaPlayer(mediaPlayer);
                    mediaPlayer.play();
                }
            }, d.getHeureDiffusion().getTime());
        }

        liste.setItems(items);
        mv.setMediaPlayer(listeMediaPlayers.get("pause"));
        mv.getMediaPlayer().play();
    }
}
