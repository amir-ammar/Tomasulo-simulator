package ReservationStationPackage;

import MessagesPackage.Message;

public interface ReservationStationObserver {
    public void ReservationUpdateInstructionQueue(Message message);

    public  void ReservationUpdateRegisterFile(Message message);

    public void ReservationUpdateDataBus(Message message);
    public void ReservationUpdateLoadBuffer(Message message);

    public void ReservationUpdateStoreBuffer(Message message);

}
