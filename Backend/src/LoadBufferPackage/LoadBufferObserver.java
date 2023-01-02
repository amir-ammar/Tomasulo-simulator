package LoadBufferPackage;

import MessagesPackage.Message;

public interface LoadBufferObserver {
    public void LoadBufferUpdateQueue(Message message);
    public void LoadBufferUpdateRegFile(Message message);

    public void LoadBufferUpdateDataBus(Message message);

    public void LoadBufferUpdateReservation(Message message);

}
