package network;

/**
 * Message (abstract base class)
 * Owner: Member A (Networking & Concurrency)
 *
 * Responsibility:
 *   - Base type for everything sent between GameClient and GameServer/ClientHandler.
 *   - Needs to be serializable (implements Serializable, or define a JSON encoding
 *     if you choose to send Messages as JSON strings instead of raw Java objects).
 *
 * TODO:
 *   - [ ] Decide on wire format: Java object serialization vs JSON strings
 *         (JSON is already the chosen format for persistence — consider reusing it
 *         here for consistency, but a raw ObjectOutputStream is simpler to start with)
 *   - [ ] Define the `type` field / discriminator so the receiving end knows which
 *         Message subclass it got
 */
public abstract class Message /* implements Serializable */ {
    // TODO: field - String type
}
