package mpower.org.elearning_module.interfaces;

/**
 * Created by sabbir on 10/26/17.
 */

public interface AudioPlayerListener {
    void playAudio(String name);
    void stopPlayer();
    void mutePlayer(boolean flag);
    void pausePlayer();
    void resume();
}
