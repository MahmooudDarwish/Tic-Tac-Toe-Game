/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.constants;

/**
 *
 * @author Mahmoud
 */
public abstract class AppConstants {

    //ICONS
    public final static String xIconPath = "/assets/icons/XIcon.png";
    public final static String oIconPath = "/assets/icons/OIcon.png";
    public final static String warningIconPath = "/assets/icons/warningIcone.png";
    public final static String doneIconPath = "/assets/icons/doneIcone.png";
    public final static String backIconPath = "/assets/icons/BackIcon.png";
    public final static String userIconPath = "/assets/icons/UserIcon.png";
    public final static String cupIconPath = "/assets/icons/CupIcon.png";

    //IMAGES
    public final static String bachgroundImagePath = "/assets/images/XOImage.jpg";

    //TONES
    public final static String buttonClickedTonePath = "/assets/tones/buttonClicked.wav";
    public final static String buttonClickedTonePath1 = "/assets/tones/gameBoardButtonClicked.mp3";

    //VIDEOS
    public final static String winVideoPath = "src/assets/videos/winVideo.mp4";
    public final static String loseVideoPath = "src/assets/videos/loseVideo.mp4";
    public final static String drawVideoPath = "src/assets/videos/drawVideo.mp4";

    //PATHS
    public final static String loginPath = "/screens/login_screen/LoginScreen.fxml";
    public final static String gameModePath = "/screens/game_mode_screen/GameModeUi.fxml";
    public final static String connectionModePath = "/screens/connection_mode_screen/ConnectionModeScreen.fxml";
    public final static String signupModePath = "/screens/signup_screen/SignupScreen.fxml";
    public final static String userHomePath = "/screens/user_home_screen/UserHomeScreen.fxml";
    public final static String gameBoardScreenPath = "/screens/game_board_screen/GameBoardScreen.fxml";
    public final static String aiModeScreenPath = "/screens/ai_mode_screen/AiMode.fxml";
    public final static String enterServerIpScreen ="/screens/enter_ip_screen/EnterServerIp.fxml";



    //SERVER
    private  static String serverIp;
    
    public  static void  setServerIp (String ip)
    {
        serverIp=ip;
    }
    public  static String  getServerIp()
    {
        return serverIp;
    }
    
     

}
