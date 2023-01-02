package StoreBuffer;

import MessagesPackage.Message;

public interface StoreBufferObserver {
    public void storeBufferUpdateQueue(Message message);
    public void storeBufferUpdateRegFile(Message message);
    public void storeBufferUpdateReservation(Message message);


}
