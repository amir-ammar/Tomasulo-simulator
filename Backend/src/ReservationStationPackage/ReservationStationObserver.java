package ReservationStationPackage;

import MessagesPackage.Message;

public interface ReservationStationObserver {
    public void updateInstructionQueue(Message message);

    public  void updateRegisterFile(Message message);

    public void updateDataBus(Message message);
}
