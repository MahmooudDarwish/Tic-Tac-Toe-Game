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
    public final static String userIconPath = "/assets/icons/userIcon.png";
    public final static String cupIconPath = "/assets/icons/cupIcon.png";
    public final static String playRecordsIconPath = "/assets/icons/playRecordsIcon.png";
    public final static String playNewGameIconPath = "/assets/icons/playNewGameIcon.png";
    public final static String logoutIconPath = "/assets/icons/logoutIcon.png";
    public final static String loadingIcone1Path = "/assets/icons/loadingIcone1.png";
    public final static String loadingIcone2Path = "/assets/icons/loadingIcone2.png";
    
    public final static String playIconePath = "/assets/icons/play-station.png";
    public final static String palyIcone2Path = "/assets/icons/play.png";


    //IMAGES
    public final static String bachgroundImagePath = "/assets/images/XOImage.jpg";
    public final static String bluredbachgroundImagePath = "/assets/images/BluredXOImage.jpg";

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
    public final static String onlinegameBoardScreenPath = "/screens/online_game_board_screen/OnlineGameBoardScreen.fxml";

    public final static String aiModeScreenPath = "/screens/ai_mode_screen/AiMode.fxml";
    public final static String enterServerIpScreen = "/screens/enter_ip_screen/EnterServerIp.fxml";
    public final static String gameLoppyPath = "/screens/lobby_screen_mode/LobbyScreenUi.fxml";
    public final static String playerhistoryPath = "/screens/player_history_screen/PlayerHistory.fxml";
    public final static String startScreenPath = "/screens/start_screen/StartScreen.fxml";
    public final static String playRecordScreenPath = "/screens/play_record_screen/PlayRecord.fxml";
    //SERVER
    private static String serverIp;
    private static final int SERVER_PORT = 5006;

    public static int getServerPort() {
        return SERVER_PORT;
    }

    public static void setServerIp(String ip) {
        serverIp = ip;
    }

    public static String getServerIp() {
        return serverIp;
    }

}
