import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class JavaTextToSpeech implements Text2Speech {

    public void textToSpeech(String text) {
        if (text.length() > 0) {
            VoiceManager voiceManager = VoiceManager.getInstance();
            Voice helloVoice = voiceManager.getVoice("kevin");

            /* Allocates the resources for the voice.
             */
            helloVoice.allocate();

            /* Synthesize speech.
             */
            helloVoice.speak(text);

            /* Clean up and leave.
             */
            helloVoice.deallocate();
        }
    }
}
