package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.Subscriber;

/**
     *A class implements {@link Broadcast}. When sending Broadcast messages
     * using the {@link MessageBroker} it will be received by all the subscribers of this
     * Broadcast-message type (the message Class).
     * <p>
     * in this object the current tick that will be broadcast to the {@link Subscriber}
     * <p>
     */

    public class TickBroadcast implements Broadcast {
        private int Tick;
        /**
         * @param tick the tick that wanted to broadcast
         */
        public TickBroadcast(int tick){
            this.Tick = tick;
        }

        /**
         *
         * @return the tick of this broadcast
         */
        public int getTick() {
            return Tick;
        }

    }
