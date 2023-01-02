package LoadBufferPackage;

public interface LoadBufferSubject {
    public void registerObserver(LoadBufferObserver o);
    public void removeObserver(LoadBufferObserver o);
    public void notifyInstructionQueue();
    public void notifyRegFile();
    public void notifyReservation();

    public void notifyDataBus(String data);

}
