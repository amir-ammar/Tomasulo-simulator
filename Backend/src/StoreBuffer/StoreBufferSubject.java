package StoreBuffer;

public interface StoreBufferSubject {
    public void registerObserver(StoreBufferObserver o);
    public void removeObserver(StoreBufferObserver o);
    public void notifyInstructionQueue();

    public void notifyReservationStation();

    public void notifyRegisterFile();

}
