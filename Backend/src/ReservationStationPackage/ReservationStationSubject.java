package ReservationStationPackage;

import ReservationStationPackage.ReservationStationObserver;

public interface ReservationStationSubject {
    public void registerObserver(ReservationStationObserver o);
    public void removeObserver(ReservationStationObserver o);
    public void notifyInstructionQueue();

    public void notifyRegisterFile();

    public void notifyDataBus(String data);

    public void notifyLoadBuffer(String data);
    public void notifyStoreBuffer();

}
