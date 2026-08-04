package network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests derived from the UML: Message subclasses (PlayCardMessage,
 * EndTurnMessage, StateUpdateMessage) each carry a `type` discriminator
 * plus their own fields. These are fully implemented already (simple data
 * carriers) — this test file should pass immediately and mainly guards
 * against accidental changes.
 */
class MessageTest {

    @Test
    void playCardMessageCarriesCardAndLane() {
        PlayCardMessage message = new PlayCardMessage("Wolf Pup", 1);
        assertEquals("PLAY_CARD", message.getType());
        assertEquals("Wolf Pup", message.getCardName());
        assertEquals(1, message.getLaneIndex());
    }

    @Test
    void endTurnMessageHasCorrectType() {
        EndTurnMessage message = new EndTurnMessage();
        assertEquals("END_TURN", message.getType());
    }

    @Test
    void stateUpdateMessageCarriesPayload() {
        StateUpdateMessage message = new StateUpdateMessage("{\"life\":25}");
        assertEquals("STATE_UPDATE", message.getType());
        assertEquals("{\"life\":25}", message.getStatePayload());
    }
}
