package logic.exceptions;

/**
 * Custom exception which will be thrown if the bot rotation of loaded level file is invalid.
 *
 * @author Timo Peters
 */
public class InvalidBotRotationException extends Exception {
    public InvalidBotRotationException(int botRotation) {
        super("InvalidBotRotationException: BotRotation was " + botRotation);
    }
}
