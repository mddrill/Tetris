/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res.sound;

import java.applet.Applet;
import java.applet.AudioClip;

/**
 *
 * @author mddrill
 */
public enum Sound {
    clearRowSound(Applet.newAudioClip(Sound.class.getResource("Clear_Row.wav"))),
    errorSound(Applet.newAudioClip(Sound.class.getResource("Error.wav"))),
    gameSong(Applet.newAudioClip(Sound.class.getResource("Game_Song.wav")));
    
    private final AudioClip audioclip;

    private Sound(AudioClip audioclip) {
        this.audioclip = audioclip;
    }

    public static void playClearRowSound() {
        clearRowSound.audioclip.play();
    }

    public static void playErrorSound() {
        errorSound.audioclip.play();
    }
    public static void playGameSong() {
        gameSong.audioclip.play();
    }

    public static Sound getClearRowSound() {
        return clearRowSound;
    }

    public static Sound getErrorSound() {
        return errorSound;
    }

    public static Sound getGameSong() {
        return gameSong;
    }

    public AudioClip getAudioclip() {
        return audioclip;
    }
    
    
    
    
}
